package net.eventhub.test;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
 
import org.glassfish.jersey.client.ClientConfig;


//import net.eventhub.rest.Product;
import net.eventhub.domain.User;
 
public class TestClient {
    private static String baseURI = "http://localhost:8080/EventHub/rest/users";
 
    static WebTarget getWebTarget() {
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
         
        return client.target(baseURI);     
    }
     
    public static void main(String[] args) {
        testList(); 
        //testAdd(); 
        //testGet();
       
        //testUpdate();
        //testDelete();
    }
    
    static void testList() {
        WebTarget target = getWebTarget();
         
        String response = target.request().accept(MediaType.APPLICATION_JSON).get(String.class);
         
        System.out.println(response);      
    }
    
	/*
	 * static void testGet() { WebTarget target = getWebTarget(); String productId =
	 * "3"; Product product = target.path(productId)
	 * .request().accept(MediaType.APPLICATION_JSON) .get(Product.class);
	 * 
	 * System.out.println(product.toString()); }
	 */
    
    static void testAddUser() {
        WebTarget target = getWebTarget();
        User newUser = new User();
        
        Response response = target.request()
                .post(Entity.entity(newUser, MediaType.APPLICATION_JSON), Response.class);
         
        System.out.println(response.getLocation().toString());
    }
    
	/*
	 * static void testAdd() { WebTarget target = getWebTarget(); Product product =
	 * new Product("ZenFoneX", 888.88f); Response response = target.request()
	 * .post(Entity.entity(product, MediaType.APPLICATION_JSON), Response.class);
	 * 
	 * System.out.println(response.getLocation().toString()); }
	 */
    
	/*
	 * private static void testUpdate() { WebTarget target = getWebTarget(); Product
	 * product = new Product("ZenFoneX", 100f); String productId = "4"; Response
	 * response = target.path(productId).request() .put(Entity.entity(product,
	 * MediaType.APPLICATION_JSON), Response.class); System.out.println(response); }
	 */
    
    private static void testDelete() {
        WebTarget target = getWebTarget();
        String productId = "3";
        Response response = target.path(productId).request()
                .delete(Response.class);
        System.out.println(response);      
    }
 
}
