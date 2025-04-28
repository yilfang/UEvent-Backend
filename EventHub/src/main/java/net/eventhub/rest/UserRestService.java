package net.eventhub.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import javax.ws.rs.core.Response;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
//import javax.ws.rs.PUT;
//import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


import net.eventhub.domain.User;
import net.eventhub.domain.UserPushToken;
import net.eventhub.rest.filter.Secured;
import net.eventhub.service.EventBusiness;
import net.eventhub.service.UserBusiness;
import net.eventhub.service.UserBusinessObject;
import net.eventhub.service.UserPushTokenBusiness;
import net.eventhub.utils.EventHubUtils;
import net.eventhub.utils.NullAwareBeanUtilsBean;
import net.eventhub.utils.VOEntityConverter;
import net.eventhub.valueobject.UserPushTokenVO;

@Component
@Path("/users")
//@Secured
public class UserRestService {
    
    @Autowired
    @Qualifier(value="userBusiness")
    private UserBusiness userBusinessObj;
    
    @Autowired
    @Qualifier(value="userPushTokenBusiness")
    private UserPushTokenBusiness userPuseTokenBusiness;
    
 // http://localhost:8080/EventHub/rest/users/json/search/keyword
    @GET
    @Path("/json/search/{keyword}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchUserByKeyword(@PathParam("keyword") String keyword ) {
    	
    	if(keyword == null ) {
            return EventHubUtils.validationError();
        }		
    	
    	List<User> users = userBusinessObj.searchUserByKeyword(keyword);
    	
    	if(users == null) {
    		
            return EventHubUtils.emptyReturn();
        }
        
        return Response.ok().entity(users).build();
    }
 
 // http://localhost:8080/EventHub/rest/users/json/getByEmail/abc@edf.com
    @GET
    @Path("/json/getByEmail/{email}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByEmail(@PathParam("email") String email ) {
    	
    	if(email == null ) {
            return EventHubUtils.validationError();
        }
    	List<User> users = new ArrayList<>();  		
    	User aUser = userBusinessObj.getUserByEmail(email);
    	
    	if(aUser == null) {
            return EventHubUtils.emptyReturn();
        }
    	
        users.add(aUser);
        return Response.ok(users).build();
    }
 
    
    // Basic CRUD operations for User service
    
    // http://localhost:8080/EventHub/rest/users/
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)   
    public List<User> getAllUsers() {
        return userBusinessObj.getAllUsers();
    }
    
 // http://localhost:8080/EventHub/rest/users/get/1
    @GET
    @Path("/json/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id")int userId) {
    	User aUser = userBusinessObj.getUserById(userId);
    	if ( aUser == null ) {
    		return EventHubUtils.errorReturn();
    	}
    	
        return Response.ok().entity(aUser).build();
    }
    
    
 // http://localhost:8080/eventHub/rest/users/json/add
    @POST
    @Path("json/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User newUser) {
    	Integer newUserId = null;
    	if ( newUser != null )
    	{
    		newUserId = userBusinessObj.createUser(newUser);
    	}
        if ( newUserId == null )
        {
        	return EventHubUtils.errorReturn();
        }
        return Response.ok(newUserId).build();
    }
    
    // http://localhost:8080/EventHub/rest/users/json/update
    @PUT //
    @Path("/json/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(User updatingUser) {
    	User existingUser = userBusinessObj.getUserById(updatingUser.getId());
    	NullAwareBeanUtilsBean bean = new NullAwareBeanUtilsBean();
    			
    	try {
			bean.copyProperties(existingUser,updatingUser);
		} catch (IllegalAccessException | InvocationTargetException e) {
			
			e.printStackTrace();
			return EventHubUtils.errorReturn();
		} 
    	
    	try {
			userBusinessObj.updateUser(existingUser);
		} catch (Exception e) {
	
			e.printStackTrace();
			return EventHubUtils.errorReturn();
		}
        return Response.ok().entity(existingUser).build();
    }
    
    // http://localhost:8080/EventHub/rest/users/delete/1
    @DELETE
    @Path("delete/{id}")
    public Response deleteUser(@PathParam("id") int userId) throws Exception {
        try {
			userBusinessObj.deleteUser(userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return EventHubUtils.errorReturn();
		}
        return Response.ok("Success").build();
    }
    
 // http://localhost/EventHub/rest/users/pushToken/add
    @POST
    @Path("/pushToken/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPushToken(UserPushTokenVO token) {
    	
    	
    	if ( token != null )
    	{
    		token = userPuseTokenBusiness.addPushToken(token);
    	}
        if ( token == null )
        {
        	return Response.ok("The Push Token already exists.").build();
        }
        return Response.ok(token).build();
    }
    
 // http://localhost/EventHub/rest/users/getPushToken/1
    @GET
    @Path("/getPushToken/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserTokens(@PathParam("userId") Integer userId) {
    	Collection<UserPushTokenVO> tokens = null;
    	if ( userId != null )
    	{
    		tokens = userPuseTokenBusiness.getUserPushTokens(userId);
    	}
        if ( tokens == null )
        {
        	return EventHubUtils.errorReturn();
        }
        return Response.ok(tokens).build();
    }
    
 // http://localhost/EventHub/rest/users/getPushToken/listUser?userIds=1&userIds=2
    @GET
    @Path("/getPushToken/listUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListOfUsersTokens(@QueryParam("userIds") List<Integer> userIds) {
    	Collection<UserPushTokenVO> tokens = null;
    	if ( userIds != null && userIds.size() > 0 )
    	{
    		tokens = userPuseTokenBusiness.getListOfUsersPushTokens(userIds);
    	}
        if ( tokens == null )
        {
        	return EventHubUtils.errorReturn();
        }
        return Response.ok(tokens).build();
    }
 
 // https://localhost/EventHub/rest/users/updatePushToken/
    @PUT
    @Path("/updatePushToken")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUserToken(UserPushTokenVO token) {
    	UserPushTokenVO newToken = null;
    	if ( token != null  )
    	{
    		newToken = userPuseTokenBusiness.updatePushToken(token);
    	}
        if ( newToken == null )
        {
        	return EventHubUtils.errorReturn();
        }
        return Response.ok(newToken).build();
    }
    
 // https://localhost/EventHub/rest/users/deletePushToken
    @DELETE
    @Path("/deletePushToken")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUserToken(UserPushTokenVO token) {
    	Boolean success = false;
    	if ( token != null  )
    	{
    		String tokenString = token.getPushToken();
    		success = userPuseTokenBusiness.deletePushToken(tokenString);
    	}
		
		 if ( !success ) { 
			 return EventHubUtils.errorReturn(); 
		}
		 else
		 {
			 return Response.ok("Success").build();
		 }
		
        
    }
 
  
 

	public UserBusiness getUserBusinessObj() {
		return userBusinessObj;
	}

	public void setUserBusinessObj(UserBusiness userBusinessObj) {
		this.userBusinessObj = userBusinessObj;
	}

	public UserPushTokenBusiness getUserPuseTokenBusiness() {
		return userPuseTokenBusiness;
	}

	public void setUserPuseTokenBusiness(UserPushTokenBusiness userPuseTokenBusiness) {
		this.userPuseTokenBusiness = userPuseTokenBusiness;
	}
    
}

