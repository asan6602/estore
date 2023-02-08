import { Product } from "./product";

export interface User {
    id: number;
    username: string;
    password: string;
    isAdmin: boolean;
    shoppingCart: Array<Product>;
    wishlist: Array<Product>;
}