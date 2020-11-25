package com.usta.covidApp_ejb.beans.interfaces;

import java.util.List;

import javax.ejb.Remote;

/**
 * Interfaz de tipo bean que se encarga de
 * definir los metodos para realizar el crud.
 * @author rafaeltorres
 *@param <T> tipo de objeto que recibe
 */
@Remote
public interface IObjectQueryMysql<T> {

	/**
	 * metodo que se encarga de crear el objeto
	 * a persistir
	 * @param t
	 * @throws Exception
	 */
	public void create(T t) throws Exception;

	/**
	 * metodo encargado de buscar y listar todos los
	 * objetos de la consulta que se haga
	 * @return
	 * @throws Exception
	 */
	public List<T> findAll(Class<T> t) throws Exception;
	
	/**
	 * metodo de encontrar por el id el objeto buscado
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public T findById(Integer id, Class<T> object) throws Exception;
	
	/**
	 * metodo para realizar cualquier cambio o
	 * modificacion sobre un dato de un objeto
	 * @param t
	 * @throws Exception
	 */
	public void uptade(T t) throws Exception;
	
	/**
	 * elimina un objeto dependiendo el id que se solicite borrar
	 * @param id
	 * @throws Exception
	 */
	public void delete(Integer id, Class<T> object) throws Exception;
	
	/**
	 * Busca el usuario segun credenciales;
	 * @param user
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public T searchUser(String user, String password) throws Exception;
	
	/**
	 * recuperamos contraseña del usuario 
	 * @return
	 * @throws Exception
	 */
	public T requiredPass(String user) throws Exception;
	
	public T searchPersonByDocument(String documento) throws Exception;
}
