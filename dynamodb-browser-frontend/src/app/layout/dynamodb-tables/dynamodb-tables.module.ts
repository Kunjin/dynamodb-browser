import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCheckboxModule, MatFormFieldModule, MatInputModule, MatSelectModule} from '@angular/material';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DynamodbTablesComponent } from './dynamodb-tables.component';
import { DynamodbTablesRoutingModule } from './dynamodb-tables-routing.module';

@NgModule({
    imports: [
        CommonModule,
        DynamodbTablesRoutingModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatCheckboxModule,
        ReactiveFormsModule,
        FormsModule ],
    declarations: [ DynamodbTablesComponent ]
})
export class DynamodbTablesModule {}
