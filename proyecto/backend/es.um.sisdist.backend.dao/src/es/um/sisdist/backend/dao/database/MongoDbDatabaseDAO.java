package es.um.sisdist.backend.dao.database;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static java.util.Arrays.*;
import java.util.ArrayList;

import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;

import es.um.sisdist.backend.dao.models.DataBase;
import es.um.sisdist.backend.dao.models.User;
import es.um.sisdist.backend.dao.utils.Lazy;


public class MongoDbDatabaseDAO implements IDatabaseDAO {

	private Supplier<MongoCollection<DataBase>> collection;

	public MongoDbDatabaseDAO() {
		CodecProvider pojoCodecProvider = PojoCodecProvider.builder()
				.conventions(asList(Conventions.ANNOTATION_CONVENTION)).automatic(true).build();
		CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));

		String uri = "mongodb://root:root@" + Optional.ofNullable(System.getenv("MONGO_SERVER")).orElse("localhost")
				+ ":27017/ssdd?authSource=admin";

		collection = Lazy.lazily(() -> {
			MongoClient mongoClient = MongoClients.create(uri);
			MongoDatabase database = mongoClient
					.getDatabase(Optional.ofNullable(System.getenv("DB_NAME")).orElse("ssdd"))
					.withCodecRegistry(pojoCodecRegistry);
			return database.getCollection("databases", DataBase.class);
		});
	}

	// elimina una base de datos dado su NOMBRE
	@Override
	public boolean deleteDatabase(String databaseName, String userId) {
	    try {
	        collection.get().deleteOne(Filters.and(Filters.eq("name", databaseName), Filters.eq("idUser", userId)));
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}


	// retorna db insertada con anterioridad dado su NOMBRE
	// debe de modificarse para añadir el user ID

	@Override
	public Optional<DataBase> getDatabase(String userId, String databaseName) {
		try {
			Optional<DataBase> database = Optional.ofNullable(collection.get().find(Filters.and(Filters.eq("name", databaseName), Filters.eq("idUser", userId))).first());
			return database;
		} catch (Exception e) {
		}

		return null;
	}
/**
	// añade una nueva entrada clave valor al mapa de pares<k,v>
	@Override
	public void addClaveValor(String db, String clave, String valor) {
		try {
			DataBase database = getDatabase(db);
			if (database != null) {
				// Obtener el mapa de pares clave-valor de la base de datos
				HashMap<String, String> paresDb = database.getPares();
				paresDb.put(clave, valor);
				StringBuilder sb = new StringBuilder();

				for (Map.Entry<String, String> entry : paresDb.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					sb.append(key).append(":").append(value).append(",");
				}

				// Eliminar la última coma
				if (sb.length() > 0) {
					sb.deleteCharAt(sb.length() - 1);
				}

				// Crear el filtro para actualizar el documento
				Bson filter = Filters.eq("name", db);

				// Update para el campo pares con la cadena de pares actualizada
				Bson update = Updates.set("pares", sb.toString());

				// com.mongodb.client.result.UpdateResult result =
				// collection.get().updateOne(filter, update);
				collection.get().updateOne(filter, update);
			}
		} catch (Exception e) {
		}
	}
*/
	/** eliminar un par<k,v>
	@Override
	public void deleteClaveValor(String db, String clave) {
		try {
			// Obtener la instancia de la base de datos
			DataBase database = getDatabase(db);

			if (database != null) {
				// Obtener el mapa de pares clave-valor de la base de datos
				HashMap<String, String> pares = database.getPares();

				// Eliminar la clave especificada del mapa de pares
				pares.remove(clave);

				// Convertir el mapa a una representación de cadena
				StringBuilder sb = new StringBuilder();

				for (Map.Entry<String, String> entry : pares.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					sb.append(key).append(":").append(value).append(",");
				}

				// Eliminar la última coma
				if (sb.length() > 0) {
					sb.deleteCharAt(sb.length() - 1);
				}

				Bson filter = Filters.eq("_id", database.getId());

				Bson update = Updates.set("pares", sb.toString());

				collection.get().updateOne(filter, update);

			}
		} catch (Exception e) {
		}
	}
*/
	@Override
	public void getValues() {
		// Obtiene lista de todos los pares Clave,Valor de la base de datos

		// TODO Auto-generated method stub

	}
/**
	@Override
	public boolean insertDatabase(String db, String idUser) {
		try {
			DataBase database = new DataBase(db); // Crear objeto DataBase con el nombre db
			database.setId(UUID.randomUUID().toString()); // Generar un ID único para la base de datos
			database.setIdUser(idUser); // Asignar el ID del usuario asociado

			collection.get().insertOne(database);

			return true;
		} catch (Exception e) {
			return false;
		}
	}
*/
	
	@Override
	public boolean insertDatabase(String db, String idUser, HashMap<String, Object> datos, String url) {
	    try {
	        DataBase database = new DataBase(db); // Crear objeto DataBase con el nombre db
	        database.setId(UUID.randomUUID().toString()); // Generar un ID único para la base de datos
	        database.setHashMap(datos); // Asignar el map de pares clave-valor
	        database.setUrl(url); // Asignar la URL

	        InsertOneResult result = collection.get().insertOne(database);

	        return result.wasAcknowledged();
	        //collection.get().insertOne(database);
	        //return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}


	@Override
	public ArrayList<DataBase> getDatabases(String userId) {
	    try {
	        MongoCollection<DataBase> mongoCollection = collection.get(); // Obtener la colección de la base de datos


	        FindIterable<DataBase> queryResult = mongoCollection.find(eq("idUser", userId));
	        //ArrayList<DataBase> result = new ArrayList<>(queryResult.into(new ArrayList<>()));
	        ArrayList<DataBase> result = new ArrayList<>();
	        for (DataBase database : queryResult) {
	            result.add(database);
	        }
	        return result;
	    } catch (Exception e) {
	        // Manejar la excepción según sea necesario
	    }

	    return null;
	}

	@Override
	public boolean deleteClaveValor(String dbId, String clave) {
		// TODO Auto-generated method stub
		return false;
	}	
	

}
