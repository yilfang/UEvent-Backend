package net.eventhub.rest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.sun.istack.logging.Logger;

@Component
@Path("/.well-known")
public class AppleAppSiteAssociation {

	@GET
    @Path("/apple-app-site-association")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addResourceHandlers() {
    	Logger log = Logger.getLogger(getClass());
        String json = "";
        InputStream inputStream = getClass().getResourceAsStream("apple-app-site-association.json");
		/*
		 * try(InputStream stream = inputStream) { json =
		 * StreamUtils.copyToString(stream, Charset.forName("UTF-8")); } catch
		 * (IOException ioe) { log.
		 * info("Apple app association could not be retrieved! iOS app will be impacted. Error: "
		 * + ioe.getMessage()); }
		 */
        return Response.ok().entity(inputStream).build();
    }
}
