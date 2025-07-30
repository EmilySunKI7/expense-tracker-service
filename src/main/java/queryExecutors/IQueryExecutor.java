package queryExecutors;

import managers.CategoryManager;
import models.Expense;
import java.util.List;

interface IQueryExecutor {

    CategoryManager manager = new CategoryManager();

    List<Expense> execute(List<Expense> list);

}
