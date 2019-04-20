import { Component, OnInit } from '@angular/core';
import { routerTransition } from '../../router.animations';
import { TransactionService } from '../../shared/services';
import { ActivatedRoute } from '@angular/router';
import { MatDialog, MatTableDataSource } from '@angular/material';
import _ from 'lodash';
import { AddRecordDialog } from './add-record/add-record.component';
import { Angular5Csv } from 'angular5-csv/dist/Angular5-csv';
import { DeleteRecordDialog } from './delete-record/delete-record.component';
import { EditRecordDialog } from './edit-record/edit-record.component';

const HASH = 'HASH';
const RANGE = 'RANGE';

@Component({
    selector: 'dynamodb-tables-selector',
    templateUrl: './dynamodb-tables.component.html',
    styleUrls: ['./dynamodb-tables.component.scss'],
    animations: [routerTransition()]
})
export class DynamodbTablesComponent implements OnInit {

    columnDefs = [];
    dataSource: MatTableDataSource<any>;
    operations = [];
    selectedOption = 'scan';
    tableParam = '';
    keySchema = {};
    selectedRangeKeyOperation = 'EQUALS';
    selectedHashKeyOperation = 'EQUALS';
    rangeKeyValue = '';
    hashKeyValue = '';
    exclusiveKeys = {};
    hasRecords = true;
    renderedData: any;

    constructor(private transactionsService: TransactionService,
                private activatedRoute: ActivatedRoute,
                public dialog: MatDialog) {
    }

    ngOnInit(): void {
        this.activatedRoute.params.subscribe(params => {
            this.selectedOption = 'scan';
            this.reinitializeDataTable();
            this.tableParam = params['table'];
            this.scanTable();

            this.transactionsService.getTableDetails(this.tableParam).subscribe(records => {
                console.log(`${this.tableParam} details: `, records);

                let hash_key = {};
                let range_key = {};
                let attributes = [];


                //get key schema
                this.getKeySchema(records, hash_key, range_key);

                // get data type of keys
                for (let i = 0; i < records.table.attributeDefinitions.length; i++) {
                    let item = {};
                    const attributeType = records.table.attributeDefinitions[i].attributeType;
                    let dataType;
                    if (attributeType === 'S') {
                        dataType = 'string';
                    } else if (attributeType === 'B') {
                        dataType = 'boolean';
                    } else if (attributeType === 'N') {
                        dataType = 'number';
                    }

                    if (hash_key['attribute'] === records.table.attributeDefinitions[i].attributeName) {
                        _.set(hash_key, 'data_type', dataType);
                    } else if (range_key['attribute'] === records.table.attributeDefinitions[i].attributeName) {
                        _.set(range_key, 'data_type', dataType);
                    } else {
                        _.set(item, 'data_type', dataType);
                        _.set(item, 'attribute_name', records.table.attributeDefinitions[i].attributeName);
                        attributes.push(item);
                    }
                }

                _.set(this.keySchema, 'hash_key', hash_key);
                _.set(this.keySchema, 'range_key', range_key);
                _.set(this.keySchema, 'attributes', attributes);
                console.log('key schema:', this.keySchema);
            })

        });

        this.transactionsService.getOperations().subscribe(operations => {
            this.operations = operations;
        });
    }

    search() {
        console.log(`selected operation: ${this.selectedHashKeyOperation}`);
        console.log(`range key value: ${this.hashKeyValue}`);

        //TODO: refactor this
        if (this.rangeKeyValue !== '') {
            this.transactionsService.queryByHashKeyRangeKey(
                this.tableParam,
                //_.get(this.keySchema, 'hash_key'['attribute']),
                //TODO: refactor use _.get
                this.keySchema['hash_key']['attribute'],
                this.selectedHashKeyOperation,
                this.hashKeyValue,
                this.keySchema['range_key']['attribute'],
                this.selectedRangeKeyOperation,
                this.rangeKeyValue).subscribe(result => {

                if (result.length == 0) {
                    this.hasRecords = false;
                    return;
                }

                let recordsArr = [];
                //TODO: For now.. Look on how to handle this better!!
                for (let i = 0; i < result.length; i++) {
                    recordsArr.push(JSON.parse(result[i]));
                }
                this.initializeDataTable(recordsArr);

            })
        } else {
            this.transactionsService.queryByHashKey(
                this.tableParam,
                //_.get(this.keySchema, 'hash_key'['attribute']),
                //TODO: refactor use _.get
                this.keySchema['hash_key']['attribute'],
                this.selectedHashKeyOperation,
                this.hashKeyValue).subscribe(result => {

                if (result.length == 0) {
                    this.hasRecords = false;
                    return;
                }

                let recordsArr = [];
                //TODO: For now.. Look on how to handle this better!!
                for (let i = 0; i < result.length; i++) {
                    recordsArr.push(JSON.parse(result[i]));
                }
                this.initializeDataTable(recordsArr);
            })
        }
    }

