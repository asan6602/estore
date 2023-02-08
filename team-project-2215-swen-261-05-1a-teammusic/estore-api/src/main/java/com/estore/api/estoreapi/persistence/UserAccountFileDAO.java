package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.UserAccount;

/**
 * Implements the functionality for JSON file-based peristance for UserAccounts
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author aaron santos
 */
@Component
public class UserAccountFileDAO implements UserAccountDAO{
    private static final Logger LOG = Logger.getLogger(UserAccountFileDAO.class.getName());
    Map<Integer,UserAccount> UserAccounts; 
    private ObjectMapper objectMapper;
    private static int nextId;  // The next Id to assign to a new UserAccount
    private String filename;    // Filename to read from and write to

    /**
     * Creates a UserAccount File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public UserAccountFileDAO(@Value("${UserAccount.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the userAccounts from the file
    }

    /**
     * Generates the next id for a new {@linkplain UserAcount UserAccount}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

        /**
     * Generates an array of {@linkplain UserAccount UserAccounts} from the tree map
     * 
     * @return  The array of {@link UserAccount UserAccounts}, may be empty
     */
    private UserAccount[] getUserAccountArray() {
        return getUserAccountArray(null);
    }

    /**
     * Generates an array of {@linkplain UserAccount UserAccounts} from the tree map for any
     * {@linkplain UserAccount UserAccounts} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain UserAccount UserAccounts}
     * in the tree map
     * 
     * @return  The array of {@link UserAccount UserAccounts}, may be empty
     */
    private UserAccount[] getUserAccountArray(String containsText) { // if containsText == null, no filter
        ArrayList<UserAccount> UserAccountArrayList = new ArrayList<>();

        for (UserAccount userAccount : UserAccounts.values()) {
            if (containsText == null || userAccount.getUsername().contains(containsText)) {
                UserAccountArrayList.add(userAccount);
            }
        }

        UserAccount[] UserAccountArray = new UserAccount[UserAccountArrayList.size()];
        UserAccountArrayList.toArray(UserAccountArray);
        return UserAccountArray;
    }
    
    /**
     * Saves the {@linkplain UserAccount UserAccounts} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link UserAccount UserAccounts} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        UserAccount[] UserAccountArray = getUserAccountArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),UserAccountArray);
        return true;
    }

    /**
     * Loads {@linkplain UserAccount UserAccounts} from the JSON file into the map
     * 
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        UserAccounts = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of 
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file

        UserAccount[] UserAccountArray = objectMapper.readValue(new File(filename),UserAccount[].class);
        // Add each hero to the tree map and keep track of the greatest id
        
        for (UserAccount UserAccount : UserAccountArray) {
            UserAccounts.put(UserAccount.getId(),UserAccount);
            if (UserAccount.getId() > nextId)
                nextId = UserAccount.getId();
        }
        
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    

    /**
    ** {@inheritDoc}
     */
    @Override
    public UserAccount createUserAccount(UserAccount UserAccount) throws IOException {
        synchronized(UserAccounts) {
            // We create a new UserAccount object because the id field is immutable
            // and we need to assign the next unique id
            UserAccount newUserAccount = new UserAccount(nextId(), UserAccount.getUsername(), UserAccount.getPassword(), false, UserAccount.getShoppingCart(), UserAccount.getWishlist());
            UserAccounts.put(newUserAccount.getId(),newUserAccount);
            save(); // may throw an IOException
            return newUserAccount;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public UserAccount searchUsername(String username) throws IOException {
        synchronized(UserAccounts){
            for (UserAccount userAccount : UserAccounts.values()) {
                if (userAccount.getUsername().equals(username)) {
                    return userAccount;
                }
            }
            return null;
        }
    }

    @Override
    public UserAccount updateUser(UserAccount uAccount) throws IOException {
        synchronized(UserAccounts){
            if (UserAccounts.containsKey(uAccount.getId()) == false)
                return null;  // user does not exist
            UserAccounts.put(uAccount.getId(), uAccount);
            save(); // may throw an IOException
            return uAccount;
        }
    }

    /**
    ** {@inheritDoc}
    */
    @Override
    public UserAccount searchPassword(String password) throws IOException {
        synchronized(UserAccounts){
            for (UserAccount userAccount : UserAccounts.values()) {
                if (userAccount.getPassword().equals(password)) {
                    return userAccount;
                }
            }
            return null;
        }
    }
    
}
