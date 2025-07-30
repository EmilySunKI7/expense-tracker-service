package managers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.CategoryRequests;
import models.ExpenseCategory;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CategoryManagerTest {

    @Test
    public void createManager(){
        System.setProperty("config.file.path", "/app.properties");
        CategoryManager manager = new CategoryManager();
        Set<ExpenseCategory> set = manager.getSet();
        assertEquals(1, set.size());
    }

    @Test
    public void testCategoryRequests() throws JsonProcessingException {
        String request = "{\"cats\": [{\"category\": \"accessories\", \"parent\": \"electronics\"}]}";
        ObjectMapper mapper = new ObjectMapper();
        CategoryRequests req = mapper.readValue(request, CategoryRequests.class);
        System.out.println(req);
        assertEquals(1, req.getCats().size());
    }

}