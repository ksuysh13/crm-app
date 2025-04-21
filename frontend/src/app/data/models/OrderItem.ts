import { Discount } from './Discount';
import { Product } from './Product';

export interface OrderItem {
    orderItemId?: number;
    quantity: number;
    price: number;
    orderId: number;
    product?: Product;
    discount?: Discount;
}