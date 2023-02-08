package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.estore.api.estoreapi.persistence.UserAccountDAO;
import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.UserAccount;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Io;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Tag("Controller-tier")
public class UserControllerTest {
    private UserController userController;
    private UserAccountDAO mockUserAccountDAO;

    @BeforeEach
    public void setupUserController(){
        mockUserAccountDAO = mock(UserAccountDAO.class);
        userController = new UserController(mockUserAccountDAO);
    }

    @Test
    public void testCreateUserAccount() throws IOException {  // createHero may throw IOException
        // Setup
        UserAccount userAccount = new UserAccount(99, "baller22", "secure123",false, null, null);
        // when createHero is called, return true simulating successful
        // creation and save
        when(mockUserAccountDAO.createUserAccount(userAccount)).thenReturn(userAccount);

        // Invoke
        ResponseEntity<UserAccount> response = userController.createUserAccount(userAccount);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(userAccount,response.getBody());
    }

    @Test
    public void testCreateUserAccountIOFail() throws IOException {
        UserAccount userAccount = new UserAccount(99, "baller22", "secure123",false, null, null);
        // When findHeroes is called with the search string, return the two
        /// heroes above
        //when(mockUserAccountDAO.searchUsername(searchString)).thenReturn(userAccount);
        doThrow(new IOException()).when(mockUserAccountDAO).createUserAccount(userAccount);

        // Invoke
        ResponseEntity<UserAccount> response = userController.createUserAccount(userAccount);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test 
    public void testCreateUserAccountConflict() throws IOException {
        String username = "baller22";

        UserAccount userAccount = new UserAccount(99, username, "secure123",false, null, null);
        when(mockUserAccountDAO.createUserAccount(userAccount)).thenReturn(userAccount);

        ResponseEntity<UserAccount> response = userController.createUserAccount(userAccount);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userAccount,response.getBody());

        UserAccount userAccount2 = new UserAccount(100, username, "secure1234",false, null, null);
        when(mockUserAccountDAO.createUserAccount(userAccount2)).thenReturn(userAccount2);
        when(mockUserAccountDAO.searchUsername(username)).thenReturn(userAccount2);
        ResponseEntity<UserAccount> conflictResponse = userController.createUserAccount(userAccount2);
        assertEquals(HttpStatus.CONFLICT, conflictResponse.getStatusCode());

    }

    @Test
    public void testSearchUsername() throws IOException { // findHeroes may throw IOException
        // Setup
        String searchString = "homerun11";
        UserAccount userAccount = new UserAccount(99, "baller22", "secure123",false, null, null);
        // When findHeroes is called with the search string, return the two
        /// heroes above
        when(mockUserAccountDAO.searchUsername(searchString)).thenReturn(userAccount);

        // Invoke
        ResponseEntity<UserAccount> response = userController.searchUsername(searchString);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(userAccount,response.getBody());
    }

