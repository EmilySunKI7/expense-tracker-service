package services;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import managers.CategoryManager;
import models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import security.AdminAccess;
import security.ExpenseCategoryException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Path("/category")
public class CategoryService {

    CategoryManager manager;
    static Logger logger = LoggerFactory.getLogger(CategoryService.class);

    public CategoryService(){
        this.manager = new CategoryManager();
    }
    public CategoryService(CategoryManager manager){
        this.manager = manager;
    }


    @Path("getall")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Set<ExpenseCategory> getAll() throws IOException {
        /*ExpenseCategory cat = new ExpenseCategory("electronics");
        manager.addToSet(cat);
        manager.addToSet(cat);*/
        return manager.getSet();
    }

    @POST
    @AdminAccess
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    @Path("addcategory")
    public Set<ExpenseCategory> addCategory(CategoryRequests requests) throws ExpenseCategoryException {
        System.out.println("requests is: " + requests);
        List<ExpenseCategory> cats = requests.getCats();
        System.out.println(cats);
        for(ExpenseCategory item:cats){
            logger.info("item : {}", item);
            manager.addToSet(item);
        }
        return manager.getSet();
    }

    @POST
    @AdminAccess
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    @Path("addcategorysingle")
    public Set<ExpenseCategory> addCategorySingle(ExpenseCategory cat) throws ExpenseCategoryException {
        System.out.println("requests is: " + cat);
        manager.addToSet(cat);
        System.out.println(manager.getSet());
        return manager.getSet();
    }

    @POST
    @AdminAccess
    @Produces("application/json")
    @Path("addcategory2")
    public Set<ExpenseCategory> addCategory2(CategoryRequests requests) throws ExpenseCategoryException {
        List<ExpenseCategory> cats = requests.getCats();
        manager.addToSet2(cats);
        return manager.getSet();
    }


    @POST
    @AdminAccess
    @Produces("application/json")
    @Path("removecategory")
    public Set<ExpenseCategory> removeCategory(ExpenseCategory cat) throws Exception {
        ExpenseCategory toRemove = manager.findCatFromSet(cat.getCategory());
         return manager.removeFromSet(toRemove);
    }


    @POST
    @AdminAccess
    @Produces("application/json")
    @Path("editcategory")
    public Set<ExpenseCategory> editCategory(CategoryRequests requests) throws Exception {
        List<ExpenseCategory> catsList = requests.getCats();
        ExpenseCategory category = catsList.get(0);
        String newCat = catsList.get(1).getCategory();
        String newParent = catsList.get(1).getParent();
        manager.editCat(category, newCat, newParent);
        return manager.getSet();
    }


    @GET
    @Path("main")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Set<ExpenseCategory> loadMainCategories(){
        return manager.loadHighestLevelCats();
    }


    @GET
    @Path("{category}/subcategories")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Set<ExpenseCategory> loadSubcatsByCategory(@PathParam("category") String category) {
        ExpenseCategory cat = manager.findCatFromSet(category);
        return manager.loadSubCats(cat);
    }

    @GET
    @Path("{category}/findcategory")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public ExpenseCategory findCatFromSet(@PathParam("category") String category){
        return manager.findCatFromSet(category);
    }


    @POST
    @AdminAccess
    @Produces("application/json")
    @Path("save")
    public Boolean saveCats(CategoryRequests requests){
        List<ExpenseCategory> categories = requests.getCats();
        Set<ExpenseCategory> cats = new HashSet<>(categories);
        manager.setSet(cats);
        return manager.saveCats();
    }

    @POST
    @Path("savetest")
    public void saveCatsTest(){
        manager.saveCatstest();
    }

    @GET
    @Path("exceptionexample")
    public String exceptionExample() throws ExpenseCategoryException {
        throw new ExpenseCategoryException("exception thrown");
    }

}
