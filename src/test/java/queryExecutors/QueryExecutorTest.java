package queryExecutors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import managers.BookManager;
import managers.CategoryManager;
import models.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import services.Expenses;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class QueryExecutorTest {

    static Expenses expense;
    static Book book;

    @BeforeAll
    static void init() throws IOException {
        expense = new Expenses(new BookManager(new CategoryManager()));
        book = expense.getAll();
    }

    @Test
    public void checkQueryExact() throws JsonProcessingException {
        QueryBuilder queryBuilder = new QueryBuilder();

        queryBuilder = queryBuilder.queryExact("item", "chocolate chip cookie");

        QueryExecutor executor = new QueryExecutor(queryBuilder);
        System.out.println("The actual number of expenses that match the query is: " + executor.runQuery(book).size() + "\nThe expected is 1!");
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(queryBuilder));
        assertEquals(1, executor.runQuery(book).size());
    }

    @Test
    public void checkQueryRange(){

        QueryBuilder queryBuilder = new QueryBuilder();

        queryBuilder = queryBuilder.queryRange("date", new Range("20240930", "20250101"));

        QueryExecutor executor = new QueryExecutor(queryBuilder);
        System.out.println("The actual number of expenses that match the query is: " + executor.runQuery(book).size() + "\nThe expected is 4!");
        assertEquals(4, executor.runQuery(book).size());
    }

    @Test
    public void checkQueryLessThan(){
        QueryBuilder queryBuilder = new QueryBuilder();

        queryBuilder = queryBuilder.queryLessThan("date", "20241201");

        QueryExecutor executor = new QueryExecutor(queryBuilder);
        System.out.println("The actual number of expenses that match the query is: " + executor.runQuery(book).size() + "\nThe expected is 3!");
        assertEquals(3, executor.runQuery(book).size());
    }

    @Test
    public void checkQueryMoreThan(){
        QueryBuilder queryBuilder = new QueryBuilder();

        queryBuilder = queryBuilder.queryMoreThan("date", "20241201");

        QueryExecutor executor = new QueryExecutor(queryBuilder);
        System.out.println("The actual number of expenses that match the query is: " + executor.runQuery(book).size() + "\nThe expected is 2!");
        assertEquals(2, executor.runQuery(book).size());
    }

    @Test
    public void checkQueryNot(){
        QueryBuilder queryBuilder = new QueryBuilder();

        queryBuilder = queryBuilder.queryLessThan("spender", "Emily");

        QueryExecutor executor = new QueryExecutor(queryBuilder);
        System.out.println("The actual number of expenses that match the query is: " + executor.runQuery(book).size() + "\nThe expected is 0!");
        assertEquals(0, executor.runQuery(book).size());
    }

    @Test
    public void checkQueryMultiple(){
        QueryBuilder queryBuilder = new QueryBuilder();

        queryBuilder = queryBuilder.queryLessThan("date", "20251201");
        queryBuilder = queryBuilder.queryExact("type", "food");
        queryBuilder = queryBuilder.queryRange("price", new Range("0.00", "8.00"));


        QueryExecutor executor = new QueryExecutor(queryBuilder);
        System.out.println("The actual number of expenses that match the query is: " + executor.runQuery(book).size() + "\nThe expected is 2!");
        assertEquals(2, executor.runQuery(book).size());
    }

}