    @Test
    public void testSearchUsernameFail() throws IOException {
        String username = "homerun11";
        UserAccount userAccount = new UserAccount(99, username, "secure123",false, null, null);
        // When findHeroes is called with the search string, return the two
        /// heroes above
        //when(mockUserAccountDAO.searchUsername(searchString)).thenReturn(userAccount);
        doThrow(new IOException()).when(mockUserAccountDAO).searchUsername(username);

        // Invoke
        ResponseEntity<UserAccount> response = userController.searchUsername(username);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testSearchPassword() throws IOException { // findHeroes may throw IOException
        // Setup
        String searchString = "secure123";
        UserAccount userAccount = new UserAccount(99, "baller22", searchString,false, null, null);
        // When findHeroes is called with the search string, return the two
        /// heroes above
        when(mockUserAccountDAO.searchPassword(searchString)).thenReturn(userAccount);

        // Invoke
        ResponseEntity<UserAccount> response = userController.searchPassword(searchString);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(userAccount,response.getBody());
    }

    @Test
    public void testSearchPasswordFail() throws IOException {
        String password = "homerun11";
        UserAccount userAccount = new UserAccount(99, "baller22", password,false, null, null);
        // When findHeroes is called with the search string, return the two
        /// heroes above
        //when(mockUserAccountDAO.searchUsername(searchString)).thenReturn(userAccount);
        doThrow(new IOException()).when(mockUserAccountDAO).searchPassword(password);

        // Invoke
        ResponseEntity<UserAccount> response = userController.searchPassword(password);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetCartForUser() throws IOException { // findHeroes may throw IOException
        // Setup
        Product cart[] = new Product[1];
        Product product = new Product(18, "song66", 20, 5, "image", "video", 0, 0);
        cart[0] = product;
        String username = "baller22";
        UserAccount userAccount = new UserAccount(99, username, "secure123",false, cart, null);
        // When findHeroes is called with the search string, return the two
        /// heroes above
        when(mockUserAccountDAO.searchUsername(username)).thenReturn(userAccount);

        // Invoke
        ResponseEntity<Product[]> response = userController.getCartForUser(username);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(userAccount.getShoppingCart(),response.getBody());
    }

    @Test
    public void testGetCartForUserFail() throws IOException {
        Product cart[] = new Product[1];
        Product product = new Product(18, "song66", 20, 5, "image", "video", 0, 0);
        cart[0] = product;
        String username = "baller22";
        UserAccount userAccount = new UserAccount(99, username, "secure123",false, cart, null);
        doThrow(new IOException()).when(mockUserAccountDAO).searchUsername(username);
        ResponseEntity<Product[]> response = userController.getCartForUser(username);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdateCartForUser() throws IOException {
        Product cart[] = new Product[1];
        Product product = new Product(18, "song66", 20, 5, "image", "video", 0, 0);
        cart[0] = product;
        
        UserAccount userAccount = new UserAccount(99, "baller22", "secure123",false, null, null);

        when(mockUserAccountDAO.createUserAccount(userAccount)).thenReturn(userAccount);
        when(mockUserAccountDAO.updateUser(userAccount)).thenReturn(userAccount);

        userAccount.setShoppingCart(cart);

        ResponseEntity<UserAccount> response = userController.updateCart(userAccount);
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testUpdateCartFail() throws IOException {
        Product cart[] = new Product[1];
        Product product = new Product(18, "song66", 20, 5, "image", "video", 0, 0);
        cart[0] = product;
        
        UserAccount userAccount = new UserAccount(99, "baller22", "secure123",false, null, null);

        when(mockUserAccountDAO.createUserAccount(userAccount)).thenReturn(userAccount);
        doThrow(new IOException()).when(mockUserAccountDAO).updateUser(userAccount);

        userAccount.setShoppingCart(cart);

        ResponseEntity<UserAccount> response = userController.updateCart(userAccount);
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }


    @Test
    public void testGetWishlistforUser() throws IOException { // findHeroes may throw IOException
        // Setup
        Product wishlist[] = new Product[1];
        Product product = new Product(18, "song66", 20, 5, "image", "video", 0, 0);
        wishlist[0] = product;
        String username = "baller22";
        UserAccount userAccount = new UserAccount(99, username, "secure123",false, null, wishlist);
        // When findHeroes is called with the search string, return the two
        /// heroes above
        when(mockUserAccountDAO.searchUsername(username)).thenReturn(userAccount);

        // Invoke
        ResponseEntity<Product[]> response = userController.getWishlistForUser(username);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(userAccount.getWishlist(),response.getBody());
    }

    @Test
    public void testGetWishlistforUserFail() throws IOException {
        Product wishlist[] = new Product[1];
        Product product = new Product(18, "song66", 20, 5, "image", "video", 0, 0);
        wishlist[0] = product;
        String username = "baller22";
        UserAccount userAccount = new UserAccount(99, username, "secure123",false, null, wishlist);
        doThrow(new IOException()).when(mockUserAccountDAO).searchUsername(username);
        ResponseEntity<Product[]> response = userController.getWishlistForUser(username);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    
}
