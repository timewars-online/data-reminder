package com.timewars.datarm.files;

import com.timewars.datarm.classes.myItem;

import java.util.ArrayList;

public class ItemsOperations {

    public ArrayList<myItem> getItems() {
        return items;
    }

    private ArrayList<myItem> items = new ArrayList<>();

    public ItemsOperations()
    {
        CustomConfig.setup();

        if(CustomConfig.getCustomFile().contains("items"))
            loadItems();

    }

    public void saveItems()
    {
        CustomConfig.getCustomFile().set("items", items);
        CustomConfig.save();
    }

    public void loadItems()
    {
        items = (ArrayList<myItem>) CustomConfig.getCustomFile().get("items");
    }
}
