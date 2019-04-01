import { Component, OnInit } from '@angular/core';
import { routerTransition } from '../../router.animations';
import { TransactionService } from '../../shared/services';
import { ActivatedRoute } from '@angular/router';
import { MatTableDataSource } from '@angular/material';
import _ from 'lodash';

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
    selected = 'scan';
    tableParam = '';
    keySchema = {};
    selectedRangeKeyOperation = '';
    selectedHashKeyOperation = '';
    rangeKeyValue = '';
    hashKeyValue = '';

    constructor(private transactionsService: TransactionService,
                private activatedRoute: ActivatedRoute) {
    }

    ngOnInit(): void {
        this.activatedRoute.params.subscribe(params => {
            this.selected = 'scan';
            this.reinitializeDataTable();
            this.tableParam = params['table'];
            this.transactionsService.getRecordsByTables(this.tableParam).subscribe(records => {
                for (let column in JSON.parse(records[0])) {
                    this.columnDefs.push(column);
                }
                this.dataSource = records;
                let recordsArr = [];
                //TODO: For now.. Look on how to handle this better!!
                for (let i = 0; i < records.length; i++) {
                    recordsArr.push(JSON.parse(records[i]));
                }
                this.addDataInDataTable(recordsArr);
            })

            this.transactionsService.getTableDetails(this.tableParam).subscribe(records => {
                console.log(`${this.tableParam} details: `, records);

                let hash_key = {};
                let range_key = {};

                //get key schema
                for (let i = 0; i < records.table.keySchema.length; i++) {

                    if (HASH === records.table.keySchema[i].keyType) {
                        _.set(hash_key, 'attribute', records.table.keySchema[i].attributeName);
                    }

                    if (RANGE === records.table.keySchema[i].keyType) {
                        _.set(range_key, 'attribute', records.table.keySchema[i].attributeName);
                    }
                }

                // get data type of keys
                for (let i = 0; i < records.table.attributeDefinitions.length; i++) {

                    if (hash_key['attribute'] === records.table.attributeDefinitions[i].attributeName) {
                        _.set(hash_key, 'data_type', records.table.attributeDefinitions[i].attributeType);
                    }

                    if (range_key['attribute'] === records.table.attributeDefinitions[i].attributeName) {
                        _.set(range_key, 'data_type', records.table.attributeDefinitions[i].attributeType);
                    }
                }

                _.set(this.keySchema, 'hash_key', hash_key);
                _.set(this.keySchema, 'range_key', range_key);
                console.log(this.keySchema);
            })

        });

        this.transactionsService.getOperations().subscribe(operations => {
            this.operations = operations;
        });
    }

    private reinitializeDataTable() {
        this.dataSource = new MatTableDataSource<any>();
        this.columnDefs = [];
    }

    private addDataInDataTable(arr: any[]) {
        this.dataSource = new MatTableDataSource(arr);
    }

    search() {
        console.log(`selected operation: ${this.selectedHashKeyOperation}`);
        console.log(`range key value: ${this.hashKeyValue}`);

        //TODO: refactor this
        if (this.rangeKeyValue !== '') {
            this.transactionsService.getRecordByHashKeyRangeKey(
                this.tableParam,
                //_.get(this.keySchema, 'hash_key'['attribute']),
                //TODO: refactor use _.get
                this.keySchema['hash_key']['attribute'],
                this.selectedHashKeyOperation,
                this.hashKeyValue,
                this.keySchema['range_key']['attribute'],
                this.selectedRangeKeyOperation,
                this.rangeKeyValue).subscribe(result => {

                let recordsArr = [];
                //TODO: For now.. Look on how to handle this better!!
                for (let i = 0; i < result.length; i++) {
                    recordsArr.push(JSON.parse(result[i]));
                }
                this.addDataInDataTable(recordsArr);
            })
        } else {
            this.transactionsService.getRecordByHashKey(
                this.tableParam,
                //_.get(this.keySchema, 'hash_key'['attribute']),
                //TODO: refactor use _.get
                this.keySchema['hash_key']['attribute'],
                this.selectedHashKeyOperation,
                this.hashKeyValue).subscribe(result => {

                let recordsArr = [];
                //TODO: For now.. Look on how to handle this better!!
                for (let i = 0; i < result.length; i++) {
                    recordsArr.push(JSON.parse(result[i]));
                }
                this.addDataInDataTable(recordsArr);
            })
        }
    }
}
