package es.um.sisdist.models;

import es.um.sisdist.backend.dao.models.DataBase;

public class DatabaseDTOUtils {
	
	// get DataBase Object
	public static DataBase fromDTO(DatabaseDTO dto) {
		DataBase database = new DataBase(dto.getName());
		database.setId(dto.getId());
		database.setHashMap(dto.getHashMap());
		database.setUrl(dto.getUrl());
		return database;
	}

	// get DatabaseDTO
	public static DatabaseDTO toDTO(DataBase database) {
		DatabaseDTO dto = new DatabaseDTO(database.getName());
		dto.setId(database.getId());
		dto.setHashMap(database.getHashMap());
		dto.setUrl(database.getUrl());
		return dto;
	}

}
