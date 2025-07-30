package managers;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Book;
import models.Expense;
import models.SearchType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import queryExecutors.QueryBuilder;
import queryExecutors.QueryExecutor;
import services.BroadcastingService;

import java.io.*;
import java.time.Month;
import java.util.*;

public class BookManager {

    static Logger logger = LoggerFactory.getLogger(BookManager.class);
    private Book book;
    private String filePath;
    ObjectMapper objectMapper = new ObjectMapper();
    private CategoryManager catMan;

    public BookManager(CategoryManager catMan){
        String configFilePath = System.getProperty("config.file.path");
        Properties properties = new Properties();
        try (InputStream input = BookManager.class.getResourceAsStream(configFilePath)) {
            properties.load(input);
            this.catMan = catMan;
            this.filePath = properties.getProperty("book.filepath");
            this.book = loadBookFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BookManager(Book book){
        this.book = book;
    }

    public Book getBook(){
        return this.book;
    }

    public void setBook(Book book){
        this.book = book;
    }

    private Book loadBookFromFile() throws IOException {
        this.book = objectMapper.readValue(new File(this.filePath), Book.class);
        return this.book;
    }

    public List<Expense> runQuery(QueryBuilder queryBuilder){

        logger.info("aaa");
        QueryExecutor executor = new QueryExecutor(queryBuilder);
        return executor.runQuery(getBook());
    }

    public SearchType sendSearchTypes(String searchBy){
        SearchType st = new SearchType();
        st.sendTypes(searchBy);
        return st;
    }

    public List<Expense> runQuery2(String queryString) throws IOException {

        logger.info("bbb");
        logger.info(queryString);
        QueryBuilder queryBuilder = objectMapper.readValue(queryString, QueryBuilder.class);
        logger.info("queryBuild : {}", queryBuilder);
        QueryExecutor executor = new QueryExecutor(queryBuilder);
        return executor.runQuery(getBook());
    }

    public List<Expense> getMonthlyStatement(String year, String month){
        List<Expense> toReturn = new ArrayList<>();
        Month monthM = Month.valueOf(month.toUpperCase());
        int monthNumber = monthM.getValue();
        String minDateString = year + monthNumber + "00";
        String maxDateString = year + monthNumber + "31";

        int minDate = Integer.parseInt(minDateString);
        int maxDate = Integer.parseInt(maxDateString);

        for (Expense expense:this.book.getExpenses().values()){
            if(minDate < expense.date && expense.date <= maxDate){
                toReturn.add(expense);
            }
        }

        return toReturn;
    }

    public double getMonthlyTotal(String year, String month){
        double total = 0;
        List<Expense> monthlyExpenses = getMonthlyStatement(year, month);
        for (Expense expense:monthlyExpenses){
            total += expense.price;
        }

        return total;
    }


    public Boolean saveBook(){
        try{
            //objectMapper.writeValue(new File(this.filePath), this.list);
            objectMapper.writeValue(new File(this.filePath), this.book);
            BroadcastingService.BroadcasterEnumSingleton.INSTANCE.publishEvent("book updated");
            logger.info("Book saved successfully!");
            logger.info(this.book.toString());
            return true;
        } catch (IOException e) {
            logger.info("Could not save book", e);
            return false;
        }
    }

}
