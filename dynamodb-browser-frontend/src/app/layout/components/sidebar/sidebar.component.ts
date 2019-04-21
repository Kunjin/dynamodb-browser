import { Component, Output, EventEmitter, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { TransactionService } from '../../../shared/services/transaction-service';
import { ToastrManager } from 'ng6-toastr-notifications';
import _ from 'lodash';

@Component({
    selector: 'app-sidebar',
    templateUrl: './sidebar.component.html',
    styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
    isActive: boolean;
    collapsed: boolean;
    showMenu: string;
    pushRightClass: string;
    tables: string[] = [];

    @Output() collapsedEvent = new EventEmitter<boolean>();

    constructor(public router: Router,
                private transactionService: TransactionService,
                public toastr: ToastrManager) {
    }

    ngOnInit() {
        this.isActive = false;
        this.collapsed = false;
        this.showMenu = '';
        this.pushRightClass = 'push-right';

        this.transactionService.getTablesCount().subscribe(
            count => {
                console.log('table count: ', count);
                this.displayTables();
            });

        // initialization
        this.displayTables();

    }


    private displayTables() {
        this.transactionService.displayTables().subscribe(
            tables => {
                this.tables = tables;
                console.log('tables: ', this.tables);
                this.showMenu = 'pages';
            }, err => {
                this.showFailedToast(_.get(err, 'error.message'));
            });
    }

    private showFailedToast(message: string) {
        this.toastr.errorToastr('', message, {
            position: 'bottom-full-width',
            animate: 'slideFromBottom'
        });
    }

}