    changeOption() {
        if ('scan' === this.selectedOption) {
            this.reinitializeDataTable();
            this.scanTable();
        } else {
            this.exclusiveKeys = {};
        }
    }

    next() {
        this.reinitializeDataTable();
        this.scanTable(this.exclusiveKeys);
    }

    createItemDialog(): void {
        const dialogRef = this.dialog.open(AddRecordDialog, {
            width: '1000px',
            height: '500px'
        });

        dialogRef.componentInstance.table = this.tableParam;
        dialogRef.componentInstance.keySchema = this.keySchema;

        dialogRef.afterClosed().subscribe(item => {
            let hk_property = _.get(item, ['hash_key', 'attribute_name']);
            let hk_value = _.get(item, ['hash_key', 'attribute_value']);
            let rk_property = _.get(item, ['range_key', 'attribute_name']);
            let rk_value = _.get(item, ['range_key', 'attribute_value']);

            let newAttributes = {};
            _.set(newAttributes, hk_property, hk_value);
            let attributes = _.get(item, 'attributes');
            for (let i= 0; i < attributes.length; i++) {
                _.set(newAttributes, _.get(attributes, [i, 'attribute_name']), _.get(attributes, [i, 'attribute_value']));
            }

            if (rk_property) {
                _.set(newAttributes, rk_property, rk_value);
            }

            this.dataSource.data.push(newAttributes);
            this.initializeDataTable(this.dataSource.data);
        });
    }

    deleteRecordDialog(element): void {
        this.transactionsService.getTableDetails(this.tableParam).subscribe(records => {
            let hash_key = {};
            let range_key = {};
            this.getKeySchema(records, hash_key, range_key);
            console.log('element:', element);

            const dialogRef = this.dialog.open(DeleteRecordDialog, {
                width: '500px',
                height: '200px'
            });

            dialogRef.componentInstance.table = this.tableParam;
            dialogRef.componentInstance.hash_key = hash_key;
            dialogRef.componentInstance.range_key = range_key;
            dialogRef.componentInstance.currentRecord = element;

            dialogRef.afterClosed().subscribe(item => {
                let hk_property = _.get(item, ['hash_key', 'attribute_name']);
                let hk_value = _.get(item, ['hash_key', 'attribute_value']);
                let rk_property = _.get(item, ['range_key', 'attribute_name']);
                let rk_value = _.get(item, ['range_key', 'attribute_value']);
                if (rk_property) {
                   _.remove(this.dataSource.data, { [hk_property] : hk_value, [rk_property]: rk_value });
                 } else {
                    _.remove(this.dataSource.data, { [hk_property] : hk_value });
                 }
                this.initializeDataTable(this.dataSource.data);
            });
        });


    }

