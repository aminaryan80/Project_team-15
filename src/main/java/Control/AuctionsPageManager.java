package Control;
import Control.CustomerManagers.ProductPageManager;
import Control.Manager;
import Models.Account.Account;
import Models.Shop.Category.*;
import Models.Shop.Product.Product;
import View.Products.ProductsMenu;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AuctionsPageManager extends Manager {

    private Category currentCategory;
    private List<Filter> filters = new ArrayList<>();
    private List<LengthFilter> lengthFilters = new ArrayList<>();
    private Sort currentSort = null;
    private List<Product> products;

    public AuctionsPageManager(Account account) {
        super(account);
        this.currentCategory = mainCategory;
        products = Product.getAllAuctionedProducts();
        this.menu = new ProductsMenu(this, productsInShort());
    }

    // filtering
    public String showAvailableFilters() {
        return "status\n" +
                "name\n" +
                "companyName\n" +
                "price\n" +
                "seller\n" +
                "isAvailable\n" +
                "category\n" +
                currentCategory.getFeaturesNames().toString();
    }

    public boolean hasFeatureWithName(String name) {
        if (currentCategory == getMainCategory()) {
            return false;
        }
        return currentCategory.getFeaturesNames().contains(name);
    }

    public boolean isEnteredFilterFieldValid(String field) {
        for (Filter filter : filters) {
            if (filter.getField().equals(field)) {
                return false;
            }
        }
        if (field.equals("status") || field.equals("name") || field.equals("companyName") ||
                field.equals("price") || field.equals("seller") || field.equals("isAvailable")) {
            return true;
        }
        if (!currentCategory.equals(mainCategory) && currentCategory.getFeaturesNames().contains(field))
            return true;
        return field.equals("category") && currentCategory.getSubCategories() != null;
    }

    public ArrayList<String> applyFilter(String filterType, String filterValue) {
        filters.add(new Filter(filterType, filterValue));
        if (filterType.equals("category")) {
            currentCategory = Category.getCategoryByName(filterValue);
        }
        products = Product.getAllAuctionedProducts();
        setFilters();
        applySort();
        return productsInShort();
    }

    public ArrayList<String> applyFilter(String filterType, String minValue, String maxValue) {
        lengthFilters.add(new LengthFilter(filterType, minValue, maxValue));
        products = Product.getAllAuctionedProducts();
        setFilters();
        applySort();
        return productsInShort();
    }

    private void setFilters() {
        for (Filter filter : filters) {
            String field = filter.getField();
            if (field.equals("status")) {
                products = setStatusFilter(filter);
            } else if (field.equals("name")) {
                products = setNameFilter(filter);
            } else if (field.equals("companyName")) {
                products = setCompanyNameFilter(filter);
            } else if (field.equals("price")) {
                products = setPriceFilter(filter);
            } else if (field.equals("seller")) {
                products = setSellerFilter(filter);
            } else if (field.equals("isAvailable")) {
                products = setIsAvailableFilter(filter);
            } else if (field.equals("category")) {
                products = setCategoryFilter(filter);
            } else {
                products = setFeaturesFilter(filter);
            }
        }
        for (LengthFilter lengthFilter : lengthFilters) {
            if (lengthFilter.getField().equals("price")) {
                setPriceLengthFilter(lengthFilter);
            }
        }
    }

    private ArrayList<Product> setPriceLengthFilter(LengthFilter filter) {
        return products.stream().filter(product -> {
            return product.getPrice() <= Double.parseDouble(filter.getMaxValue()) || product.getPrice() >= Double.parseDouble(filter.getMinValue());
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<String> productsInShort() {
        ArrayList<String> productsInShort = new ArrayList<>();
        for (Product product : products) {
            productsInShort.add(product.getId() + "  " + product.getName() + "  " + product.getPrice() + "  " + (product.getPrice() - product.getAuction().getDiscountAmount()));
        }
        return productsInShort;
    }

    private ArrayList<Product> setFeaturesFilter(Filter filter) {
        return products.stream().filter(product -> {
            Feature feature = product.getFeatureByName(filter.getField());
            return feature.getValue().equals(filter.getValue());
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<Product> setCategoryFilter(Filter filter) {
        return products.stream().filter(product -> {
            return product.getCategory().equals(Category.getCategoryByName(filter.getValue()));
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<Product> setIsAvailableFilter(Filter filter) {
        return products.stream().filter(product -> {
            return product.isAvailable() == Boolean.parseBoolean(filter.getValue());
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<Product> setSellerFilter(Filter filter) {
        return products.stream().filter(product -> {
            return product.getSeller().equals(Account.getAccountByUsername(filter.getValue()));
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<Product> setPriceFilter(Filter filter) {
        return products.stream().filter(product -> {
            return product.getPrice() == Double.parseDouble(filter.getValue());
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<Product> setCompanyNameFilter(Filter filter) {
        return products.stream().filter(product -> {
            return product.getCompanyName().equals(filter.getValue());
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<Product> setNameFilter(Filter filter) {
        return products.stream().filter(product -> {
            return product.getName().equals(filter.getValue());
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<Product> setStatusFilter(Filter filter) {
        return products.stream().filter(product -> {
            return product.getStatus().equals(Product.parseProductStatus(filter.getValue()));
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    public List<String> currentFilters() {
        ArrayList<String> filtersNames = new ArrayList<String>();
        for (Filter filter : filters) {
            filtersNames.add(filter.toString());
        }
        for (LengthFilter lengthFilter : lengthFilters) {
            filtersNames.add(lengthFilter.toString());
        }
        return filtersNames;
    }

    public boolean isItSelectedFilter(String filterName) {
        for (Filter filter : filters) {
            if (filter.getField().equals(filterName)) {
                return true;
            }
        }
        for (LengthFilter lengthFilter : lengthFilters) {
            if (lengthFilter.getField().equals(filterName)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEnteredLengthFilterFieldValid(String field) {
        return field.equals("price");
    }

    public String showAvailableLengthFilter() {
        return "price";
    }

    public List<String> disableFilter(String filterField) {
        Object filter = getFilterByField(filterField);
        if (filter instanceof Filter) {
            filters.remove(filter);
        } else {
            lengthFilters.remove(filter);
        }
        products = Product.getAllAuctionedProducts();
        if (filterField.equals("category")) {
            currentCategory = mainCategory;
        }
        setFilters();
        applySort();
        return productsInShort();
    }

    private Object getFilterByField(String field) {
        for (Filter filter : filters) {
            if (filter.getField().equals(field)) {
                return filter;
            }
        }
        for (LengthFilter lengthFilter : lengthFilters) {
            if (lengthFilter.getField().equals(field)) {
                return lengthFilter;
            }
        }
        return null;
    }

    // sorting
    public String showAvailableSorts() {
        return "price\n" +
                "name\n" +
                "rating\n" +
                "features";
    }

    public ArrayList<Product> sort(String sort, boolean isAscending) {
        products = Product.getAllAuctionedProducts();
        setFilters();
        currentSort = new Sort(sort, isAscending);
        applySort();
        return (ArrayList<Product>) products;
    }

    private void applySort() {
        if (currentSort == null) {
            return;
        }
        String field = currentSort.getField();
        if (field.equals("price")) {
            sortByPrice();
        } else if (field.equals("name")) {
            sortByName();
        } else if (field.equals("rating")) {
            sortByRating();
        } else {
            sortByFeature();
        }
        if (!currentSort.isAscending()) {
            Collections.reverse(products);
        }
    }

    private void sortByPrice() {
        Product[] productsForSort = products.toArray(new Product[0]);
        for (int i = 0; i < productsForSort.length; i++) {
            for (int j = i + 1; j < productsForSort.length; j++) {
                if (productsForSort[i].getPrice() > productsForSort[j].getPrice()) {
                    Product temp = productsForSort[i];
                    productsForSort[i] = productsForSort[j];
                    productsForSort[j] = temp;
                }
            }
        }
        products = Arrays.asList(productsForSort);
    }

    private void sortByName() {
        Product[] productsForSort = products.toArray(new Product[0]);
        for (int i = 0; i < productsForSort.length; i++) {
            for (int j = i + 1; j < productsForSort.length; j++) {
                if (productsForSort[i].getName().compareTo(productsForSort[j].getName()) > 0) {
                    Product temp = productsForSort[i];
                    productsForSort[i] = productsForSort[j];
                    productsForSort[j] = temp;
                }
            }
        }
        products = Arrays.asList(productsForSort);
    }

    private void sortByFeature() {
        Product[] productsForSort = products.toArray(new Product[0]);
        for (int i = 0; i < productsForSort.length; i++) {
            for (int j = i + 1; j < productsForSort.length; j++) {
                if (productsForSort[i].getFeatureByName(currentSort.getField()).getValue().compareTo(productsForSort[j].getFeatureByName(currentSort.getField()).getValue()) > 0) {
                    Product temp = productsForSort[i];
                    productsForSort[i] = productsForSort[j];
                    productsForSort[j] = temp;
                }
            }
        }
        products = Arrays.asList(productsForSort);
    }

    private void sortByRating() {
        Product[] productsForSort = products.toArray(new Product[0]);
        for (int i = 0; i < productsForSort.length; i++) {
            for (int j = i + 1; j < productsForSort.length; j++) {
                if (productsForSort[i].getRate() > productsForSort[j].getRate()) {
                    Product temp = productsForSort[i];
                    productsForSort[i] = productsForSort[j];
                    productsForSort[j] = temp;
                }
            }
        }
        products = Arrays.asList(productsForSort);
    }

    public boolean isEnteredSortFieldValid(String field) {
        return field.equals("price") || field.equals("name") || field.equals("rating") || field.equals("features");
    }

    public String currentSort() {
        return currentSort.toString();
    }

    public ArrayList<String> disableSort() {
        currentSort = null;
        products = Product.getAllAuctionedProducts();
        setFilters();
        return productsInShort();
    }

    public boolean hasProductWithId(String id) {
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    // show products
    public List<String> showProducts() {
        products = currentCategory.getAllProducts().stream().filter(product -> {
            return product.getAuction().getBeginningDate().compareTo(LocalDate.now()) < 0 && product.getAuction().getEndingDate().compareTo(LocalDate.now()) > 0;
        }).collect(Collectors.toCollection(ArrayList::new));
        setFilters();
        applySort();
        return productsInShort();
    }

    // show product [productId]
    public void showProductById(String id) {
        new ProductPageManager(this.getAccount(), Product.getProductById(id));
    }
}
