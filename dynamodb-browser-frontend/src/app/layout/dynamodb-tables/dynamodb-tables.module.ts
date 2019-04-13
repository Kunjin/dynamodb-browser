import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
    MatButtonModule,
    MatDialogModule,
    MatInputModule,
    MatProgressBarModule,
    MatSelectModule,
    MatTableModule,
    MatTooltipModule
} from '@angular/material';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DynamodbTablesComponent } from './dynamodb-tables.component';
import { DynamodbTablesRoutingModule } from './dynamodb-tables-routing.module';
import { AddRecordDialog } from './add-record/add-record.component';
import { DeleteRecordDialog } from './delete-record/delete-record.component';

@NgModule({
    imports: [
        CommonModule,
        DynamodbTablesRoutingModule,
        MatTableModule,
        MatTooltipModule,
        MatProgressBarModule,
        MatSelectModule,
        MatInputModule,
        MatButtonModule,
        MatDialogModule,
        ReactiveFormsModule,
        FormsModule ],
    declarations: [ DynamodbTablesComponent, AddRecordDialog, DeleteRecordDialog ],
    entryComponents: [ AddRecordDialog, DeleteRecordDialog ]
})
export class DynamodbTablesModule {}
