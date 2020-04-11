package Models.Shop;

import Models.Account.Customer;
import Models.Account.Seller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Product {
    private static ArrayList<Product> allProducts = new ArrayList<Product>();
    private String id;
    private ProductStatus status;
    private String name;
    private String companyName;
    private double price;
    private Seller seller;
    private boolean isAvailable;
    private Category category;
    private String description;
    private double rate;
    private ArrayList<Customer> allBuyers;
    private List<Comment> allComments;
    private Map<String, String> features;
    //TODO different sellers for one product

    public static Product getProductById(String id) {

    }

    public List<String> getComments() {

    }

    public Seller getSeller() {
        return seller;
    }

    public static boolean hasProductWithId(String id) {

    }

    public static void deleteProduct(Product product) {

    }

    public static ArrayList<String> viewProductInShort() {

    }

    private enum ProductStatus {
        UNDER_REVIEW_FOR_CONSTRUCTION, UNDER_REVIEW_FOR_EDITING, CONFIRMED
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", status=" + status +
                ", name='" + name + '\'' +
                ", companyName='" + companyName + '\'' +
                ", price=" + price +
                ", seller=" + seller +
                ", isAvailable=" + isAvailable +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", rate=" + rate +
                ", allComments=" + allComments +
                '}';
    }
}
