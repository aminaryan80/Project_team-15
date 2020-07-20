package Client.Control.Principal.ManageCategories;

import Client.Control.Manager;
import Client.ViewController.Controller;
import Client.ViewController.principal.manageCategories.ManageCategoriesController;
import Models.Account.Account;
import Models.Shop.Category.Category;

import java.util.ArrayList;
import java.util.HashMap;

public class ManageCategoriesManager extends Manager {
    public ManageCategoriesManager(Account account) {
        super(account);
    }

    public ManageCategoriesManager(Account account, Addresses address, Manager manager) {
        super(account, address, manager);
        Controller controller = loadFxml(Addresses.MANAGE_CATEGORIES);
        update(controller);
    }

    public void update(Controller c) {
        ManageCategoriesController controller = (ManageCategoriesController) c;
        controller.init();
    }

    public int addCategory(String supCategoryName, String categoryName, HashMap<String, Integer> features, ArrayList<String> productsIds) {
        if (canAddCategory(supCategoryName, categoryName)) {
            new Category(categoryName, supCategoryName, features, productsIds);
            return 0;
        }
        return 1;
    }

    public boolean canAddCategory(String supCategoryName, String categoryName) {
        if (Category.hasCategoryWithName(supCategoryName)) {
            if (!Category.getCategoryByName(supCategoryName).hasCategoryInsideWithName(categoryName)) {
                return true;
            } else error("Invalid category name");
        } else error("Invalid category name");
        return false;
    }

    public void openEditCategory(String categoryName) {
        if (Category.hasCategoryWithName(categoryName))
            new EditCategoryManager(account, Category.getCategoryByName(categoryName));
        else error("Invalid category name");
        loadFxml(Addresses.EDIT_CATEGORY, true);
    }
}