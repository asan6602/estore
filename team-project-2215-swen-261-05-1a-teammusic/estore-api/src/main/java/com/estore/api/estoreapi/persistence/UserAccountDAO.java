package com.estore.api.estoreapi.persistence;

import java.io.IOException;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.UserAccount;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;

import org.apache.catalina.User;

/**
 * Defines the interface for UserAccount object persistence
 * 
 * @author aaron santos
 */
public interface UserAccountDAO {
        /**
     * Creates and saves a {@linkplain UserAccount UserAccount}
     * 
     * @param UserAccount {@linkplain UserAccount UserAccount} object to be created and saved
     * 
     * The id of the UserAccount object is ignored and a new unique id is assigned
     *
     * @return new {@link UserAccount UserAccount} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    public UserAccount createUserAccount(UserAccount UserAccount) throws IOException;

    /**
     * Finds all {@linkplain UserAccount UserAccounts} whose name contains the given text
     * 
     * @param containsText The text to match against
     * 
     * @return An array of {@link UserAccount UserAccounts} whose nemes contains the given text, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    //UserAccount[] searchUserAccount(String containsText) throws IOException;

    /**
     * Finds {@linkplain UserAccount UserAccount} whose username is the given text
     * 
     * @param username The text to match against
     * 
     * @return A {@link UserAccount UserAccount} whose name is the given text
     * 
     * @throws IOException if an issue with underlying storage
     */
    UserAccount searchUsername(String username) throws IOException;

    UserAccount updateUser(UserAccount uAccount) throws IOException;

    /**
     * Finds {@linkplain UserAccount UserAccount} whose password is the given text
     * 
     * @param password The text to match against
     * 
     * @return a {@link UserAccount UserAccount} whose password is given text
     * 
     * @throws IOException if an issue with underlying storage
     */
    UserAccount searchPassword(String password) throws IOException;
}
