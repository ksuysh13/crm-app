import { Routes } from '@angular/router';
import { ClientListComponent } from './client-list/client-list.component';
import { EditClientComponent } from './edit-client/edit-client.component';
import { DiscountComponent } from './discount/discount.component';
import { EditDiscountComponent } from './edit-discount/edit-discount.component';
import { ManufacturerListComponent } from './manufacturer-list/manufacturer-list.component';
import { EditManufacturerComponent } from './edit-manufacturer/edit-manufacturer.component';
import { ProductGroupListComponent } from './product-group-list/product-group-list.component';
import { EditProductGroupComponent } from './edit-product-group/edit-product-group.component';
import { ProductListComponent } from './product-list/product-list.component';
import { EditProductComponent } from './edit-product/edit-product.component';
import { OrderListComponent } from './order-list/order-list.component';
import { EditOrderComponent } from './edit-order/edit-order.component';
import { OrderItemListComponent } from './order-item-list/order-item-list.component';
import { EditOrderItemComponent } from './edit-order-item/edit-order-item.component';

export const routes: Routes = [
    { path: '', redirectTo: '/clients', pathMatch: 'full' },
    {path: 'clients', component: ClientListComponent},
    { path: 'clients/new', component: EditClientComponent },
    { path: 'clients/:clientId', component: EditClientComponent },
    { path: 'clients/:clientId/orders', component: OrderListComponent },
    { path: 'clients/:clientId/orders/new', component: EditOrderComponent },
    { path: 'clients/:clientId/orders/:orderId', component: EditOrderComponent },
    { path: 'clients/:clientId/orders/:orderId/order-items', component: OrderItemListComponent },
    { path: 'clients/:clientId/orders/:orderId/order-items/new', component: EditOrderItemComponent },
    { path: 'clients/:clientId/orders/:orderId/order-items/:orderItemId', component: EditOrderItemComponent },
    {path: 'discounts', component: DiscountComponent},
    {path: 'discounts/new', component: EditDiscountComponent},
    {path: 'discounts/:discountId', component: EditDiscountComponent},
    {path: 'manufacturers', component: ManufacturerListComponent},
    { path: 'manufacturers/new', component: EditManufacturerComponent },
    { path: 'manufacturers/:manufacturerId', component: EditManufacturerComponent },
    { path: 'product-groups', component: ProductGroupListComponent },
    { path: 'product-groups/new', component: EditProductGroupComponent },
    { path: 'product-groups/:groupId', component: EditProductGroupComponent },
    { path: 'product-groups/:groupId/products', component: ProductListComponent },
    { path: 'product-groups/:groupId/products/new', component: EditProductComponent },
    { path: 'product-groups/:groupId/products/:productId', component: EditProductComponent }
];