package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class ProductTest {
    
    @Test
    public void testConstructor() {
        Product product = new Product(2, "Constructor Test", 0.00, 5, "image", "video", 0, 0);
        assertEquals(product.getId(), 2);
        assertEquals(product.getName(), "Constructor Test");
        assertEquals(product.getPrice(), 0.0);
        assertEquals(product.getQuantity(), 5);
    }

    @Test
    public void testGetId() {
        Product product = new Product(2, "Get ID Test", 0.00, 5, "image", "video", 0, 0);
        assertEquals(product.getId(), 2);
    }

    // Can't test setter without getter
    @Test
    public void testSetName() {
        Product product = new Product(2, "Set Dummy name", 0.00, 5, "image", "video", 0, 0);
        product.setName("Updated Name");
        assertEquals(product.getName(), "Updated Name");
    }

    @Test
    public void testGetName() {
        Product product = new Product(2, "Get Name Test", 0.00, 5, "image", "video", 0, 0);
        assertEquals(product.getName(), "Get Name Test");
    }

    @Test 
    public void testGetImage() {
        Product product = new Product(2, "Get Image Test", 0.00, 5, "image", "video", 0, 0);
        assertEquals(product.getImage(), "image");
    }

    @Test
    public void testGetVideo() {
        Product product = new Product(2, "Get Video Test", 0.00, 5, "image", "video", 0, 0);
        assertEquals(product.getVideo(), "video");
    }


    @Test
    public void testSetPrice() {
        Product product = new Product(2, "Set Dummy name", 0.00, 5, "image", "video", 0, 0);
        product.setPrice(10.00);
        assertEquals(product.getPrice(), 10.00);
    }

    @Test
    public void testGetPrice() {
        Product product = new Product(2, "Get Name Test", 0.00, 5, "image", "video", 0, 0);
        assertEquals(product.getPrice(), 0.00);
    }

    @Test
    public void testSetQuantity() {
        Product product = new Product(2, "Set Dummy name", 0.00, 5, "image", "video", 0, 0);
        product.setQuantity(200);
        assertEquals(product.getQuantity(), 200);
    }

    @Test
    public void testGetQuantity() {
        Product product = new Product(2, "Get Name Test", 0.00, 5, "image", "video", 0, 0);
        assertEquals(product.getQuantity(), 5);
    }

    @Test
    public void testSetupVotes() {
        Product product = new Product(2, "Set Dummy name", 0.00, 5, "image", "video",0,0);
        product.setUpVotes(200);
        assertEquals(product.getUpVotes(), 200);
    }

    @Test
    public void testSetdownVotes() {
        Product product = new Product(2, "Set Dummy name", 0.00, 5, "image", "video",0,0);
        product.setDownVotes(200);
        assertEquals(product.getDownVotes(), 200);
    }

    @Test
    public void testToString() {
        int id = 20;
        String name = "Test Case";
        double price = 2.00;
        int quantity = 15;
        String image = "image";
        String video = "video";
        String expected_string = String.format(Product.STRING_FORMAT, id, name, price, quantity,image,video, 0, 0);
        
        Product product = new Product(id, name, price, quantity,image, video, 0, 0);

        String actual_string = product.toString();
        assertEquals(expected_string, actual_string);
    }
}
