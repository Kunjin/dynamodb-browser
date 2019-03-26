import { Component, OnInit } from '@angular/core';
import { routerTransition } from '../../router.animations';
import { TransactionService } from '../../shared/services';
import { ActivatedRoute } from '@angular/router';
import { MatTableDataSource } from '@angular/material';


@Component({
    selector: 'dynamodb-tables-selector',
    templateUrl: './dynamodb-tables.component.html',
    styleUrls: ['./dynamodb-tables.component.scss'],
    animations: [routerTransition()]
})
export class DynamodbTablesComponent implements OnInit {

    columnDefs = [];
    dataSource: MatTableDataSource<any>;

    constructor(private transactionsService: TransactionService,
                private activatedRoute: ActivatedRoute,) {
    }

    ngOnInit(): void {
        this.activatedRoute.params.subscribe(params => {
            const tableParam = params['table'];
            this.transactionsService.getRecordsByTables(tableParam).subscribe(records => {
                for (let column in JSON.parse(records[0])) {
                    this.columnDefs.push(column);
                }
                this.dataSource = records;
                console.log(records);
                let recordsArr = [];
                for (let i=0; i < records.length; i++) {

                    // recordsArr.push(records[i]);
                    recordsArr.push({ mic_code: 'FRAB', record: 'AAA', modified_date:'2019-01-01', isin: 'AU1'})
                }
                this.initializeDataTable(recordsArr);
            })
        });
    }

    private initializeDataTable(arr: any[]) {
        this.dataSource = new MatTableDataSource(arr);
        console.log(this.dataSource);
    }
}
