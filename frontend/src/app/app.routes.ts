import { Routes } from '@angular/router';
import { ClientListComponent } from './client-list/client-list.component';
import { EditClientComponent } from './edit-client/edit-client.component';
import { DiscountComponent } from './discount/discount.component';
import { EditDiscountComponent } from './edit-discount/edit-discount.component';

export const routes: Routes = [
    { path: '', redirectTo: '/clients', pathMatch: 'full' },
    {path: 'clients', component: ClientListComponent},
    { path: 'clients/new', component: EditClientComponent },
    { path: 'clients/:clientId', component: EditClientComponent },
    {path: 'discounts', component: DiscountComponent},
    {path: 'discounts/new', component: EditDiscountComponent},
    {path: 'discounts/:discountId', component: EditDiscountComponent}
];