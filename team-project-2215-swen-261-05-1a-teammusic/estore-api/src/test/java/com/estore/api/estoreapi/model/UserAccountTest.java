package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class UserAccountTest{

    @Test
    public void testGetId(){
        UserAccount userAccount = new UserAccount(2, "Joe123", "badpassword", false, null, null);
        assertEquals(userAccount.getId(), 2);
    }

    @Test
    public void testGetUsername(){
        UserAccount userAccount = new UserAccount(2, "Joe123", "badpassword", false, null, null);
        assertEquals(userAccount.getUsername(), "Joe123");
    }

    @Test
    public void testSetUsername(){
        UserAccount userAccount = new UserAccount(2, "Joe123", "badpassword", false, null, null);
        userAccount.setUsername("NEWUSERNAME");
        assertEquals(userAccount.getUsername(), "NEWUSERNAME");
    }

    @Test
    public void TestGetPassword(){
        UserAccount userAccount = new UserAccount(2, "Joe123", "badpassword", false, null, null);
        assertEquals(userAccount.getPassword(), "badpassword");
    }

    @Test
    public void TestGetIsAdmin(){
        UserAccount userAccount = new UserAccount(2, "Joe123", "badpassword", false, null, null);
        assertEquals(userAccount.getIsAdmin(), false);
    }

    @Test
    public void TestToString() {
        UserAccount userAccount = new UserAccount(2, "Joe123", "badpassword", false, null, null);
        String expectedString = "UserAccount [id=2, username=Joe123, password=badpassword, isAdmin=false, shoppingCart=null, wishlist=null]";


        assertEquals(expectedString, userAccount.toString());
    }

    @Test
    public void TestsetShoppingCart() {
        
        UserAccount userAccount = new UserAccount(2, "Joe123", "badpassword", false, null, null);
        Product testproduct = new Product(582, "newtest product", 0, 10, "testimage", "testvideo", 0, 0);
        Product[] testArray = {testproduct};
        userAccount.setShoppingCart(testArray);

        assertEquals(testArray,userAccount.getShoppingCart());
    }

    @Test
    public void TestsetWishlist() {
        
        UserAccount userAccount = new UserAccount(2, "Joe123", "badpassword", false, null, null);
        Product testproduct = new Product(582, "newtest product", 0, 10, "testimage", "testvideo", 0, 0);
        Product[] testArray = {testproduct};
        userAccount.setWishlist(testArray);

        assertEquals(testArray,userAccount.getWishlist());
    }
}