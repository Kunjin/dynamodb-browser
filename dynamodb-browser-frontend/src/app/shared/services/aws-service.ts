import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient} from '@angular/common/http';
import {AWS_PROFILE, AWS_REGIONS} from '../constants';


@Injectable()
export class AwsService {
    constructor(private http: HttpClient) {
    }

    getAwsRegions() : Observable<any>{
        return this.http.get(AWS_REGIONS);
    }

    getAwsProfile() : Observable<any> {
        return this.http.get(AWS_PROFILE);
    }
}
