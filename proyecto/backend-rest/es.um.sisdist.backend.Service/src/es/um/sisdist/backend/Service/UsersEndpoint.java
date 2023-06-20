package es.um.sisdist.backend.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Logger;

import es.um.sisdist.backend.Service.impl.AppLogicImpl;
import es.um.sisdist.backend.dao.models.DataBase;
import es.um.sisdist.models.DatabaseDTO;
import es.um.sisdist.models.DatabaseDTOUtils;
import es.um.sisdist.models.UserDTO;
import es.um.sisdist.models.UserDTOUtils;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/u")
public class UsersEndpoint
{
    private static final Logger logger = Logger.getLogger(AppLogicImpl.class.getName());

    private AppLogicImpl impl = AppLogicImpl.getInstance();
    /** punto de entrada para una solicitud GET a la ruta "/u/{username}",
     * donde "{username}" es un parámetro de ruta que representa el nombre de usuario 
     del usuario del que se desea obtener información.
    */
    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserDTO getUserInfo(@PathParam("username") String username)
    {
        return UserDTOUtils.toDTO(impl.getUserByEmail(username).orElse(null));
    }
    
    /**
    // obtener las bases de datos de un usuario dado su ID
    @GET
    @Path("/{id}/db")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDatabasesUser(@PathParam ("id") String userID) {
    	ArrayList<DataBase> databases = impl.getDatabasesByUserId(userID);
    	if(databases != null) {
    		ArrayList<DatabaseDTO> databasesDTO = new ArrayList<DatabaseDTO>();
    		for( DataBase db : databases ) {
    			databasesDTO.add( DatabaseDTOUtils.toDTO(db) );
    		}		
    		return Response.ok(databasesDTO).build();  
    	}else {
    		// contenido vacio
    		return Response.status(Status.NO_CONTENT).build(); 

    	}
    }
    */
    
 	// metodo para que el usuario pueda crear bases de datos
 	@POST
    @Path("/{id}/db")
 	@Consumes(MediaType.APPLICATION_JSON)
 	public Response createDatabase(@PathParam("id") String userId, DatabaseDTO databaseDTO) {
 		// obtener los datos iniciales de databaseRequest.getD()
 		logger.info("HE RECIBIDO TU SOLICITUD DE CREAR BASE");
 		logger.info("HE RECIBIDO TU NOMBRE");
 		logger.info(databaseDTO.getName());
 		logger.info("HE RECIBIDO TUS CLAVES");
 		logger.info(databaseDTO.getPares().toString());
 		// crear la base de datos
 		// impl.createDatabase(databaseDTO.getName(), userId, databaseDTO.getPares());
 		// Construye la URL de la base de datos
 		String databaseUrl = "/u/" + userId + "/db/" + databaseDTO.getName();
 		// Construye la respuesta con el código HTTP 201 Created y la cabecera Location
 		return Response.status(Response.Status.CREATED).header("Location", databaseUrl).build();
 	}
 	
 	/**
 	// metodo consulta de bases de datos
 	@GET
     @Path("/{id}/db/{name}")
     public Response getDatabase(@PathParam("id") String userId, @PathParam("name") String databaseName) {
         impl.getDatabase(databaseName);

         return Response.ok().build();
     }
 	
 	
 	@DELETE
 	@Path("/{dbName}")
 	public Response deleteDatabase(@PathParam("id") String userId, @PathParam("dbName") String databaseName) {
 	    // Verificar si la base de datos existe y pertenece al usuario
 	    //if (impl.isDatabaseOwnedByUser(databaseName, userId)) {
 	        // Eliminar la base de datos
 	        boolean deleted = impl.deleteDatabase(userId, databaseName);

 	        if (deleted) {
 	            return Response.ok().build(); // Respuesta HTTP 200 OK si se eliminó correctamente
 	        } else {
 	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); // Respuesta HTTP 500 Internal Server Error si hubo un error al eliminar
 	        }
 	    
 	}
 	
 	// Eliminar un par clave/valor de la base de datos
 	@DELETE
 	@Path("/{id}/db/{dbid}/d/{key}")
 	public Response deleteKeyValue(@PathParam("id") String userId, @PathParam("dbid") String databaseId,@PathParam("key") String clave) {

		boolean deleted = impl.deleteKeyValue(userId, databaseId, clave);

 		if (deleted) {
			return Response.ok().build(); // Respuesta HTTP 200 OK si se eliminó correctamente
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); // Respuesta HTTP 500 Internal Server
		}
 	    
 	}*/
}
