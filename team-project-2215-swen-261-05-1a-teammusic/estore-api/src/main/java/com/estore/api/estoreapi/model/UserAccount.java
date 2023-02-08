package com.estore.api.estoreapi.model;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Represents a UserAccount entity
 * 
 * @author aaron santos
 */

public class UserAccount {
    private static final Logger LOG = Logger.getLogger(UserAccount.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = "UserAccount [id=%d, username=%s, password=%s, isAdmin=%s, shoppingCart=%s, wishlist=%s]";

    @JsonProperty("id") private int id;
    @JsonProperty("username") private String username;
    @JsonProperty("password") private String password;
    @JsonProperty("isAdmin") private boolean isAdmin;
    @JsonProperty("shoppingCart") private Product[] shoppingCart;
    @JsonProperty("wishlist") private Product[] wishlist;


    /**
     * Create a UserAccount
     * if no isAdmin is specified, it is set to false
     * @param id The id of the user
     * @param username the username
     * @param password the password
     * @param isAdmin whether the account is an Admin or not
     */

    @JsonCreator
    public UserAccount(@JsonProperty("id") int id, @JsonProperty("username") String username, @JsonProperty("password") String password, @JsonProperty("isAdmin") Boolean isAdmin, @JsonProperty("shoppingCart") Product[] shoppingCart, @JsonProperty("wishlist") Product[] wishlist) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.shoppingCart = shoppingCart;
        this.wishlist = wishlist;
    }

    public int getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }
    
    public Product[] getShoppingCart(){
        return this.shoppingCart;
    }

    public void setShoppingCart(Product[] cart) {
        this.shoppingCart = cart;
    }

    public Product[] getWishlist(){
        return this.wishlist;
    }

    public void setWishlist(Product[] wishlist) {
        this.wishlist = wishlist;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, this.getId(), this.getUsername(), this.getPassword(), isAdmin, 
                            this.shoppingCart, this.wishlist);
    }

}
