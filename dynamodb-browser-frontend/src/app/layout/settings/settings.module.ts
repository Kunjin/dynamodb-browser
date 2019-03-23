import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SettingsComponent } from './settings.component';
import { MatFormFieldModule, MatInputModule, MatSelectModule } from '@angular/material';
import { SettingsRoutingModule } from './settings-routing.module';

@NgModule({
    imports: [ CommonModule, SettingsRoutingModule,  MatFormFieldModule, MatInputModule, MatSelectModule ],
    declarations: [ SettingsComponent ]
})
export class SettingsModule {}
