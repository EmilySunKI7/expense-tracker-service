package security;


import jakarta.ws.rs.container.DynamicFeature;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;
import services.CategoryService;
import services.Expenses;
import java.lang.annotation.Annotation;


public class MyDynamicFeature implements DynamicFeature, Feature {


    @Override
    public boolean configure(FeatureContext featureContext) {
        boolean enabled = featureContext.getConfiguration().getRuntimeType() != null;
        if (enabled) {
            featureContext.register(ContentChecker.class);
        }

        return enabled;
    }

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext featureContext) {

        boolean enabled = featureContext.getConfiguration().getRuntimeType() != null;
        if (
        (CategoryService.class.equals(resourceInfo.getResourceClass())
                &&
                (resourceInfo.getResourceMethod().getName().contains("addCategory") ||
                        resourceInfo.getResourceMethod().getName().contains("getAll")) )
        || (Expenses.class.equals(resourceInfo.getResourceClass())
                && resourceInfo.getResourceMethod().getName().contains("saveBook")))
        {
            featureContext.register(ContentChecker.class);
        }

        ;

        if(resourceInfo.getResourceMethod().isAnnotationPresent(AdminAccess.class)){
            featureContext.register(TestFilter.class);
        }


    }



}
