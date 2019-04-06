import { Component, OnInit } from '@angular/core';
import { routerTransition } from '../../router.animations';
import { AwsService, TransactionService } from '../../shared/services';
import { FormGroup, FormControl, FormBuilder } from '@angular/forms';
import { ToastrManager } from 'ng6-toastr-notifications';

@Component({
    selector: 'connection-selector',
    templateUrl: './connection.component.html',
    styleUrls: ['./connection.component.scss'],
    animations: [routerTransition()]
})
export class ConnectionComponent implements OnInit {

    connectionForm: FormGroup;
    regions: String[] = [];
    profiles: String[] = [];
    isUseAwsProfileChecked = false;

    constructor(private awsService: AwsService,
                private fb: FormBuilder,
                public toastr: ToastrManager,
                private transactionService: TransactionService) {
        this.connectionForm = fb.group({
            awsRegion: new FormControl(''),
            awsProfile: new FormControl({value: '', disabled: true}),
            isAwsProfileUsed: false,
            accessKey: new FormControl(''),
            secretKey: new FormControl('')
        })
    }

    ngOnInit(): void {
        this.awsService.getAwsRegions().subscribe(
            region => {
                this.regions = region;
            }
        )

        this.awsService.getAwsProfile().subscribe(
            profile => {
                this.profiles = profile;
            }
        )

        this.connectionForm.get('awsRegion').setValue(this.regions);
        this.connectionForm.get('awsProfile').setValue(this.profiles);

        this.loadSettings();

        this.connectionForm.get('isAwsProfileUsed').valueChanges.subscribe(checked => {
            if (checked) {
                this.isUseAwsProfileChecked = true;
                this.connectionForm.get('awsProfile').enable();
                this.enableDisableAccessSecretKey(false);
            } else {
                this.isUseAwsProfileChecked = false;
                this.connectionForm.get('awsProfile').disable();
                this.enableDisableAccessSecretKey(true);
            }
        })
    }

    loadSettings(): void {
        this.awsService.loadSettings().subscribe(data => {
            this.connectionForm.get('awsRegion').setValue(data.region);
            this.connectionForm.get('awsProfile').setValue(data.profile);
            this.connectionForm.get('isAwsProfileUsed').setValue(data.isAwsProfileUsed);
            this.connectionForm.get('accessKey').setValue(data.accessKey);
            this.connectionForm.get('secretKey').setValue(data.secretKey);
        })
    }

    enableDisableAccessSecretKey(toggle): void {
        if (toggle) {
            this.connectionForm.get('accessKey').enable();
            this.connectionForm.get('secretKey').enable();
        } else {
            this.connectionForm.get('accessKey').disable();
            this.connectionForm.get('secretKey').disable();
        }
    }

    onSubmit(): void {
        this.awsService.saveSettings(
            this.connectionForm.get('awsRegion').value,
            this.connectionForm.get('awsProfile').value,
            //this.connectionForm.get('isAwsProfileUsed').value,
            this.isUseAwsProfileChecked,
            this.connectionForm.get('accessKey').value,
            this.connectionForm.get('secretKey').value)
            .subscribe(data => {
                console.log(data);
                this.showSuccessToast();
                this.transactionService.displayTables().subscribe(
                    tables => {
                        console.log('tables: ', tables);
                        this.transactionService.setTablesCount(tables.length);
                    })
            }, err => {
                console.log("Connection failed to saved due to ", err);
                this.showFailedToast();
            });
    }

    showSuccessToast() {
        let message = 'Connection successfully saved.';
        this.toastr.successToastr('', message, {
            position: 'bottom-full-width',
            animate: 'slideFromBottom'
        });
    }

    showFailedToast() {
        let message = 'Connection was not successfully saved.';
        this.toastr.errorToastr('', message, {
            position: 'bottom-full-width',
            animate: 'slideFromBottom'
        });
    }
}
