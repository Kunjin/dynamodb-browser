import { Component, OnInit } from '@angular/core';
import { TransactionService } from '../../../shared/services';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { ToastrManager } from 'ng6-toastr-notifications';

@Component({
    selector: 'add-record-dialog',
    templateUrl: './add-record.component.html',
    styleUrls: ['./add-record.component.scss'],
})

export class AddRecordDialog implements OnInit {

    table: string;
    itemForm: FormGroup;
    keySchema: {};
    hash_key: FormGroup;
    range_key: FormGroup;

    constructor(private transactionsService: TransactionService,
                public toastr: ToastrManager,
                private fb: FormBuilder) {
        console.log('add record dialog')
    }

    ngOnInit(): void {

        console.log('add-record :', this.keySchema);

        this.hash_key = this.fb.group({ attribute_name: this.keySchema['hash_key']['attribute'], data_type: this.keySchema['hash_key']['data_type'], attribute_value: ''})
        this.range_key = this.fb.group({ attribute_name: this.keySchema['range_key']['attribute'], data_type: this.keySchema['range_key']['data_type'], attribute_value: ''});

        this.itemForm = this.fb.group({
            table_name: this.table,
            hash_key: this.hash_key,
            range_key: this.range_key,
            attributes: this.fb.array([this.fb.group({attribute_name: '', data_type: '', attribute_value: ''})])
        })
    }

    get attributes() {
        return this.itemForm.get('attributes') as FormArray;
    }

    addAttribute() {
        this.attributes.push(this.fb.group({ attribute_name: '', data_type: '', attribute_value: ''}));
    }

    deleteAttribute(index) {
        this.attributes.removeAt(index);
    }

    save() {
        this.transactionsService.saveRecord(this.itemForm.value).subscribe(result => {
            console.log('result on saving ', result);
            this.showSuccessToast();
        })
    }

    showSuccessToast() {
        let message = 'Connection successfully saved.';
        this.toastr.successToastr('', message, {
            position: 'bottom-full-width',
            animate: 'slideFromBottom'
        });
    }
}
