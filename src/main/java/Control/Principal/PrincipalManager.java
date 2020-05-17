package Control.Principal;

import Control.Identity;
import Control.Manager;
import Models.Account.Account;
import Models.Account.Customer;
import Models.Shop.Off.Discount;
import View.Principal.PrincipalMenu;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PrincipalManager extends Manager {

    public PrincipalManager(Account account) {
        super(account);
        this.menu = new PrincipalMenu(this);
    }

    public void createDiscountCode(ArrayList<String> newDiscountInputs, ArrayList<String> allowedCustomersNames) {
        // 0:discount percent - 1:maximumDiscount - 2:discountUseCount - 3:beginningDate - 4:endingDate
        // Discount discount = new Discount(generateNewId(), parseDate(newDiscountInputs.get(3)), parseDate(newDiscountInputs.get(4)),
        Discount discount = new Discount(
                Identity.getId(),
                LocalDate.parse(newDiscountInputs.get(3)),
                LocalDate.parse(newDiscountInputs.get(4)),
                Integer.parseInt(newDiscountInputs.get(0)),
                Double.parseDouble(newDiscountInputs.get(1)),
                Integer.parseInt(newDiscountInputs.get(2)),
                allowedCustomersNames
        );
        for (Customer customer : getCustomersListByNames(allowedCustomersNames)) {
            customer.addDiscount(discount);
        }
    }

    private ArrayList<Customer> getCustomersListByNames(ArrayList<String> allowedCustomersNames) {
        ArrayList<Customer> customers = new ArrayList<>();
        for (String customerName : allowedCustomersNames) {
            customers.add((Customer) Account.getAccountByUsername(customerName));
        }
        return customers;
    }
}
