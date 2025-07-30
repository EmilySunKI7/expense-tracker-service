package security;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Provider
public class ContentChecker implements ReaderInterceptor, WriterInterceptor {

    static Logger logger = LoggerFactory.getLogger(ContentChecker.class);

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext readerInterceptorContext) throws IOException, WebApplicationException {

        System.out.println("reached ContentChecker for reader");
        MultivaluedMap<String, String> map = readerInterceptorContext.getHeaders();
        for(Map.Entry<String, List<String>> entry : map.entrySet()){
            logger.info("{} : {}", entry.getKey(), entry.getValue().get(0));
        }
        InputStream is = readerInterceptorContext.getInputStream();
        //TODO THIS IS WHAT WAS MAKING THE REQUEST NULL!!!!!!!
        String text = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        InputStream stream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        System.out.println(text);
        readerInterceptorContext.setInputStream(stream);

        return readerInterceptorContext.proceed();
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext writerInterceptorContext) throws IOException, WebApplicationException {
        System.out.println("reached ContentChecker for writer");
        writerInterceptorContext.proceed();
    }
}
