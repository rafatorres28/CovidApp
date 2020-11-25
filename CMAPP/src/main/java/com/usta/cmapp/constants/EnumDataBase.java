package com.usta.cmapp.constants;

/**
 * contiene valores constantes que jamas van a cambiar en la aplicacion 
 * @author GhostKillerBC
 *
 */
public enum EnumDataBase {

	MYSQL(1,"MySql"),
	POSTGRESQL(2,"Postgresql");
	
	
	private int id;
	
	private String description;

	private EnumDataBase(int id, String description) {
		this.id = id;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}
	
	
	
}
