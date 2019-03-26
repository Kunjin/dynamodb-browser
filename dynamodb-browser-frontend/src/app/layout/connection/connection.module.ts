import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConnectionComponent } from './connection.component';
import { MatCheckboxModule, MatFormFieldModule, MatInputModule, MatSelectModule} from '@angular/material';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ConnectionRoutingModule } from './connection-routing.module';

@NgModule({
    imports: [
        CommonModule,
        ConnectionRoutingModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatCheckboxModule,
        ReactiveFormsModule,
        FormsModule ],
    declarations: [ ConnectionComponent ]
})
export class ConnectionModule {}
