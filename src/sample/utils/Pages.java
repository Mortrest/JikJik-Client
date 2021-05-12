package sample.utils;

import javafx.scene.layout.AnchorPane;

import java.util.LinkedList;

public class Pages {
    static LinkedList<AnchorPane> pages;
    public Pages(){
        pages = new LinkedList<>();

    }

    public static LinkedList<AnchorPane> getPages() {
        return pages;
    }

}
