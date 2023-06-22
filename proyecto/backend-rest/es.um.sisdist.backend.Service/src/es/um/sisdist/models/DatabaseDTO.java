package es.um.sisdist.models;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;

@XmlRootElement
public class DatabaseDTO {
	private String id;
	private String name;
	private String url;
	private HashMap<String, Object> hashMap;


	public DatabaseDTO(String name) {
		this.name = name;
		this.hashMap = new HashMap<String, Object>();
	}

	
	
	public String getUrl() {
		return url;
	}



	public void setUrl(String url) {
		this.url = url;
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
	
}
