import { Component, OnInit } from '@angular/core';
import { TransactionService } from '../../../shared/services';
import { FormBuilder } from '@angular/forms';
import { ToastrManager } from 'ng6-toastr-notifications';
import { MatDialogRef } from '@angular/material';
import _ from 'lodash';

@Component({
    selector: 'delete-record-dialog',
    templateUrl: './delete-record.component.html',
    styleUrls: ['./delete-record.component.scss'],
})

export class DeleteRecordDialog implements OnInit {

    table: string;
    hash_key: {};
    range_key: {};
    item: {};
    currentRecord: {}

    constructor(private transactionsService: TransactionService,
                public dialogRef: MatDialogRef<DeleteRecordDialog>,
                public toastr: ToastrManager,
                private fb: FormBuilder) {
        console.log('delete record dialog');

    }

    ngOnInit(): void {
        console.log('table: ', this.table);
        console.log('hash_key: ', this.hash_key);

        this.item = {
            'table_name': this.table,
            'hash_key': {
                'attribute_name': _.get(this.hash_key, 'attribute'),
                'attribute_value': _.get(this.currentRecord, _.get(this.hash_key, 'attribute'))
            },
            'range_key': {
                'attribute_name': _.get(this.range_key, 'attribute'),
                'attribute_value': _.get(this.currentRecord, _.get(this.range_key, 'attribute'))
            }
        }

        console.log('item to be deleted: ', this.item);
    }


    deleteRecord(): void {
        this.transactionsService.deleteRecord(this.item).subscribe(success => {
            this.showResultToast(true);
            this.dialogRef.close(this.item);
        }, err => {
            this.showResultToast(false);
        })
    }

    showResultToast(isSuccess: boolean) {
        let message;

        if (isSuccess === true) {
            message = 'Successfully deleted record.';
            this.toastr.successToastr('', message, {
                position: 'bottom-full-width',
                animate: 'slideFromBottom'
            });
        } else {
            message = 'Failed to delete record';
            this.toastr.errorToastr('', message, {
                position: 'bottom-full-width',
                animate: 'slideFromBottom'
            });
        }
    }

    cancel(): void {
        this.dialogRef.close(this.item);
    }
}
