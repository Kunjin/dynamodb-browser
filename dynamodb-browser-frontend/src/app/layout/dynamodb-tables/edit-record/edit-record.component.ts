import { Component, OnInit } from '@angular/core';
import { TransactionService } from '../../../shared/services';
import { FormArray, FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { ToastrManager } from 'ng6-toastr-notifications';
import { MatDialogRef } from '@angular/material';
import _ from 'lodash';

@Component({
    selector: 'edit-record-dialog',
    templateUrl: './edit-record.component.html',
    styleUrls: ['./edit-record.component.scss'],
})

export class EditRecordDialog implements OnInit {

    itemForm: FormGroup;
    selectedItem: {};
    hash_key: FormGroup;
    range_key: FormGroup;


    constructor(private transactionsService: TransactionService,
                public dialogRef: MatDialogRef<EditRecordDialog>,
                public toastr: ToastrManager,
                private fb: FormBuilder) {
        console.log('edit record dialog');
    }

    ngOnInit(): void {
        this.hash_key = this.fb.group({
            attribute_name: new FormControl(this.selectedItem['hash_key']['attribute_name']),
            data_type: new FormControl(this.selectedItem['hash_key']['data_type']),
            attribute_value: new FormControl(this.selectedItem['hash_key']['attribute_value'])
        })
        this.range_key = this.fb.group({
            attribute_name: new FormControl(this.selectedItem['range_key']['attribute_name']),
            data_type: new FormControl(this.selectedItem['range_key']['data_type']),
            attribute_value: new FormControl(this.selectedItem['range_key']['attribute_value'])
        });

        let formArrays = this.selectedItem['attributes'].map(attribute => this.fb.group(attribute));
        this.itemForm = this.fb.group({
            table_name: this.selectedItem['table_name'],
            hash_key: this.hash_key,
            range_key: this.range_key,

            // attributes: this.fb.array([this.fb.group(this.selectedItem['attributes'])])
            attributes: this.fb.array(formArrays)
        })
    }

    get attributes() {
        return this.itemForm.get('attributes') as FormArray;
    }

    addAttribute() {
        this.attributes.push(this.fb.group({attribute_name: '', data_type: '', attribute_value: ''}));
    }

    deleteAttribute(index) {
        this.attributes.removeAt(index);
    }

    update() {
        this.transactionsService.saveRecord(this.itemForm.value).subscribe(result => {
            console.log('result on saving ', result);
            this.showResultToast(true);
            this.dialogRef.close(this.itemForm.value);
        }, err => {
            this.showResultToast(false);
        })
    }

    showResultToast(isSuccess: boolean) {
        let message;

        if (isSuccess === true) {
            message = 'Record successfully saved.';
            this.toastr.successToastr('', message, {
                position: 'bottom-full-width',
                animate: 'slideFromBottom'
            });
        } else {
            message = 'Failed to saved record';
            this.toastr.errorToastr('', message, {
                position: 'bottom-full-width',
                animate: 'slideFromBottom'
            });
        }
    }
}
