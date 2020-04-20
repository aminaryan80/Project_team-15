package Control;

import Models.Account.Account;
import Models.Account.Customer;
import Models.Shop.Product;

import java.util.ArrayList;

public class CustomerManager extends MainManager {

    public CustomerManager(Account account) {
        super(account);
    }

    // edit [field]
    public boolean isEnteredAccountFieldValid(String field) {

    }

    public void editAccountAttribute(String field, String newAttribute) {

    }

    public boolean isEnteredFieldValid(String type) {

    }

    // view cart
    public String viewCart() {
        // show commands
    }

    public String showProducts() {

    }

    public void viewProductById(String id) {
        // goes to product page
    }

    public void increaseProductQuantity(String id) {

    }

    public void decreaseProductQuantity(String id) {

    }

    public boolean hasProductInAuctions(Product product) {
        product.getSeller().hasProductInAuctions(product);
    }

    public double getTotalPrice() {
        return ((Customer) account).getCart().getTotalPrice();
    }

    // purchase
    public void pay(ArrayList<String> receiverInformation, String discountCode) {

    }

    public double canPay() {

    }

    private double getPaymentAmountDiscountApplied() {

    }

    // view orders
    public ArrayList<String> viewOrders() {

    }

    public String showOrderById(String id) {

    }

    public void rateProduct(String id, int score) {

    }

    // view balance
    public double viewCustomerBalance() {

    }

    // view discount codes
    public ArrayList<String> viewDiscountCodes() {

    }
}