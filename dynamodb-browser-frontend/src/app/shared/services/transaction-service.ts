import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { RECORDS_URL, TABLES_URL } from '../constants';

@Injectable()
export class TransactionService {
    constructor(private http: HttpClient) {
    }

    displayTables() : Observable<any> {
        return this.http.get(TABLES_URL);
    }

    getRecordsByTables(table: string): Observable<any> {
        return this.http.get(`${RECORDS_URL}/${table}`);
    }

}
