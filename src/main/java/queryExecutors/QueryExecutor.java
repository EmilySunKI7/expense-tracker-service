package queryExecutors;

import models.Book;
import models.Expense;

import java.util.ArrayList;
import java.util.List;

public class QueryExecutor {

    private QueryBuilder queries;

    public QueryExecutor(QueryBuilder queries){
        this.queries = queries;
    }

    public List<Expense> runQuery(Book book)
    {
        List<Expense> listToReturn = new ArrayList<Expense>();
        for (Expense expense : book.getExpenses().values())
        {
            listToReturn.add(expense);
        }

        IQueryExecutor something = new QueryExactExecutor(queries.stuff);
        listToReturn = something.execute(listToReturn);

        something = new QueryRangeExecutor(queries.stuffRange);
        listToReturn = something.execute(listToReturn);

        something = new QueryLessThanExecutor(queries.stuffLessThan);
        listToReturn = something.execute(listToReturn);

        something = new QueryMoreThanExecutor(queries.stuffMoreThan);
        listToReturn = something.execute(listToReturn);

        something = new QueryNotExecutor(queries.stuffNot);
        listToReturn = something.execute(listToReturn);

        return listToReturn;
    }
}
