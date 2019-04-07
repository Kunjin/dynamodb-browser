import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LayoutComponent } from './layout.component';

const routes: Routes = [
    {
        path: '',
        component: LayoutComponent,
        children: [
            { path: '', redirectTo: 'connection', pathMatch: 'prefix' },
            { path: 'dynamodb-tables', loadChildren: './dynamodb-tables/dynamodb-tables.module#DynamodbTablesModule' },
            { path: 'connection', loadChildren: './connection/connection.module#ConnectionModule'},
            { path: 'dynamodb-tables/:table', loadChildren: './dynamodb-tables/dynamodb-tables.module#DynamodbTablesModule' }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class LayoutRoutingModule {}
