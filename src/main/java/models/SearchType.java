package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "name", "item", "type", "spender", "price", "id" })
public class SearchType {

    private static final String[] name = { "Equal", "Range", "Less Than", "More Than", "Not" } ;
    private static final String[] item = { "Equal", "Range", "Less Than", "More Than", "Not" };
    private static final String[] type = { "Equal", "Not" };
    private static final String[] spender = { "Equal", "Range", "Less Than", "More Than", "Not" };
    private static final String[] price = { "Equal", "Range", "Less Than", "More Than", "Not" };
    private static final String[] id = { "Equal", "Range", "Less Than", "More Than", "Not" };

    private String[] sentTypes;

    public SearchType(){}

    public String[] getSentTypes() {
        return sentTypes;
    }

    public void setSentTypes(String[] sentTypes) {
        this.sentTypes = sentTypes;
    }

    public String[] sendTypes(String searchBy){
        switch (searchBy){
            case "name":
                sentTypes = name;
                break;
            case "item":
                sentTypes = item;
                break;
            case "type":
                sentTypes = type;
                break;
            case "spender":
                sentTypes = spender;
                break;
            case "price":
                sentTypes = price;
                break;
            case "id":
                sentTypes = id;
                break;
        }
        /*if(searchBy.equals("type")){
            sentTypes = new String[] {"Equal", "Not" };
        }
        else{
            sentTypes = new String[] {"Equal", "Range", "Less Than", "More Than", "Not" };
        }*/
        return sentTypes;
    }

}
