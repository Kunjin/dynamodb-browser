import { Component, OnInit } from '@angular/core';
import { routerTransition } from '../../router.animations';
import { AwsService } from '../../shared/services';
import { FormGroup, FormControl, FormBuilder } from '@angular/forms';


@Component({
    selector: 'settings-selector',
    templateUrl: './settings.component.html',
    styleUrls: ['./settings.component.scss'],
    animations: [routerTransition()]
})
export class SettingsComponent implements OnInit {

    settingsForm: FormGroup;
    regions: String[] = [];
    profiles: String[] = [];
    isChecked = false;

    constructor(private awsService: AwsService, private fb: FormBuilder) {
        this.settingsForm = fb.group({
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

        this.settingsForm.get('awsRegion').setValue(this.regions);
        this.settingsForm.get('awsProfile').setValue(this.profiles);

        this.loadSettings();

        this.settingsForm.get('isAwsProfileUsed').valueChanges.subscribe(checked => {
            if (checked) {
                this.isChecked = true;
                this.settingsForm.get('awsProfile').enable();
                this.enableDisableAccessSecretKey(false);
            } else {
                this.isChecked = false;
                this.settingsForm.get('awsProfile').disable();
                this.enableDisableAccessSecretKey(true);
            }
        })
    }

    loadSettings(): void {
        this.awsService.loadSettings().subscribe(data => {
            this.settingsForm.get('awsRegion').setValue(data.region);
            this.settingsForm.get('awsProfile').setValue(data.profile);
            this.settingsForm.get('isAwsProfileUsed').setValue(data.isAwsProfileUsed);
            this.settingsForm.get('accessKey').setValue(data.accessKey);
            this.settingsForm.get('secretKey').setValue(data.secretKey);
        })
    }

    enableDisableAccessSecretKey(toggle): void {
        if (toggle) {
            this.settingsForm.get('accessKey').enable();
            this.settingsForm.get('secretKey').enable();
        } else {
            this.settingsForm.get('accessKey').disable();
            this.settingsForm.get('secretKey').disable();
        }
    }

    onSubmit(): void {
        this.awsService.saveSettings(
            this.settingsForm.get('awsRegion').value,
            this.settingsForm.get('awsProfile').value,
            //this.settingsForm.get('isAwsProfileUsed').value,
            this.isChecked,
            this.settingsForm.get('accessKey').value,
            this.settingsForm.get('secretKey').value)
            .subscribe(data => {
                console.log(data);
        })
    }
}
