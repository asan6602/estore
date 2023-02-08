package com.estore.api.estoreapi.controller;

import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.estore.api.estoreapi.persistence.UserAccountDAO;
import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.UserAccount;

/**
 * Handles the REST API requests for the UserAccount resource
 * 
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author aaron santos
 */
@RestController
@RequestMapping("UserAccounts")
public class UserController {
    private static final Logger LOG = Logger.getLogger(UserController.class.getName());
    private UserAccountDAO UserAccountDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param UserAccountDao The {@link UserAccountDAO UserAccount Data Access Object} to perform CRUD operations
     * 
     * This dependency is injected by the Spring Framework
     */
    public UserController(UserAccountDAO userAccountDao) {
        this.UserAccountDao = userAccountDao;
    }

    /**
     * Creates a {@linkplain UserAccount UserAccount} with the provided UserAccount object
     * 
     * @param UserAccount - The {@link UserAccount UserAccount} to create
     * 
     * @return ResponseEntity with created {@link UserAccount UserAccount} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link UserAccount UserAccount} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<UserAccount> createUserAccount(@RequestBody UserAccount UserAccount) {
        LOG.info("POST /UserAccount " + UserAccount);
        try {
            if(UserAccountDao.searchUsername(UserAccount.getUsername()) == null) {
                UserAccount newProduct = UserAccountDao.createUserAccount(UserAccount);
                return new ResponseEntity<UserAccount>(newProduct,HttpStatus.CREATED);
            }
            else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for the UserAccount {@linkplain UserAccount UserAccount} whose name is
     * the text in username
     * 
     * @param username is text used to find the {@link UserAccount UserAccount}
     * 
     * @return ResponseEntity with {@link UserAccount UserAccount} object and
     * HTTP status of OK
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/")
    public ResponseEntity<UserAccount> searchUsername(@RequestParam String username) {
        LOG.info("GET /UserAccounts/?username="+username);

        try {
            UserAccount UserAccount = UserAccountDao.searchUsername(username);
            return new ResponseEntity<UserAccount>(UserAccount,HttpStatus.OK);
        }
        catch (IOException e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Assumes User is valid
    @GetMapping("cart")
    public ResponseEntity<Product[]> getCartForUser(@RequestParam String username) {
        LOG.info("GET /UserAccounts/cart/?username="+username);

        try {
            UserAccount user = UserAccountDao.searchUsername(username);
            //LOG.info(user.toString());
            
            return new ResponseEntity<Product[]>(user.getShoppingCart(),HttpStatus.OK);
        }
        catch (IOException e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Assumes User is valid
    @PostMapping("addItem")
    public ResponseEntity<UserAccount> updateCart (@RequestBody UserAccount uAccount) {
        LOG.info("POST /UserAccounts/addItem" + uAccount);

        try {
            LOG.info(uAccount.toString());
            UserAccount newAccount = UserAccountDao.updateUser(uAccount);
            LOG.info(newAccount.toString());
            return new ResponseEntity<UserAccount>(newAccount,HttpStatus.OK);
        }
        catch (IOException e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("wishlist")
    public ResponseEntity<Product[]> getWishlistForUser(@RequestParam String username) {
        LOG.info("GET /UserAccounts/wishlist/?username="+username);

        try {
            UserAccount user = UserAccountDao.searchUsername(username);          
            return new ResponseEntity<Product[]>(user.getWishlist(),HttpStatus.OK);
        }
        catch (IOException e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for the UserAccount {@linkplain UserAccount UserAccount} whose name is
     * the text in password
     * 
     * @param password is text used to find the {@link UserAccount UserAccount}
     * 
     * @return ResponseEntity with {@link UserAccount UserAccount} object and
     * HTTP status of OK
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("password")
    public ResponseEntity<UserAccount> searchPassword(@RequestParam String password) {
        LOG.info("GET /UserAccounts/password/passwords="+password);

        try {
            UserAccount UserAccount = UserAccountDao.searchPassword(password);
            return new ResponseEntity<UserAccount>(UserAccount,HttpStatus.OK);
        }
        catch (IOException e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}