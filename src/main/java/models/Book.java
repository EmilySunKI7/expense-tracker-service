package models;

import java.util.Map;

public class Book {
    Map<Integer, Expense> expenses;

    public Book(){

    }

    public Book(Map<Integer, Expense> expenses){
        this.expenses = expenses;
    }

    public Map<Integer, Expense> getExpenses(){
        return this.expenses;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Expense expense:expenses.values()){
            sb.append(expense.toString());
        }
        return sb.toString();
    }
}
