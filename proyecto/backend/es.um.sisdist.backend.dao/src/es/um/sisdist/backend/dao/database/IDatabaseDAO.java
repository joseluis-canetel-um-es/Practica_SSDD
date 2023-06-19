package es.um.sisdist.backend.dao.database;

import java.util.ArrayList;
import java.util.List;

import es.um.sisdist.backend.dao.models.DataBase;

/**
* A partir de la URL de la base de datos, se pueden añadir y eliminar
pares clave/valor a la base de datos, así como obtener listados de
valores y lanzar procesamientos map-reduce *
*/
public interface IDatabaseDAO {
	 public boolean insertDatabase(String db, String idUser, List<String> values);
	 public boolean deleteDatabase(String idUser, String databaseName);
	 public DataBase getDatabase(String databaseId);
	// public void addClaveValor(String db, String clave, String valor);
	 public boolean deleteClaveValor(String dbId, String clave);
	 public void getValues(); // listar valores de la bd
	 public ArrayList<DataBase> getDatabases(String userId);
}
