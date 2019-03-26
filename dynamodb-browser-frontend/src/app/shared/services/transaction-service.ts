import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { TABLES_URL } from '../constants';

@Injectable()
export class TransactionService {
    constructor(private http: HttpClient) {
    }

    displayTables() : Observable<any> {
        return this.http.get(TABLES_URL);
    }

}
