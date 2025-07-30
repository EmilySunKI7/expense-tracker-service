package services;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/v2")
public class MyResource2 {

    @Path("items")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List getAll() {
        return List.of(new Item(1L, "Item1"), new Item(2L, "Item2"));
    }

    @Path("items2")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List getAll2() {
        return List.of(new Item(1L, "Item1"), new Item(2L, "Item2"));
    }

}
