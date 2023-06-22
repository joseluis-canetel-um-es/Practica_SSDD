package es.um.sisdist.backend.Service;

import java.util.HashMap;
import java.util.Map;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/*actua como punto de entrada para la configuración de la aplicación RESTful.*/
@ApplicationPath("/")
public class ServiceApp extends Application
{
	// configurar características adicionales de JAX-RS
	/*
		 @Override
		    public Map<String, Object> getProperties() {
		        Map<String, Object> properties = new HashMap<>();
		        properties.put("jersey.config.jsonFeature", "true");
		        properties.put("jersey.config.server.provider.classnames",  "org.glassfish.jersey.media.multipart.MultiPartFeature");
		       // properties.put("jersey.config.server.response.encoding", "UTF-8");
		        return properties;
		    }
	*/
}
