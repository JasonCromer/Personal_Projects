package com.dev.cromer.jason.cshelper.Logic;

/**
 * Created by jason on 8/4/15.
 * This Class represents a DataStructure in the DataStructures Fragment.
 * Each DataStructureItem has a String datatype called name.
 */

public class DataStructureItem {

    String name;
    int imageID;

    public DataStructureItem(String name, int imageID) {
        this.name = name;
        this.imageID = imageID;
    }

    public String getName() {
        return name;
    }

    public int getImageID() {
        return imageID;
    }
}
