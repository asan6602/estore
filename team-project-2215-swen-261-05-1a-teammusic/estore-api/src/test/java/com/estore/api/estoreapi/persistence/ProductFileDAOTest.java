package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.estore.api.estoreapi.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Persistence-tier")
public class ProductFileDAOTest {
    ProductFileDAO productFileDAO;
    Product[] testProducts;
    ObjectMapper mockObjectMapper;

    @BeforeEach
    public void setupProductFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testProducts = new Product[3];
        testProducts[0] = new Product(15, "song20", 20, 5, "image", "video", 0, 0);
        testProducts[1] = new Product(16, "song25", 20, 5, "image", "video", 0, 0);
        testProducts[2] = new Product(17, "song30", 20, 5, "image", "video", 0, 0);

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the Product array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Product[].class))
                .thenReturn(testProducts);
        productFileDAO = new ProductFileDAO("doesnt_matter.txt", mockObjectMapper);
    }

    @Test
    public void testGetProducts() throws IOException {
        Product[] products = productFileDAO.getProducts();
        assertEquals(products.length, testProducts.length);
        for (int i = 0; i < testProducts.length;++i)
            assertEquals(products[i],testProducts[i]);
    }

    @Test
    public void testSearchProducts() throws IOException{
        Product[] products = productFileDAO.searchProduct("song20");

        assertEquals(products.length, 1);
        assertEquals(products[0], testProducts[0]);
    }

    @Test
    public void testGetProduct() {
        // Invoke
        Product product = productFileDAO.getProduct(16);

        // Analzye
        assertEquals(product,testProducts[1]);

        //null test
        Product resultnull = assertDoesNotThrow(() -> productFileDAO.getProduct(123804),
                                "Unexpected exception thrown");
        assertEquals(null,resultnull);
    }

    @Test
    public void testCreateProduct() {
        // Setup
        Product product = new Product(18, "song66", 20, 5, "image", "video", 0, 0);

        // Invoke
        Product result = assertDoesNotThrow(() -> productFileDAO.createProduct(product),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Product actual = productFileDAO.getProduct(18);
        assertEquals(actual.getId(),product.getId());
        assertEquals(actual.getName(),product.getName());
    }

    @Test
    public void testUpdateProduct() {
        // Setup
        Product Product = new Product(17, "song70", 20, 5, "image", "video", 0, 0);

        // Invoke
        Product result = assertDoesNotThrow(() -> productFileDAO.updateProduct(Product),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Product actual = productFileDAO.getProduct(Product.getId());
        assertEquals(actual,Product);
    }

    @Test
    public void testDeleteProduct() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> productFileDAO.deleteProduct(17),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test Productes array - 1 (because of the delete)
        // Because Productes attribute of ProductFileDAO is package private
        // we can access it directly
        assertEquals(productFileDAO.products.size(), testProducts.length-1);

        //false test
        boolean resultnull = assertDoesNotThrow(() -> productFileDAO.deleteProduct(2859062),
                            "Unexpected exception thrown");
        assertEquals(false,resultnull);
    }
}
