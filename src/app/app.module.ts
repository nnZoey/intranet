import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { 
  NbThemeModule, 
  NbLayoutModule, 
  NbSidebarModule, 
  NbCardModule, 
  NbButtonModule, 
  NbMenuModule, 
  NbSearchModule, 
  NbInputModule,
  NbAccordionModule,
  NbTreeGridModule
} from '@nebular/theme';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { NbEvaIconsModule } from '@nebular/eva-icons';
import { NbIconModule } from '@nebular/theme';
import { SidebarComponent } from './sidebar/sidebar.component';
import { SidebarMenuModule } from 'angular-sidebar-menu';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu'
import { MatDialogModule } from '@angular/material/dialog';
import { MatExpansionModule } from '@angular/material/expansion';
import { MenuListItemComponent } from './menu-list-item/menu-list-item.component';

import { PerfectScrollbarModule } from 'ngx-perfect-scrollbar';
import { MaterialExampleModule } from './material.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { TableEmployeeComponent } from './table-employee/table-employee.component';
import { TableAssignmentComponent } from './table-assignment/table-assignment.component';
import { TableProjectComponent } from './table-project/table-project.component';
import { TableJobComponent } from './table-job/table-job.component';
import { TableJobHistoryComponent } from './table-job-history/table-job-history.component';
import { TableBankAccountComponent } from './table-bank-account/table-bank-account.component';
import { TableBankPersonComponent } from './table-bank-person/table-bank-person.component';


@NgModule({
  declarations: [
    AppComponent,
    SidebarComponent,
    MenuListItemComponent,
    TableEmployeeComponent,
    TableAssignmentComponent,
    TableProjectComponent,
    TableJobComponent,
    TableJobHistoryComponent,
    TableBankAccountComponent,
    TableBankPersonComponent
  ],
  imports: [
    NgbModule,
    BrowserModule,
    AppRoutingModule,

    CommonModule,
    BrowserAnimationsModule,
    NoopAnimationsModule,
    NbThemeModule.forRoot({ name: 'corporate' }),
    NbLayoutModule,
    NbEvaIconsModule,
    NbSidebarModule.forRoot(),
    NbIconModule,
    NbCardModule,
    NbButtonModule,
    NbMenuModule.forRoot(),
    NbSearchModule,
    NbInputModule,
    NbTreeGridModule,
    SidebarMenuModule,
    MatDialogModule,
    MatMenuModule,
    MatListModule,
    MatIconModule,
    MatSidenavModule,
    MatToolbarModule,
    MatButtonModule,
    PerfectScrollbarModule,
    MaterialExampleModule,
    NbAccordionModule,
    MatExpansionModule
  ],
  providers: [
    
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
