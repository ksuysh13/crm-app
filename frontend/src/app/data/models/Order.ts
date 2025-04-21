export interface Order {
    orderId?: number;
    orderDate: string;
    totalAmount: number;
    isCompleted: boolean;
    clientId: number;
  }