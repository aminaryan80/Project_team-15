package Models.Shop.Product;

import Control.Identity;
import Models.Account.Account;
import Models.Account.Customer;
import Models.Account.Seller;
import Models.Address;
import Models.Gson;
import Models.Shop.Category.Category;
import Models.Shop.Category.Feature;
import Models.Shop.Off.Auction;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Product {
    private final static String[] changeableFields = {"name", "price", "isAvailable",
            "description", "features"};
    private static ArrayList<Product> allProducts = new ArrayList<>();
    private String id;
    private ProductStatus status;
    private String name;
    private String companyName;
    private double price;
    //    private Seller seller;
    private String sellerUsername;
    private boolean isAvailable;
    //    private Category category;
    private String categoryName;
    private String categoryId;
    private String description;
    //    private ArrayList<Rate> allRates = new ArrayList<>();
    private ArrayList<String> allRatesIds = new ArrayList<>();
    //    private ArrayList<Customer> allBuyers = new ArrayList<>();
//    private ArrayList<String> allBuyersNames = new ArrayList<>();
    private ArrayList<String> allBuyersUsernames = new ArrayList<>();
    //    private List<Comment> allComments = new ArrayList<>();
    private List<String> allCommentsIds = new ArrayList<>();
    private ArrayList<Feature> features;
    //    private Auction auction;
    private String auctionId;

    public Product(String name, String companyName, double price, Seller seller,
                   boolean isAvailable, Category category, String description, ArrayList<Feature> features) {
        this.id = Identity.getId();
        this.name = name;
        this.companyName = companyName;
        this.price = price;
        this.sellerUsername = seller.getUsername();
        this.isAvailable = isAvailable;
        if (category != null) {
            this.categoryName = category.getName();
            this.categoryId = category.getId();
            category.addProduct(id);
        }
        this.description = description;
        this.features = features;
        this.status = ProductStatus.UNDER_REVIEW_FOR_CONSTRUCTION;
        allProducts.add(this);
    }

    public static ArrayList<Product> getAllAuctionedProducts() {
        ArrayList<Product> products = new ArrayList<>();
        for (Product product : allProducts) {
            if (product.getAuction().getBeginningDate().compareTo(LocalDate.now()) < 0 && product.getAuction().getEndingDate().compareTo(LocalDate.now()) > 0) {
                products.add(product);
            }
        }
        return products;
    }

    public static ArrayList<Product> getProductsBySeller(Seller seller) {
        ArrayList<Product> products = new ArrayList<>();
        for (Product product : allProducts) {
            if (product.getSeller().equals(seller)) {
                products.add(product);
            }
        }
        return products;
    }

    public ArrayList<Feature> getFeatures() {
        return features;
    }

    public String getDescription() {
        return description;
    }

    public boolean hasAuction() {
        return auctionId != null;
    }

    public static ArrayList<Product> getAllProducts() {
        return allProducts;
    }

    public static ArrayList<Product> getProductsByName(String name) {
        ArrayList<Product> products = new ArrayList<>();
        for (Product product : allProducts) {
            if (product.getName().equals(name)) {
                products.add(product);
            }
        }
        return products;
    }

    public static boolean hasProductWithId(String id) {
        for (Product product : allProducts) {
            if (product.getId().toLowerCase().equals(id.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasProductWithId(String id, String sellerUsername) {
        for (Product product : allProducts) {
            if (product.getId().toLowerCase().equals(id.toLowerCase()) && product.getSeller().getUsername().equals(sellerUsername)) {
                return true;
            }
        }
        return false;
    }

    public static Product getProductById(String id) {
        for (Product product : allProducts) {
            if (product.getId().toLowerCase().equals(id.toLowerCase())) {
                return product;
            }
        }
        return null;
    }

    public static boolean isEnteredProductFieldValid(String field) {
        List<String> fields = Arrays.asList(changeableFields);
        return fields.contains(field);
    }

    public static void addProduct(Product product) {
        allProducts.add(product);
    }

    public static void deleteProduct(Product product) throws IOException {
        allProducts.remove(product);
        File file = new File(Address.PRODUCTS.get() + "\\" + product.getId() + ".json");
        try {
            if (file.exists())
                FileUtils.forceDelete(file);
        } catch (Exception ignored) {

        }
    }

    public static ArrayList<String> viewProductsInShort(Seller seller) {
        ArrayList<String> allProductsInShort = new ArrayList<String>();
        for (Product product : allProducts) {
            if (product.getSeller().equals(seller)) {
                allProductsInShort.add(product.viewProductInShort());
            }
        }
        return allProductsInShort;
    }

    public static ProductStatus parseProductStatus(String statusName) {
        switch (statusName) {
            case "UNDER_REVIEW_FOR_CONSTRUCTION":
                return ProductStatus.UNDER_REVIEW_FOR_CONSTRUCTION;
            case "UNDER_REVIEW_FOR_EDITING":
                return ProductStatus.UNDER_REVIEW_FOR_EDITING;
            case "CONFIRMED":
                return ProductStatus.CONFIRMED;
            default:
                return null;
        }
    }

    public static void open() throws Exception {
        File folder = new File(Address.PRODUCTS.get());
        if (!folder.exists()) folder.mkdirs();
        else {
            for (File file : folder.listFiles()) {
                allProducts.add(open(file));
            }
        }
    }

    public static Product open(File file) throws Exception {
        StringBuilder json = new StringBuilder();
        Scanner reader = new Scanner(file);
        while (reader.hasNext()) json.append(reader.next());
        reader.close();
        return Gson.INSTANCE.get().fromJson(json.toString(), Product.class);
    }

    public static void save() throws Exception {
        for (Product product : allProducts) {
            save(product);
        }
    }

    public static void save(Product product) throws Exception {
        String jsonAccount = Gson.INSTANCE.get().toJson(product);
        FileWriter file = new FileWriter(Address.PRODUCTS.get() + "\\" + product.getId() + ".json");
        file.write(jsonAccount);
        file.close();
    }

    public void addFeature(Feature feature) {
        features.add(feature);
    }

    public void editFeature(String oldName, String newName) {
        getFeatureByName(oldName).setName(newName);
    }

    public void removeFeature(String feature) {
        features.remove(getFeatureByName(feature));
    }

    public Feature getFeatureByName(String name) {
        for (Feature feature : features) {
            if (feature.getName().equals(name)) {
                return feature;
            }
        }
        return null;
    }

    public void addBuyer(Customer buyer) {
        allBuyersUsernames.add(buyer.getUsername());
    }

    public ArrayList<Customer> getAllBuyers() {
        ArrayList<Customer> allBuyers = new ArrayList<>();
        for (String buyerUsername : allBuyersUsernames) {
            allBuyers.add((Customer) Customer.getAccountByUsername(buyerUsername));
        }
        return allBuyers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String viewProductInShort() {
        return "#" + id + " : " + name + " | " + price + "$";
    }

    public String getId() {
        return id;
    }

    public ArrayList<Comment> getAllComments() {
        ArrayList<Comment> allComments = new ArrayList<>();
        for (String commentId : allCommentsIds) {
            allComments.add(Comment.getCommentById(commentId));
        }
        return allComments;
    }

    public List<String> getComments() {
        List<String> comments = new ArrayList<>();
        int i = 1;
        for (Comment comment : getAllComments()) {
            comments.add("comment " + i + ":" + "\n" + comment.getText() + "\n");
            i++;
        }
        return comments;
    }

    public Seller getSeller() {
        return (Seller) Seller.getAccountByUsername(sellerUsername);
    }

    public void setSeller(Seller seller) {
        this.sellerUsername = seller.getUsername();
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public String getAttributes() {
        if (sellerUsername == null || categoryId == null) throw new NullPointerException();
        //TODO make better
        return "Name: " + name +
                "\nId: " + id +
                "\nCompany name: " + companyName +
                "\nSeller: " + Seller.getAccountByUsername(sellerUsername).getName() +
                "\nDescription: " + description +
                "\n" + Category.getCategoryById(categoryId).getFeaturesNames().toString();
    }

    public String digest() {
        if (auctionId != null)
            return "\nDescription: " + description +
                    "\nPrice: " + price +
                    "\nAuction amount: " + getAuction().getDiscountAmount();
        else return "\nDescription: " + description +
                "\nPrice: " + price +
                "\nNot on any auction";
    }

    public ArrayList<Rate> getAllRates() {
        ArrayList<Rate> allRates = new ArrayList<>();
        for (String rateId : allRatesIds) {
            allRates.add(Rate.getRateById(rateId));
        }
        return allRates;
    }

    public double getRate() {
        double sum = 0;
        for (Rate rate : getAllRates()) {
            sum += rate.getScore();
        }
        if (allRatesIds.size() != 0) {
            return sum / allRatesIds.size();
        } else return 0;
    }

    public void addRate(Account account, int rate) {
        for (String rateId : allRatesIds) {
            Rate previousRate = Rate.getRateById(rateId);
            if (previousRate.getUsername().equals(account.getUsername()) && previousRate.getProductId().equals(this.getId()))
            {
                previousRate.setScore(rate);
                return;
            }
        }
        allRatesIds.add((new Rate(account, rate, this)).getId());
    }

    public void addComment(Comment comment) {
        allCommentsIds.add(comment.getId());
    }

    public Auction getAuction() {
        return Auction.getAuctionById(auctionId);
    }

    public void setAuction(Auction auction) {
        this.auctionId = auction.getId();
    }

    public Category getCategory() {
        return Category.getCategoryById(categoryId);
    }

    public void setCategory(Category category) {
        this.categoryId = category.getId();
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAuctionedPrice() {
        if (getAuction() == null || !getAuction().isActive(LocalDate.now())) return price;
        else {
            if (getAuction().isActive(LocalDate.now())) return price - getAuction().getDiscountAmount();
            else return price;
        }
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "#" + id +
                "\nstatus = " + status +
                "\nname = '" + name + '\'' +
                "\ncompanyName = '" + companyName + '\'' +
                "\nprice = " + price +
                "\nseller = '" + Seller.getAccountByUsername(sellerUsername) + '\'' +
                "\nisAvailable = " + isAvailable +
                "\ncategory = '" + Category.getCategoryById(categoryId).getName() + '\'' +
                "\ndescription = '" + description + '\'' +
                "\nfeatures = '" + features + '\'';
    }

//    public static void loadReferences() {
//        for (Product product : allProducts) {
//            product.loadReference();
//        }
//    }

//    private void loadReference() {
//        seller = (Seller) Account.getAccountByUsername(sellerName);
//        category = Category.getCategoryByName(categoryName);
//        auction = Auction.getAuctionById(auctionId);
//        allBuyers = new ArrayList<>();
//        for (String buyersName : allBuyersUsernames) {
//            allBuyers.add((Customer) Account.getAccountByUsername(buyersName));
//        }
//    }

    public enum ProductStatus {
        UNDER_REVIEW_FOR_CONSTRUCTION, UNDER_REVIEW_FOR_EDITING, CONFIRMED
    }
}
