import { MatFormFieldModule, MatInputModule, MatSelectModule, MatCheckboxModule, MatTableModule } from '@angular/material';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';

@NgModule({
exports: [
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    MatCheckboxModule,
    MatTableModule,
    ReactiveFormsModule,
    FormsModule
    ]
})

export class DynamodbBrowserModule {}
