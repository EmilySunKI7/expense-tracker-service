package queryExecutors;

import java.util.HashMap;
import java.util.Map;

public class QueryBuilder {

    Map<String, String> stuff;

    Map<String, Range> stuffRange;

    Map<String, String> stuffLessThan;

    Map<String, String> stuffMoreThan;

    Map<String, String> stuffNot;

    public QueryBuilder(){
        this.stuff = new HashMap<String, String>();
        this.stuffRange = new HashMap<String, Range>();
        this.stuffLessThan = new HashMap<String, String>();
        this.stuffMoreThan = new HashMap<String, String>();
        this.stuffNot = new HashMap<String, String>();
    }

    public Map<String, String> getStuff(){
        return this.stuff;
    }

    public QueryBuilder queryExact(String category, String detail)
    {
        this.stuff.putIfAbsent(category, detail);
        return this;
    }

    public Map<String, Range> getStuffRange(){
        return this.stuffRange;
    }

    public QueryBuilder queryRange(String category, Range details)
    {
        this.stuffRange.putIfAbsent(category, details);
        return this;
    }

    public Map<String, String> getStuffLessThan(){
        return this.stuffLessThan;
    }

    public QueryBuilder queryLessThan(String category, String detail)
    {
        this.stuffLessThan.putIfAbsent(category, detail);
        return this;
    }

    public Map<String, String> getStuffMoreThan(){
        return this.stuffMoreThan;
    }

    public QueryBuilder queryMoreThan(String category, String detail)
    {
        this.stuffMoreThan.putIfAbsent(category, detail);
        return this;
    }

    public Map<String, String> getStuffNot(){
        return this.stuffNot;
    }

    public QueryBuilder queryNot(String category, String detail)
    {
        this.stuffNot.putIfAbsent(category, detail);
        return this;
    }
}
