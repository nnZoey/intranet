import { Component, OnInit } from '@angular/core';
import { NbSidebarService } from '@nebular/theme';
import { NbSearchService } from '@nebular/theme';
import { NbMenuService } from '@nebular/theme';
import { takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { trigger, state, style, transition, animate } from '@angular/animations';


import { NbMenuItem } from '@nebular/theme';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
})
export class SidebarComponent implements OnInit {
  value = '';

  items: NbMenuItem[] = [
    {
      title: 'Profile',
      expanded: true,
      badge: {
        text: '30',
        status: 'primary',
      },
      children: [
        {
          title: 'Messages',
          badge: {
            text: '99+',
            status: 'danger',
          },
        },
        {
          title: 'Notifications',
          badge: {
            dotMode: true,
            status: 'warning',
          },
        },
        {
          title: 'Emails',
          badge: {
            text: 'new',
            status: 'success',
          },
        },
      ],
    },
  ];
  showFiller = false;
  constructor(
    private sidebarService: NbSidebarService,
    private searchService: NbSearchService
  ) {
    this.searchService.onSearchSubmit()
    .subscribe((data: any) => {
      this.value = data.term;
    })
   }

  ngOnInit(): void {
  }

  toggle() {
    this.sidebarService.toggle(false, 'left');
    return true;
  }
}
