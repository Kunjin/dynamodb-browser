import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { RECORDS_URL, TABLES_URL, OPERATIONS_URL, TABLE_DETAILS_URL, DATA_URL } from '../constants';
import _ from 'lodash';

@Injectable()
export class TransactionService {
    private subject = new Subject<any>();

    constructor(private http: HttpClient) {
    }

    displayTables() : Observable<any> {
        return this.http.get(TABLES_URL);
    }

    getTablesCount() : Observable<any> {
        return this.subject.asObservable();
    }

    setTablesCount(data: any) {
        this.subject.next({ data: data });
        console.log('subject: n ', this.subject);
    }

    scanTable(table: string, exclusiveKeys ?: object): Observable<any> {
        let hashKeyName = _.get(exclusiveKeys, 'hashKeyName');
        let hashKeyValue = _.get(exclusiveKeys, 'hashKeyValue');
        let rangeKeyName = _.get(exclusiveKeys, 'rangeKeyName');
        let rangeKeyValue = _.get(exclusiveKeys, 'rangeKeyValue');

        if (exclusiveKeys !== undefined) {
            return this.http.get(`${RECORDS_URL}/${table}?hashKeyName=${hashKeyName}&hashKeyValue=${hashKeyValue}&rangeKeyName=${rangeKeyName}&rangeKeyValue=${rangeKeyValue}`);
        }
        return this.http.get(`${RECORDS_URL}/${table}`);

    }

    getOperations(): Observable<any> {
        return this.http.get(OPERATIONS_URL);
    }

    getTableDetails(table: string): Observable<any> {
        return this.http.get(`${TABLE_DETAILS_URL}/${table}`);
    }

    //TODO: refactor this, maybe get the hashKey or rangeKey in the backend and only accept key's value
    queryByHashKey(table:string, hashKey: string, operator: string, hashValue: string): Observable<any> {
        return this.http.get(`${DATA_URL}/${table}?hashKey=${hashKey}&operator=${operator}&hashValue=${hashValue}`);
    }

    //TODO: refactor this, maybe get the hashKey or rangeKey in the backend and only accept key's value
    queryByHashKeyRangeKey(table:string,
                           hashKey: string,
                           hashKeyOperator: string,
                           hashValue: string,
                           rangeKey: string,
                           rangeKeyOperator: string,
                           rangeValue: string): Observable<any> {
        return this.http.get(`${DATA_URL}/${table}?hashKey=${hashKey}&operator=${hashKeyOperator}&hashValue=${hashValue}&rangeKey=${rangeKey}&operatorRangeKey=${rangeKeyOperator}&rangeValue=${rangeValue}`);
    }

}
