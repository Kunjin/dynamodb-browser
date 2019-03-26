import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DynamodbTablesComponent } from './dynamodb-tables.component';

const routes: Routes = [
    {
        path: '', component: DynamodbTablesComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class DynamodbTablesRoutingModule {
}
