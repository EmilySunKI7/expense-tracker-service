package managers;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.CategoryRequests;
import models.ExpenseCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import security.ExpenseCategoryException;
import services.BroadcastingService;

import java.io.*;
import java.util.*;

@JsonIgnoreProperties(value = { "subCats" })
public class CategoryManager {

    static Logger logger = LoggerFactory.getLogger(CategoryManager.class);
    //static Logger logger = LoggerFactory.getLogger("");

    //private List<ExpenseCategory> list;
    private Set<ExpenseCategory> set;
    private String filePath;

    ObjectMapper objectMapper = new ObjectMapper();

    public CategoryManager() {
        String configFilePath = System.getProperty("config.file.path");
        Properties properties = new Properties();
        try (InputStream input = CategoryManager.class.getResourceAsStream(configFilePath)) {
            properties.load(input);
            this.filePath = properties.getProperty("category.filepath");
            this.set = loadSetFromFile();
            for(ExpenseCategory category:set){
                loadSubCats(category);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CategoryManager(Set<ExpenseCategory> set) {
        this.set = set;
    }

    public Set<ExpenseCategory> getSet() {
        return this.set;
    }

    public void setSet(Set<ExpenseCategory> set) {
        this.set = set;
    }

    public synchronized Set<ExpenseCategory> addToSet(ExpenseCategory category) throws ExpenseCategoryException {
        if(this.set.contains(category)){
            logger.info("This category already exists!");
            throw new ExpenseCategoryException("Could not add " + category + " because category already exists!");
        }

        else if(!parentInSet(category)){
            logger.info("Could not add category because parent category does not exist");
            throw new ExpenseCategoryException("Could not add  " + category + " because parent category does not exist");
        }

        else{
            /*try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }*/
            this.set.add(category);
            addToChildren(category);
        }
        return this.set;
    }

    public synchronized Set<ExpenseCategory> addToSet2(List<ExpenseCategory> cats) throws ExpenseCategoryException {
        List<ExpenseCategory> nameList = new ArrayList<>();
        List<ExpenseCategory> parentList = new ArrayList<>();

        for(ExpenseCategory category:cats) {
            if (this.set.contains(category)) {
                nameList.add(category);
            } else if (!parentInSet(category)) {
                parentList.add(category);
            } else {
                String cate = category.getCategory().trim();
                String parent = category.getParent().trim();
                ExpenseCategory newCat = new ExpenseCategory(cate, parent);
                this.set.add(newCat);
                addToChildren(newCat);
            }
        }

        if(!nameList.isEmpty() && parentList.isEmpty()){
            CategoryRequests nameError = new CategoryRequests(nameList);
            logger.info("This category already exists!");
            throw new ExpenseCategoryException("Could not add " + nameError + " because category already exists!");
        }
        else if(nameList.isEmpty() && !parentList.isEmpty()){
            CategoryRequests parentError = new CategoryRequests(parentList);
            logger.info("Could not add category because parent category does not exist");
            throw new ExpenseCategoryException("Could not add  " + parentError + " because parent category does not exist");
        }
        else if(!nameList.isEmpty() && !parentList.isEmpty()){
            CategoryRequests nameError = new CategoryRequests(nameList);
            CategoryRequests parentError = new CategoryRequests(parentList);
            logger.info("This category already exists! Could not add category because parent category does not exist");
            throw new ExpenseCategoryException("Could not add " + nameError + " because category already exists! \n Could not add " + parentError + " because parent category does not exist");
        }
        return this.set;
    }

    public ExpenseCategory findCatFromSet(String category){
        for(ExpenseCategory cat:set){
            if (cat.getCategory().toLowerCase().replaceAll("\\s", "").equals(category.toLowerCase().replaceAll("\\s", ""))){
                return cat;
            }
        }
        return null;
    }

    public boolean parentInSet(ExpenseCategory category){
        if(category.getParent() == null){
            return true;
        }
        ExpenseCategory cat = new ExpenseCategory(category.getParent());
        return this.set.contains(cat);
        //return findCatFromSet(category.getParent()) != null;
    }

    public synchronized Set<ExpenseCategory> removeFromSet(ExpenseCategory category) throws Exception{
        if(this.set.remove(category)){
            logger.info("Category {} has been removed", category.toString2());
            if(category.getChildren() != null){
                for(ExpenseCategory cat:category.getChildren()){
                    removeFromSet(cat);
                    //remove all subcategories of category since subcats cannot exist without the parent category
                }
            }
        }
        else{
            logger.info("This category does not exist!");
            throw new ExpenseCategoryException("Could not remove because this category does not exist!");
        }
        logger.info("Process finished");
        return this.set;
    }

    public ExpenseCategory findParent(ExpenseCategory category){
        if(category.getParent() == null){
            return null;
        }
        for(ExpenseCategory cat:set){
            if (cat.isParentOf(category)){
                return cat;
            }
        }
        return null;
    }

    public Set<ExpenseCategory> addToChildren(ExpenseCategory child){
        ExpenseCategory parent = findParent(child);
        if(parent == null){
            return null;
        }
        return parent.addChild(child);
    }

    public Set<ExpenseCategory> removeFromChildren(ExpenseCategory child){
        ExpenseCategory parent = findParent(child);
        if(parent == null){
            return null;
        }
        return parent.removeChild(child);
    }

    public Set<ExpenseCategory> loadHighestLevelCats(){
        Set<ExpenseCategory> cats = new HashSet<ExpenseCategory>();
        for(ExpenseCategory cat:set){
            if (cat.getParent() == null){
                cats.add(cat);
            }
        }
        return cats;
    }

    public Set<ExpenseCategory> loadSubCats(ExpenseCategory category){
        Set<ExpenseCategory> subCats = new HashSet<ExpenseCategory>();
        for(ExpenseCategory cat:set){
            if (category.isParentOf(cat)){
                subCats.add(cat);
            }
        }
        category.setChildren(subCats);
        return subCats;
    }


    public synchronized Set<ExpenseCategory> editCat(ExpenseCategory category, String newCat, String newParent) throws Exception {
        ExpenseCategory toEdit = findCatFromSet(category.getCategory());
        Set<ExpenseCategory> prevChildren = toEdit.getChildren();
        newCat = newCat.trim();
        newParent = newParent.trim();

        if(newCat.isEmpty()){
            newCat = null;
        }
        if(newParent.isEmpty()){
            newParent = null;
        }

        if(toEdit.getParent() == null && newParent != null || toEdit.getParent() != null && !toEdit.getParent().equals(newParent)) //if there is a change in the parent
        {
            if(findCatFromSet(newParent) != null){ //parent exists
                removeFromChildren(toEdit);
                toEdit.setParent(newParent);
                addToChildren(toEdit);
            }
            else{
                logger.info("Careful! The parent you're trying to change to does not exist within this set.");
                throw new ExpenseCategoryException("Could not edit because the parent you're trying to change to does not exist within this set.");
            }
        }

        if(newCat != null && !toEdit.getCategory().equals(newCat)) //if there is a change in the category name
        {
            if(findCatFromSet(newCat) == null){//newCat is unique and/or doesn't already exist
                if(prevChildren != null){
                    for(ExpenseCategory child : prevChildren){
                        child.setParent(newCat);
                    }
                }
                ExpenseCategory newExpCat = new ExpenseCategory(newCat, newParent);
                this.set.add(newExpCat);
                newExpCat.setChildren(prevChildren);
                addToChildren(newExpCat);
                this.set.remove(toEdit);
            }
            else{
                logger.info("Careful! The category you're trying to change to already exists within this set.");
                throw new ExpenseCategoryException("Could not edit because the category you're trying to change to already exists within this set.");
            }
        }
        //logger.info(this.set.toString());
        return this.set;
    }


    public boolean isEqualsOrSubcatOf(String cat, String searchInput){
        if(cat.equals(searchInput)){
            return true;
        }
        ExpenseCategory category = findCatFromSet(cat);
        ExpenseCategory temp = findParent(category);
        while(temp != null){
            if(temp.getCategory().equals(searchInput)){
                return true;
            }
            temp = findParent(temp);
        }
        return false;
    }


    private Set<ExpenseCategory> loadSetFromFile() throws IOException {
        /*TypeReference<ArrayList<ExpenseCategory>> typeInstance = new CategoryListTypeReference();
        this.list = objectMapper.readValue(new File(filePath), typeInstance);
        this.list = objectMapper.readValue(new File(this.filePath), new TypeReference<ArrayList<ExpenseCategory>>(){});*/
        this.set = objectMapper.readValue(new File(this.filePath), new TypeReference<HashSet<ExpenseCategory>>(){});
        return this.set;
    }

    public static class CategoryListTypeReference extends TypeReference<ArrayList<ExpenseCategory>>{}

    public Boolean saveCats(){
        try{
            //objectMapper.writeValue(new File(this.filePath), this.list);
            objectMapper.writeValue(new File(this.filePath), this.set);
            BroadcastingService.BroadcasterEnumSingleton.INSTANCE.publishEvent("categories updated");
            logger.info("Categories saved successfully!");
            return true;
        } catch (IOException e) {
            logger.info("Could not save categories", e);
            return false;
        }
    }

    public void saveCatstest(){
        BroadcastingService.BroadcasterEnumSingleton.INSTANCE.publishEvent("categories updated");
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(ExpenseCategory cat:set){
            sb.append(cat.toString());
        }
        return sb.toString();
    }


}
