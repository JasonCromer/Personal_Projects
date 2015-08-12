package com.dev.cromer.jason.cshelper.Logic;

/**
 * Created by jason on 8/12/15.
 * Each Item represents a title (String) and image resource ID (int)
 * for a grid item in the GridViewAdapter, which is used for the
 * HomeScreen Fragment
 */
public class GridViewItem {

    String title;
    int imageId;

    public GridViewItem(String title, int imageId) {
        this.title = title;
        this.imageId = imageId;
    }
}
