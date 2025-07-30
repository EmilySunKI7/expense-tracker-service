package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;
import java.util.Set;

@JsonIgnoreProperties(value = { "children" })
public class ExpenseCategory {

    private String category;
    private String parent;
    private Set<ExpenseCategory> children;

    public ExpenseCategory(){}

    public ExpenseCategory(String category) {
        this.category = category;
    }

    public ExpenseCategory(String category, String parent){
        this.category = category;
        this.parent = parent;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Set<ExpenseCategory> getChildren() {
        return children;
    }

    public void setChildren(Set<ExpenseCategory> set){
        this.children = set;
    }

    public boolean isParentOf(ExpenseCategory child){
        return this.category.equals(child.parent);
    }

    public Set<ExpenseCategory> addChild(ExpenseCategory child){
        this.children.add(child);
        child.parent = this.category;
        return this.children;
    }

    public Set<ExpenseCategory> removeChild(ExpenseCategory child){
        this.children.remove(child);
        child.parent = null;
        return this.children;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenseCategory cat = (ExpenseCategory) o;
        return this.category.toLowerCase().replaceAll("\\s", "").equals(cat.category.toLowerCase().replaceAll("\\s", ""));
    }

    @Override
    public int hashCode() {
        return Objects.hash(category.toLowerCase().replaceAll("\\s", ""));
    }

    @Override
    public String toString(){
        return "{category: " + this.category + ", parent: " + this.parent + "} \n";
    }

    public String toString2(){
        {
            int chilrenSize = 0;
            if(children != null){
                chilrenSize = this.children.size();
            }
            return "{category: " + this.category + ", parent: " + this.parent + ", number of children: " +chilrenSize + "}";
        }
    }

}
