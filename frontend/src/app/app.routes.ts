import { Routes } from '@angular/router';
import { ClientListComponent } from './client-list/client-list.component';
import { EditClientComponent } from './edit-client/edit-client.component';

export const routes: Routes = [
    { path: '', redirectTo: '/clients', pathMatch: 'full' },
    {path: 'clients', component: ClientListComponent},
    { path: 'clients/new', component: EditClientComponent },
    { path: 'clients/:clientId', component: EditClientComponent },
];
