package models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Set;

public class CategoryRequests {

    @JsonProperty("cats")
    List<ExpenseCategory> cats;


    public CategoryRequests(){ }

    public CategoryRequests(List<ExpenseCategory> cats){
        this.cats = cats;
    }

    public List<ExpenseCategory> getCats() {
        return cats;
    }

    public void setCats(List<ExpenseCategory> cats) {
        this.cats = cats;
    }


    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(ExpenseCategory cat:cats){
            sb.append(cat.toString() + "\n");
        }
        return sb.toString();
    }

}


