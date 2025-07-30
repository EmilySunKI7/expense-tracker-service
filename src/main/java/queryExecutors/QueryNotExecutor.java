package queryExecutors;

import models.Expense;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryNotExecutor implements IQueryExecutor{

    Map<String, String> criterias;

    public QueryNotExecutor(Map<String, String> criterias){
        this.criterias = criterias;
    }

    @Override
    public List<Expense> execute(List<Expense> list){
        return SearchExpense(list);
    }

    boolean matchExpense(Expense expense, String category, String detail){
        return switch (category) {
            case "date" -> expense.date == (Integer.parseInt(detail));
            case "item" -> expense.item.equals(detail);
            case "type" -> manager.isEqualsOrSubcatOf(expense.type, detail);//expense.type.equals(detail);
            case "spender" -> expense.spender.equals(detail);
            case "price" -> expense.price == (Double.parseDouble(detail));
            case "id" -> expense.id == (Integer.parseInt(detail));
            default -> throw new IllegalArgumentException("Category does not exist.");
        };
    }

    List<Expense> SearchExpense(List<Expense> listOfExp){
        List<Expense> result = new ArrayList<Expense>();
        for (Expense expense : listOfExp) {
            int counter = 0;
            for (Map.Entry<String, String> criteria : this.criterias.entrySet()) {
                if (matchExpense(expense, criteria.getKey(), criteria.getValue())) {
                    counter++;
                }
            }

            if (counter != criterias.size() || criterias.isEmpty())
            {
                result.add(expense);
            }
        }
        return result;
    }

}

