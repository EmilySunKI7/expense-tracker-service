import jakarta.validation.constraints.Null;
import org.junit.jupiter.api.Test;

public class JavaExceptionTest {
    @Test
    public void testException(){
        try{
            int[] arr = new int[5];
            arr = null;
            System.out.println("Hello World!");
        }
        catch (NullPointerException e){
            System.out.println("aaa");
        }
        finally{
            System.out.println("bbb");
        }

    }

}
