package managers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonToObjConvertor {
    public <T> T convert(String name, String value, Class<T> classObj) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        T t = classObj.newInstance();
        Method m = classObj.getMethod("set" + name);
        m.invoke(t, value);

        return null;
    }
}
