package net.eventhub.rest.filter;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
	
	//private String CLIENT_ID = 
	//		"502161081219-7dhsrca0rcq7lop1ul5m4akohv88tmui.apps.googleusercontent.com";
	
	private String CLIENT_ID = 
			"343030781035-v6d27ui7pomurd2b71mlo18giojbkad2.apps.googleusercontent.com";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // Get the token header from the HTTP Authorization request header
        String token = 
            requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Check if the token is present
        if (token == null || token.isEmpty()) {
            throw new NotAuthorizedException("Token must be provided");
        }

        // Validate the token
        try {
			validateToken(token);
		} catch (GeneralSecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void validateToken(String token) throws GeneralSecurityException, IOException {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
            .Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Arrays.asList(CLIENT_ID))
                .build();

        GoogleIdToken idToken = verifier.verify(token);
        if (idToken != null) {
            Payload payload = idToken.getPayload();
            System.out.println("User ID: " + payload.getSubject());
        } else {
            throw new NotAuthorizedException("Invalid token.");
        }
    }
}
