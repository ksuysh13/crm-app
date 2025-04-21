import { Discount } from './Discount';
import { Product } from './Product';

export interface OrderItem {
    orderItemId?: number;
    quantity: number;
    price: number;
    orderId: number;
    product?: Product | { productId: number, productName: string }; 
    discount?: Discount | { discountId: number, discountPercentage: number }; 
}