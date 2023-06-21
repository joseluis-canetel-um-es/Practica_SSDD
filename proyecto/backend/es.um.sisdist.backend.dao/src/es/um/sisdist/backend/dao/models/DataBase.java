package es.um.sisdist.backend.dao.models;

import java.util.HashMap;
import java.util.List;

/**
 * 
 * Clase usada para representar las propiedades de una base de datos
 */
public class DataBase {
    private String id;
	private String name;
	private String idUser; // relacionar el id de usuario con la db
	private HashMap<String, Object> hashMap;


	public DataBase(String name) {
		this.name = name;
		this.hashMap = new HashMap<String, Object>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<String, Object> getHashMap() {
		return hashMap;
	}

	public void setHashMap(HashMap<String, Object> datos) {
		hashMap.putAll(datos);
	}
	
	public void addPar(String key, Object value) {
		this.hashMap.put(key, value);
	}
	
	public void deletePar(Object key) {
		this.hashMap.remove(key);
	}

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	
	
}