    editRecordDialog(element): void {
        console.log('element:', element);

        let hash_key = {
            'attribute_name': this.keySchema['hash_key']['attribute'],
            'data_type': this.keySchema['hash_key']['data_type'],
            'attribute_value': _.get(element, this.keySchema['hash_key']['attribute']),
        }

        let range_key = {
            'attribute_name': this.keySchema['range_key']['attribute'],
            'data_type': this.keySchema['range_key']['data_type'],
            'attribute_value': _.get(element, this.keySchema['range_key']['attribute']),
        }

        const dialogRef = this.dialog.open(EditRecordDialog, {
            width: '1000px',
            height: '500px'
        });

        // get attribute_value of indexed keys columns
        for (let i = 0; i < this.keySchema['attributes'].length; i++) {
            _.find(this.keySchema['attributes'][i],
                _.set(this.keySchema['attributes'][i], 'attribute_value',
                    _.get(element, this.keySchema['attributes'][i].attribute_name)));
        }

        // get other non-key columns
        for (let key in element) {
            let attribute = {};
            let findAtt = { 'attribute_name': key };
            if (!_.find(this.keySchema['attributes'], findAtt) &&
                key !== this.keySchema['hash_key']['attribute'] &&
                key !== this.keySchema['range_key']['attribute']) {
                _.set(attribute, 'attribute_name', key);
                _.set(attribute, 'attribute_value', _.get(element, key));
                _.set(attribute, 'data_type', 'string');

                this.keySchema['attributes'].push(attribute);
            }
        }

        let item = {};
        _.set(item, 'hash_key', hash_key);
        _.set(item, 'range_key', range_key);
        _.set(item, 'attributes', this.keySchema['attributes']);
        _.set(item, 'table_name', this.tableParam);


        dialogRef.componentInstance.selectedItem = item;
        console.log('item to update: ', item);

        dialogRef.afterClosed().subscribe(item => {
            console.log('update: ', item);
            let hk_property = _.get(item, ['hash_key', 'attribute_name']);
            let hk_value = _.get(item, ['hash_key', 'attribute_value']);
            let rk_property = _.get(item, ['range_key', 'attribute_name']);
            let rk_value = _.get(item, ['range_key', 'attribute_value']);

            let newAttributes = {};
            _.set(newAttributes, hk_property, hk_value);
            let attributes = _.get(item, 'attributes');
            for (let i= 0; i < attributes.length; i++) {
                _.set(newAttributes, _.get(attributes, [i, 'attribute_name']), _.get(attributes, [i, 'attribute_value']));
            }

            let index;
            if (rk_property) {
                _.set(newAttributes, rk_property, rk_value);
                index = _.findIndex(this.dataSource.data, { [hk_property] : hk_value, [rk_property]: rk_value });
                _.remove(this.dataSource.data, { [hk_property] : hk_value, [rk_property]: rk_value });
            } else {
                index = _.findIndex(this.dataSource.data, { [hk_property] : hk_value });
                _.remove(this.dataSource.data, { [hk_property] : hk_value });
            }
            this.dataSource.data.splice(index, 0, newAttributes);
            this.initializeDataTable(this.dataSource.data);
        });
    }


    private scanTable(exclusiveKeys ?: object) {
        this.transactionsService.scanTable(this.tableParam, exclusiveKeys).subscribe(results => {

            if (results.records.length == 0) {
                this.hasRecords = false;
                return;
            }

            let records = _.get(results, 'records');
            this.exclusiveKeys = _.get(results, 'exclusiveKeys', null);

            if (records.length > 0) {
                for (let column in JSON.parse(records[0])) {
                    this.columnDefs.push(column);
                }

                // add action in columns
                this.columnDefs.push('actions');

                this.dataSource = results;
                let recordsArr = [];
                //TODO: For now.. Look on how to handle this better!!
                for (let i = 0; i < records.length; i++) {
                    recordsArr.push(JSON.parse(records[i]));
                }
                this.initializeDataTable(recordsArr);
            }
        })
    }

    private reinitializeDataTable() {
        this.dataSource = new MatTableDataSource<any>();
        this.columnDefs = [];
        this.hasRecords = true;
    }

    private initializeDataTable(arr: any[]) {
        this.hasRecords = arr.length > 0 ? true : false;
        this.dataSource = new MatTableDataSource(arr);
        this.dataSource.connect().subscribe(d => this.renderedData = d);
    }

    private getKeySchema(records, hash_key, range_key) {
        for (let i = 0; i < records.table.keySchema.length; i++) {

            if (HASH === records.table.keySchema[i].keyType) {
                _.set(hash_key, 'attribute', records.table.keySchema[i].attributeName);
            } else if (RANGE === records.table.keySchema[i].keyType) {
                _.set(range_key, 'attribute', records.table.keySchema[i].attributeName);
            }
        }
    }

    exportCsv() {
        new Angular5Csv(this.renderedData, 'results');
    }
}
