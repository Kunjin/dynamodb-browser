import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AWS_PROFILE_URL, AWS_REGIONS_URL, SETTINGS_URL } from '../constants';


@Injectable()
export class AwsService {
    constructor(private http: HttpClient) {
    }

    getAwsRegions() : Observable<any> {
        return this.http.get(AWS_REGIONS_URL);
    }

    getAwsProfile() : Observable<any> {
        return this.http.get(AWS_PROFILE_URL);
    }

    //TODO: refactor this put the parameters in object
    saveSettings(region: string, profile: string, isAwsProfileUsed: boolean, accessKey: string, secretKey: string): Observable<any> {
        const headers = new HttpHeaders({'Content-Type':'application/json; charset=utf-8'});
        return this.http.post<any>(`${SETTINGS_URL}`, {
            'region': region,
            'profile': profile,
            'isAwsProfileUsed': isAwsProfileUsed,
            'accessKey': accessKey,
            'secretKey': secretKey
        },  { headers: headers })
    }

    loadSettings() : Observable<any> {
        return this.http.get(SETTINGS_URL);
    }
}
