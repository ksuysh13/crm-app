import { Routes } from '@angular/router';
import { ClientListComponent } from './client-list/client-list.component';
import { EditClientComponent } from './edit-client/edit-client.component';
import { DiscountComponent } from './discount/discount.component';
import { EditDiscountComponent } from './edit-discount/edit-discount.component';
import { ManufacturerListComponent } from './manufacturer-list/manufacturer-list.component';
import { EditManufacturerComponent } from './edit-manufacturer/edit-manufacturer.component';

export const routes: Routes = [
    { path: '', redirectTo: '/clients', pathMatch: 'full' },
    {path: 'clients', component: ClientListComponent},
    { path: 'clients/new', component: EditClientComponent },
    { path: 'clients/:clientId', component: EditClientComponent },
    {path: 'discounts', component: DiscountComponent},
    {path: 'discounts/new', component: EditDiscountComponent},
    {path: 'discounts/:discountId', component: EditDiscountComponent},
    {path: 'manufacturers', component: ManufacturerListComponent},
    { path: 'manufacturers/new', component: EditManufacturerComponent },
    { path: 'manufacturers/:manufacturerId', component: EditManufacturerComponent }
];