import { Component, OnInit } from '@angular/core';
import {NbMenuItem} from '@nebular/theme';
import { trigger, state, style, transition, animate } from '@angular/animations';
import { MenuListItemService } from './menu-list-item.service';
import { NavItem } from './Nav-item';
@Component({
  selector: 'app-menu-list-item',
  templateUrl: './menu-list-item.component.html',
  styleUrls: ['./menu-list-item.component.scss'],
  animations: [
    trigger('slide', [
      state('up', style({ height: 0 })),
      state('down', style({ height: '*' })),
      transition('up <=> down', animate(200))
    ]),
    trigger('smoothCollapse', [
      state('initial', style({
        height:'0',
        overflow:'hidden',
        opacity:'0'
      })),
      state('final', style({
        overflow:'hidden',
        opacity:'1'
      })),
      transition('initial=>final', animate('750ms')),
      transition('final=>initial', animate('750ms'))
    ])
  ]
})
export class MenuListItemComponent implements OnInit {
  selected = true;
  isNavbarCollapse = true;
  openAPIEnabled ?: boolean;
  entitiesSidebarItems: any[]  = [];
  showFiller = false;
  click = false;
  isMenuCollapsed = false;
  items: NbMenuItem[] = [
    {
      title: 'Profile',
      expanded: true,
      children: [
        {
          title: 'Change Password',
        },
        {
          title: 'Privacy Policy',
        },
        {
          title: 'Logout',
        },
      ],
    },
    {
      title: 'Shopping Bag',
    },
    {
      title: 'Orders',
    },
  ];

  constructor(public sidebarservice: MenuListItemService) {
  
  }

  ngOnInit() {
  }

  collapseNavbar(): void {
    this.isNavbarCollapse = true;
  }

  getState(currentMenu: any) {
    if (currentMenu.active) {
      return 'down';
    } else {
      return 'up';
    }
  }

  toggle(currentMenu: any) {
    if (currentMenu.type === 'dropdown') {

    }
  }
}

