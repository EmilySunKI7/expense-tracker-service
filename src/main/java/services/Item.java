package services;

import java.io.Serializable;

public class Item implements Serializable {

    long id;
    String name;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Item(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
