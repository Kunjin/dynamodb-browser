import {
    MatButtonModule,
    MatCheckboxModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatProgressBarModule,
    MatSelectModule,
    MatTableModule,
    MatTooltipModule
} from '@angular/material';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';

@NgModule({
exports: [
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    MatCheckboxModule,
    MatTableModule,
    MatTooltipModule,
    MatProgressBarModule,
    MatButtonModule,
    MatDialogModule,
    ReactiveFormsModule,
    FormsModule
    ]
})

export class DynamodbBrowserModule {}
