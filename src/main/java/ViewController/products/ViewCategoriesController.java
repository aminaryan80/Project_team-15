package ViewController.products;

import ViewController.Controller;
import javafx.scene.control.TreeView;

public class ViewCategoriesController extends Controller {

    public TreeView<String> categoriesTreeView;

    public void init() {
        categoriesTreeView.setRoot(manager.getCategoriesInTable());
    }
}