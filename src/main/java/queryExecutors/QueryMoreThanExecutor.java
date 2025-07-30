package queryExecutors;

import models.Expense;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryMoreThanExecutor implements IQueryExecutor {

     Map<String, String> criterias;

     public QueryMoreThanExecutor(Map<String, String> criterias){
            this.criterias = criterias;
     }

     @Override
     public List<Expense> execute(List<Expense> list){
         return SearchExpenseByMoreThan(list);
     }

     public boolean matchExpenseByMoreThan(Expense expense, String category, String stop){
         return switch (category) {
             case "date" -> expense.date > Integer.parseInt(stop);
             case "item" -> expense.item.compareToIgnoreCase(stop) > 0;
             case "type" -> expense.type.compareToIgnoreCase(stop) > 0;
             case "spender" -> expense.spender.compareToIgnoreCase(stop) > 0;
             case "price" -> expense.price > Double.parseDouble(stop);
             case "id" -> expense.id > Integer.parseInt(stop);
             default -> throw new IllegalArgumentException("Category does not exist.");
         };
     }

     public List<Expense> SearchExpenseByMoreThan(List<Expense> listOfExp){
         List<Expense> result = new ArrayList<Expense>();
         for (Expense expense : listOfExp) {
             int counter = 0;
             for (Map.Entry<String, String> criteria : this.criterias.entrySet()) {
                 if (matchExpenseByMoreThan(expense, criteria.getKey(), criteria.getValue())) {
                     counter++;
                 }
             }

             if (counter == criterias.size()) {
                 result.add(expense);
             }
         }
         return result;
     }

}
