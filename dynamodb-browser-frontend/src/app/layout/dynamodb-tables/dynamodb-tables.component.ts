import { Component, OnInit } from '@angular/core';
import { routerTransition } from '../../router.animations';
import { TransactionService } from '../../shared/services';
import { ActivatedRoute } from '@angular/router';


@Component({
    selector: 'dynamodb-tables-selector',
    templateUrl: './dynamodb-tables.component.html',
    styleUrls: ['./dynamodb-tables.component.scss'],
    animations: [routerTransition()]
})
export class DynamodbTablesComponent implements OnInit {

    constructor(private transactionsService: TransactionService,
                private activatedRoute: ActivatedRoute,) {
    }

    ngOnInit(): void {
        this.activatedRoute.params.subscribe(params => {
            const tableParam = params['table'];
            this.transactionsService.getRecordsByTables(tableParam).subscribe(records => {
                console.log(records);
            })
        });
    }

}
