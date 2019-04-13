import { Component, OnInit } from '@angular/core';
import { TransactionService } from '../../../shared/services';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { ToastrManager } from 'ng6-toastr-notifications';
import { MatDialogRef } from '@angular/material';

@Component({
    selector: 'edit-record-dialog',
    templateUrl: './edit-record.component.html',
    styleUrls: ['./edit-record.component.scss'],
})

export class EditRecordDialog implements OnInit {

    table: string;
    itemForm: FormGroup;
    itemDto: {};

    constructor(private transactionsService: TransactionService,
                public dialogRef: MatDialogRef<EditRecordDialog>,
                public toastr: ToastrManager,
                private fb: FormBuilder) {
        console.log('edit record dialog');
    }

    ngOnInit(): void {

        // console.log('add-record :', this.keySchema);
        //
        // this.hash_key = this.fb.group({
        //     attribute_name: this.keySchema['hash_key']['attribute'],
        //     data_type: this.keySchema['hash_key']['data_type'],
        //     attribute_value: ''
        // })
        // this.range_key = this.fb.group({
        //     attribute_name: this.keySchema['range_key']['attribute'],
        //     data_type: this.keySchema['range_key']['data_type'],
        //     attribute_value: ''
        // });
        //
        // this.itemForm = this.fb.group({
        //     table_name: this.table,
        //     hash_key: this.hash_key,
        //     range_key: this.range_key,
        //     attributes: this.fb.array([this.fb.group({attribute_name: '', data_type: '', attribute_value: ''})])
        // })
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

    save() {
        this.transactionsService.saveRecord(this.itemForm.value).subscribe(result => {
            console.log('result on saving ', result);
            this.showResultToast(true);
            this.dialogRef.close();
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
