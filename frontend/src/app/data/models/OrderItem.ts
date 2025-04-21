import { Discount } from "./Discount";
import { Product } from "./Product";

export interface OrderItem {
    orderItemId?: number;
    quantity: number;
    price: number;
    orderId: number;
    productId?: number; // Для создания/обновления
    product?: Product | { productId: number, productName: string, price: number }; // Для чтения
    discountId?: number | null; // Для создания/обновления
    discount?: Discount | { discountId: number, discountPercentage: number }; // Для чтения
}