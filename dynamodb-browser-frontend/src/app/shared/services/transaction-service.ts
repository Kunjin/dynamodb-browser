import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { RECORDS_URL, TABLES_URL, OPERATIONS_URL, TABLE_DETAILS_URL, DATA_URL } from '../constants';

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

    getOperations(): Observable<any> {
        return this.http.get(OPERATIONS_URL);
    }

    getTableDetails(table: string): Observable<any> {
        return this.http.get(`${TABLE_DETAILS_URL}/${table}`);
    }

    //TODO: refactor this, maybe get the hashKey or rangeKey in the backend and only accept key's value
    getRecordByHashKey(table:string, hashKey: string, operator: string, hashValue: string): Observable<any> {
        return this.http.get(`${DATA_URL}/${table}?hashKey=${hashKey}&operator=${operator}&hashValue=${hashValue}`);
    }

    //TODO: refactor this, maybe get the hashKey or rangeKey in the backend and only accept key's value
    getRecordByHashKeyRangeKey(table:string,
                               hashKey: string,
                               hashKeyOperator: string,
                               hashValue: string,
                               rangeKey: string,
                               rangeKeyOperator: string,
                               rangeValue: string): Observable<any> {
        return this.http.get(`${DATA_URL}/${table}?hashKey=${hashKey}&operator=${hashKeyOperator}&hashValue=${hashValue}&rangeKey=${rangeKey}&operatorRangeKey=${rangeKeyOperator}&rangeValue=${rangeValue}`);
    }

}
