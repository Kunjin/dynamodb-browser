import {Component, OnInit} from '@angular/core';
import {routerTransition} from "../../router.animations";
import { AwsService } from "../../shared/services";

@Component({
    selector: 'settings-selector',
    templateUrl: './settings.component.html',
    styleUrls: ['./settings.component.scss'],
    animations: [routerTransition()]
})
export class SettingsComponent implements OnInit {

    public regions: String[] = [];
    public profiles: String[] = [];

    constructor(private awsService: AwsService) {
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

    }

}
