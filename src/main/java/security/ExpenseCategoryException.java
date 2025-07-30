package security;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class ExpenseCategoryException extends Exception implements ExceptionMapper<ExpenseCategoryException> {

    private static Logger logger = LoggerFactory.getLogger(ExpenseCategoryException.class);
    /*private static String addCatError = "Could not add because category already exists!";
    private static String addParentError = "Could not add category because parent category does not exist";
    private static String editCatError = "Could not edit because the category you're trying to change to already exists within this set.";
    private static String editParentError = "Could not edit because the parent you're trying to change to does not exist within this set.";
    private static String removeCatError= "Could not remove because this category does not exist!";*/

    public ExpenseCategoryException(){
        super();
    }

    public ExpenseCategoryException(String string){
        super(string);
    }


    @Override
    public Response toResponse(ExpenseCategoryException ex) {
        String message = ex.getMessage();
        logger.info("captured exception");
        return Response.status(500)
                .entity(500 + ": " + message)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}
