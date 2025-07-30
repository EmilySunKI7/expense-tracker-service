package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import managers.CategoryManager;

import java.util.List;

public class Expense {

    public int date;
    public String item;
    public String type;
    //public static List<ExpenseCategory> categoryList;
    public String spender;
    public double price;
    public int id;


    public Expense(@JsonProperty("date") int date, @JsonProperty("item") String item, @JsonProperty("type") String type, @JsonProperty("spender") String spender,
                   @JsonProperty("price") double price, @JsonProperty("id") int id){
        this.date = date;
        this.item = item;
        this.type = type;
        this.spender = spender;
        this.price = price;
        this.id = id;
    }

    public Expense(){

    }

    @Override
    public String toString(){
        return id + ": {date: " + date + ", item: " + item + ", type: " + type + ", spender: " + spender + ", price: " + price + "}\n";
    }


}
