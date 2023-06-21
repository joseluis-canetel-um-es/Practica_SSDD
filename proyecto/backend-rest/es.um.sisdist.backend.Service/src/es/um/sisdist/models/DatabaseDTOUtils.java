package es.um.sisdist.models;

import java.util.ArrayList;
import java.util.List;

import es.um.sisdist.backend.dao.models.DataBase;

public class DatabaseDTOUtils {
	
	// get DataBase Object
	public static DataBase fromDTO(DatabaseDTO dto) {
		DataBase database = new DataBase(dto.getName());
		database.setId(dto.getId());
		database.setIdUser(dto.getIdUser());
		database.setHashMap(dto.getHashMap());
		return database;
	}

	// get DatabaseDTO
	public static DatabaseDTO toDTO(DataBase database) {
		DatabaseDTO dto = new DatabaseDTO(database.getName());
		dto.setId(database.getId());
		dto.setIdUser(database.getIdUser());
		dto.setHashMap(database.getHashMap());
		return dto;
	}

}
