import { NbMenuBadgeConfig } from "@nebular/theme/components/menu/menu.service";

export interface NavItem {
    displayName : string;
    disabled?: boolean;
    iconName: string;
    route?: string;
    children?: NavItem[];
}