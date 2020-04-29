package Control.Products;

import Control.Manager;
import Models.Account.Account;
import Models.Shop.Category;
import Models.Shop.Filter;
import Models.Shop.Sort;
import View.Products.ProductsMenu;

import java.util.ArrayList;
import java.util.List;

public class ProductsManager extends Manager {

    public ProductsManager(Account account, Category currentCategory) {
        super(account);
        this.currentCategory = currentCategory;
        this.menu = new ProductsMenu(this,currentCategory);
    }

    private Category currentCategory;
    private List<Filter> filters;
    private Sort currentSort;

    // view categories
    /*public String showCategories() {
        StringBuilder result = new StringBuilder();
        buildCategoryList(mainCategory, result, 1);
        return result.toString();
    }

    private void buildCategoryList(Category currentCategory, StringBuilder categoryField, int categoryLevel) {
        categoryField.append("-".repeat(Math.max(0, categoryLevel)));
        categoryField.append(currentCategory.getName()).append("\n");
        for (Category category : currentCategory.getSubCategories()) {
            buildCategoryList(category, categoryField, categoryLevel + 1);
        }
    }*/

    // filtering
    public String showAvailableFilters() {
        return null;
    }

    public boolean isEnteredFilterFieldValid(String field) {
        return true;
    }

    public ArrayList<String> applyFilter(String filterType, String filterValue) {
        return null;
    }

    public List<String> currentFilters() {
        return null;
    }

    public List<String> disableFilter(Filter filter) {
        return null;
    }

    private Filter getFilterByField(String field) {
        return null;
    }

    // sorting
    public String showAvailableSorts() {
        return null;
    }

    public void sort(String sort) {

    }

    public void currentSort() {

    }

    public void disableSort() {

    }

    // show products
    public List<String> showProducts() {
        return null;
    }

    // show product [productId]
    public void showProductById(String id) {

    }
}
