package es.um.sisdist.models;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.List;

@XmlRootElement
public class DatabaseDTO {
    private String id;
    private String usuario;
    private String name;
    private List<String> pares;
	public DatabaseDTO(String id, String usuario, String name, List<String> pares) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.name = name;
		this.pares = pares;
	}
	
	public DatabaseDTO()
    {
    }
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getPares() {
		return pares;
	}
	public void setPares(List<String> pares) {
		this.pares = pares;
	}
    
	
    
}
