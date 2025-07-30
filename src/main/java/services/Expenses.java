package services;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Request;
import managers.BookManager;
import models.Book;
import models.Expense;
import models.H2Database;
import models.SearchType;
import queryExecutors.QueryBuilder;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Path("/expenses")
public class Expenses {

    ObjectMapper objectMapper;
    BookManager manager;

    public Expenses(BookManager manager){
        this.objectMapper = new ObjectMapper();
        this.manager = manager;
    }


    @Path("getall")
    @GET
    @Produces({MediaType.APPLICATION_JSON, })
    public Book getAll() throws IOException {
/*        Map<String, Map<Integer, Expense>> map = objectMapper.readValue(new File("C:/Users/emily/source/repos/Home_Expense_Data.json"),
                new TypeReference<Map<String, Map<Integer, Expense>>>(){});*/

        return manager.getBook();
    }

//    @Path("getall2")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    TODO for some reason this is an array????
//    public Map<String, Map<Integer, Expense>> getAll2() throws IOException {
//        Map<String, Map<Integer, Expense>> toReturn = objectMapper.readValue(new File("C:/Users/emily/source/repos/Home_Expense_Data_Test.json"), new TypeReference<Map<String, Map<Integer, Expense>>>(){});
//        return toReturn;
//    }


    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("{searchby}/getsearchtypes")
    public SearchType getSearchTypes(@PathParam("searchby") String searchBy/*, @Context Request context*/){
        SearchType st = manager.sendSearchTypes(searchBy);
        return st;
    }


    @POST
    @Produces("application/json")
    @Path("runquery")
    public List<Expense> runQuery(QueryBuilder queryBuilder){

        return manager.runQuery(queryBuilder);
    }
/*    curl --header "Content-Type: application/json" \
            --request POST \
            --data '{"username":"xyz","password":"xyz"}' \
    http://localhost:3000/api/login
    */

    @POST
    @Produces("application/json")
    @Path("runquery2")
    public List<Expense> runQuery2(String queryString) throws IOException {

        return manager.runQuery2(queryString);
    }

    @GET
    @Produces("application/json")
    @Path("monthlystatement/{year}/{month}")
    public List<Expense> getMonthlyStatement(@PathParam("year") String year, @PathParam("month") String month){
        return manager.getMonthlyStatement(year, month);
    }

    @GET
    @Produces("application/json")
    @Path("monthlytotal/{year}/{month}")
    public double getMonthlyTotal(@PathParam("year") String year, @PathParam("month") String month){
        return manager.getMonthlyTotal(year, month);
    }


    @POST
    @Produces("application/json")
    @Path("save")
    public Boolean saveBook(Book book) {
        manager.setBook(book);
        return manager.saveBook();
    }

    @GET
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Expense getAnExpenseById(@PathParam("id") int id) throws IOException {
        Map<String, Map<Integer, Expense>> map = objectMapper.readValue(new File("C:/Users/emily/source/repos/Home_Expense_Data.json"),
                new TypeReference<Map<String, Map<Integer, Expense>>>(){});

        Map<Integer, Expense> values = map.get("expenses");

        return values.get((Integer) id);
    }

    @GET
    @Path("/price/{price}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Expense getAnExpenseByprice(@PathParam("price") double price) {
        return new Expense(20250203, "cookie", "food","Emily", 0.00, 10);
    }

    @GET
    @Path("/category")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Expense getAnExpenseByCategory(@PathParam("category") String category) {
        return new Expense(20250203, "cookie", "food","Emily", 0.00, 10);
    }

    @Path("getdbtimestamp")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getDBTimestamp() {
        H2Database database = new H2Database();
        return database.getTimeStamp();
    }

}
