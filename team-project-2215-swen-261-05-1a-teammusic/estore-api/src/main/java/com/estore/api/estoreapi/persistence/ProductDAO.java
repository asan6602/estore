package com.estore.api.estoreapi.persistence;

import java.io.IOException;
import com.estore.api.estoreapi.model.Product;

/**
 * Defines the interface for Product object persistence
 * 
 * @author SWEN Faculty
 */
public interface ProductDAO {
    /**
     * Retrieves all {@linkplain Product Products}
     * 
     * @return An array of {@link Product Product} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Product[] getProducts() throws IOException;

    /**
     * Finds all {@linkplain Product Products} whose name contains the given text
     * 
     * @param containsText The text to match against
     * 
     * @return An array of {@link Product Products} whose nemes contains the given text, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Product[] searchProduct(String containsText) throws IOException;

    /**
     * Retrieves a {@linkplain Product Product} with the given id
     * 
     * @param id The id of the {@link Product Product} to get
     * 
     * @return a {@link Product Product} object with the matching id
     * <br>
     * null if no {@link Product Product} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Product getProduct(int id) throws IOException;

    /**
     * Creates and saves a {@linkplain Product Product}
     * 
     * @param hero {@linkplain Product Product} object to be created and saved
     * <br>
     * The id of the hero object is ignored and a new uniqe id is assigned
     *
     * @return new {@link Product Product} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    Product createProduct(Product product) throws IOException;

    /**
     * Updates and saves a {@linkplain Product Product}
     * 
     * @param {@link Product Product} object to be updated and saved
     * 
     * @return updated {@link Product Product} if successful, null if
     * {@link Product Product} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Product updateProduct(Product product) throws IOException;

    /**
     * Deletes a {@linkplain Product Product} with the given id
     * 
     * @param id The id of the {@link Product Product}
     * 
     * @return true if the {@link Product Product} was deleted
     * <br>
     * false if Product with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteProduct(int id) throws IOException;
}
