package com.estore.api.estoreapi.model;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Represents a Product entity
 * 
 * @author Team 1
 */
public class Product {
    private static final Logger LOG = Logger.getLogger(Product.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = "Product [id=%d, name=%s, price=%s, quantity=%s, image=%s, video=%s, upVotes=%d, downVotes=%d]";

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("price") private double price;
    @JsonProperty("quantity") private int quantity;
    @JsonProperty("image") private String image;
    @JsonProperty("video") private String video;
    @JsonProperty("upVotes") private int upVotes;
    @JsonProperty("downVotes") private int downVotes;

    /**
     * Create a product with the given id and name
     * @param id The id of the product
     * @param name The name of the product
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */

    public Product(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("price") double price, @JsonProperty("quantity") int quantity, @JsonProperty("image") String image,  @JsonProperty("video") String video
    , @JsonProperty("upVotes") int upVotes, @JsonProperty("downVotes") int downVotes) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.video = video;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
    }

    /**
     * Retrieves the id of the product
     * @return The id of the product
     */
    public int getId() {return id;}

    /**
     * Sets the name of the product - necessary for JSON object to Java object deserialization
     * @param name The name of the product
     */
    public void setName(String name) {this.name = name;}

    /**
     * Retrieves the name of the product
     * @return The name of the product
     */
    public String getName() {return name;}

    /**
     * Retrieves the image link of the product
     * @return image link of the product
     */
    public String getImage() {
        return image;
    }

    /**
     * Retrieves the video link of the product
     * @return vidoe link of the product
     */
    public String getVideo() {
        return video;
    }

    /**
     * Retrieves the amount of upvotes of the product
     * @return upvotes of the product
     */
    public int getUpVotes() {
        return upVotes;
    }

     /**
     * updates the amount of upVotes of a product
     * @return void
     */
    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

     /**
     * Retrieves the amount of downvotes of the product
     * @return downvotes of the product
     */
    public int getDownVotes() {
        return downVotes;
    }

     /**
     * updates the amount of downVotes of a product
     * @return void
     */
    public void setDownVotes(int downVotes) {
        this.downVotes = downVotes;
    }
    

    /**
     * Sets the price of the product - necessary for JSON object to Java object deserialization
     * @param price The price of the product
     */    
    public void setPrice(double price) {this.price = price;}

    /**
     * Retrieves the price of the product
     * @return The price of the product
     */
    public double getPrice() {return price;}

    /**
     * Sets the quantity of the product - necessary for JSON object to Java object deserialization
     * @param quantity The quantity of the product
     */    
    public void setQuantity(int quantity) {this.quantity = quantity;}

    /**
     * Retrieves the quantity of the product
     * @return The quantity of the product
     */
    public int getQuantity() {return quantity;}

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,name,price,quantity,image,video,upVotes,downVotes);
    }
}
