package Client.Control.Seller;

import Client.Control.Manager;
import Client.ViewController.Controller;
import Models.Account.Account;
import Models.Account.Seller;
import Models.Shop.Category.Sort;
import Models.Shop.Log.SellingLog;
import Models.Shop.Product.Product;
import Models.Shop.Request.DeleteProductRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SellerManager extends Manager {

    private Sort currentSort;
    private ArrayList<SellingLog> logs;

    public SellerManager(Account account) {
        super(account);
    }

    public SellerManager(Account account, Addresses address, Manager manager) {
        super(account, address, manager);
    }

    public void update(Controller c) {
        /*SellerMenuController controller = (SellerMenuController) c;
        controller.setSeller(account);
        controller.init();*/
    }

    public void deleteProductById(String sellerUsername, String id) {
        Seller seller = (Seller) Account.getAccountByUsername(sellerUsername);
        new DeleteProductRequest(seller, Product.getProductById(id));
    }

    public String showAvailableSorts() {
        return "money\n" +
                "date";
    }

    public ArrayList<Object> sort(String sort, boolean isAscending) {
        logs = ((Seller) account).getAllLogs();
        currentSort = new Sort(sort, isAscending);
        applySort();
        return new ArrayList<>(logs);
    }

    private void applySort() {
        if (currentSort == null) {
            return;
        }
        String field = currentSort.getField();
        if (field.equals("money")) {
            sortByMoney();
        } else {
            sortByDate();
        }
        if (!currentSort.isAscending()) {
            Collections.reverse(logs);
        }
    }

    private void sortByMoney() {
        SellingLog[] logsForSort = logs.toArray(new SellingLog[0]);
        for (int i = 0; i < logsForSort.length; i++) {
            for (int j = i + 1; j < logsForSort.length; j++) {
                if (logsForSort[i].getMoney() > logsForSort[j].getMoney()) {
                    SellingLog temp = logsForSort[i];
                    logsForSort[i] = logsForSort[j];
                    logsForSort[j] = temp;
                }
            }
        }
        logs = (ArrayList<SellingLog>) Arrays.asList(logsForSort);
    }

    private void sortByDate() {
        SellingLog[] logsForSort = logs.toArray(new SellingLog[0]);
        for (int i = 0; i < logsForSort.length; i++) {
            for (int j = i + 1; j < logsForSort.length; j++) {
                if (logsForSort[i].getDate().isBefore(logsForSort[j].getDate())) {
                    SellingLog temp = logsForSort[i];
                    logsForSort[i] = logsForSort[j];
                    logsForSort[j] = temp;
                }
            }
        }
        logs = (ArrayList<SellingLog>) Arrays.asList(logsForSort);
    }

    public boolean isEnteredSortFieldValid(String field) {
        return field.equals("money") || field.equals("date");
    }

    public String currentSort() {
        return currentSort.toString();
    }

    public ArrayList<Object> disableSort() {
        currentSort = null;
        logs = ((Seller) account).getAllLogs();
        return new ArrayList<>(logs);
    }

    public void openManageProducts() {
        new ManageProductsManager(account, Addresses.SELLER_MENU, this);
    }

    public void openManageOffs() {
        new ViewOffsManager(account, Addresses.SELLER_MENU, this);
    }
}