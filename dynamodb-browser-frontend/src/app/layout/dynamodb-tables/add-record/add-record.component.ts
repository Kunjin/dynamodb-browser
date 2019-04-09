import { Component, OnInit } from '@angular/core';
import _ from 'lodash';
import { TransactionService } from '../../../shared/services';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { Item, Attribute } from './item';

const HASH = 'HASH';
const RANGE = 'RANGE';

@Component({
    selector: 'add-record-dialog',
    templateUrl: './add-record.component.html',
    styleUrls: ['./add-record.component.scss'],
})

export class AddRecordDialog implements OnInit {

    table: string;
    itemForm: FormGroup;
    keySchema: {};


    constructor(private transactionsService: TransactionService,
                private fb: FormBuilder) {
        console.log('add record dialog')
    }

    ngOnInit(): void {

        console.log('add-record :', this.keySchema);

            // this.transactionsService.getTableDetails(this.table).subscribe(records => {
        //     console.log(`${this.table} details: `, records);
        //
        //     let hash_key = {};
        //     let range_key = {};
        //
        //     //get key schema
        //     for (let i = 0; i < records.table.keySchema.length; i++) {
        //
        //         if (HASH === records.table.keySchema[i].keyType) {
        //             _.set(hash_key, 'attribute', records.table.keySchema[i].attributeName);
        //         } else if (RANGE === records.table.keySchema[i].keyType) {
        //             _.set(range_key, 'attribute', records.table.keySchema[i].attributeName);
        //         }
        //     }
        //
        //     // get data type of keys
        //     for (let i = 0; i < records.table.attributeDefinitions.length; i++) {
        //
        //         if (hash_key['attribute'] === records.table.attributeDefinitions[i].attributeName) {
        //             _.set(hash_key, 'data_type', records.table.attributeDefinitions[i].attributeType);
        //         }
        //
        //         if (range_key['attribute'] === records.table.attributeDefinitions[i].attributeName) {
        //             _.set(range_key, 'data_type', records.table.attributeDefinitions[i].attributeType);
        //         }
        //     }
        //     _.set(this.keySchema, 'hash_key', hash_key);
        //     _.set(this.keySchema, 'range_key', range_key);
        //
        //
        // });

        this.itemForm = this.fb.group({
            table_name: this.table,
            attributes: this.fb.array([this.fb.group({key:'', attribute_name: '', data_type: '', attribute_value: ''})])
        })
    }

    get attributes() {
        return this.itemForm.get('attributes') as FormArray;
    }

    addAttribute() {
        this.attributes.push(this.fb.group({key:'', attribute_name: '', data_type: '', attribute_value: ''}));
    }

    deleteAttribute(index) {
        this.attributes.removeAt(index);
    }
}
