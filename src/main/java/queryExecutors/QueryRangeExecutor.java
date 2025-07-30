package queryExecutors;

import models.Expense;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryRangeExecutor implements IQueryExecutor{

    Map<String, Range> criterias;

    public QueryRangeExecutor(Map<String, Range> criterias){
        this.criterias = criterias;
    }

    @Override
    public List<Expense> execute(List<Expense> list){
        return SearchExpenseByRange(list);
    }

    public boolean matchExpenseByRange(Expense expense, String category, String start, String stop){
        return switch (category) {
            case "date" -> expense.date >= Integer.parseInt(start) && expense.date <= Integer.parseInt(stop);
            case "item" -> expense.item.compareToIgnoreCase(start) > -1 && expense.item.compareToIgnoreCase(stop) < 1;
            case "type" -> expense.type.compareToIgnoreCase(start) > -1 && expense.type.compareToIgnoreCase(stop) < 1;
            case "spender" -> expense.spender.compareToIgnoreCase(start) > -1 && expense.spender.compareToIgnoreCase(stop) < 1;
            case "price" -> expense.price >= Double.parseDouble(start) && expense.price <= Double.parseDouble(stop);
            case "id" -> expense.id >= Integer.parseInt(start) && expense.id <= Integer.parseInt(stop);
            default -> throw new IllegalArgumentException("Category does not exist.");
        };
    }

    public List<Expense> SearchExpenseByRange(List<Expense> listOfExp){
        List<Expense> result = new ArrayList<Expense>();
        for (Expense expense : listOfExp) {
            int counter = 0;
            for (Map.Entry<String, Range> criteria : this.criterias.entrySet()) {
                if (matchExpenseByRange(expense, criteria.getKey(), criteria.getValue().start, criteria.getValue().stop))
                {
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
