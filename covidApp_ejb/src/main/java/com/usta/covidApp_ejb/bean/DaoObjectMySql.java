package com.usta.covidApp_ejb.bean;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.covidapp_mysql.model.Login;
import com.covidapp_mysql.model.Persona;
import com.usta.covidApp_ejb.beans.interfaces.IObjectQueryMysql;

@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DaoObjectMySql<T> implements IObjectQueryMysql<T>{
	
	@PersistenceContext(unitName = "covidApp_ejb_mysql")//Aqui defino con que motor de bases de datos voy a trabajar
	EntityManager mysqlEntity; //define los metodos que permite las transacciones 
	
	@Override
	public void create(T t) throws Exception {
		mysqlEntity.persist(t);
	}

	@Override
	public List<T> findAll(Class<T> t) throws Exception {
		List<T> object = new ArrayList<T>();
		
		Query querySql = mysqlEntity.createNamedQuery(t.getSimpleName()+".FIND_ALL");//Armo la consulta
		
		object = querySql.getResultList();//trae los datos de la consulta
		return object;
	}

	@Override
	public T findById(Integer id, Class<T> object) throws Exception {
		T t = null;
		
		t = (T) mysqlEntity.find(Object.class, id);
		return t;
	}

	@Override
	public void uptade(T t) throws Exception {
		mysqlEntity.merge(t);
		
	}

	@Override
	public void delete(Integer id, Class<T> object) throws Exception {
		T t = findById(id, object);
		
		if(t!=null) {
			mysqlEntity.remove(t);
		}
		mysqlEntity.remove(id);
		
	}

	@Override
	public T searchUser(String user, String password) throws Exception {
		T t = null;
		
		Query q = mysqlEntity.createNamedQuery(Login.FIND_USER_CREDENTIALS_MYSQL);
		q.setParameter("us", user);
		q.setParameter("ps", password);
		
		try {
			t = (T) q.getSingleResult();
		} catch (NoResultException e) {
			t = null;
		}
		
		return t;
	}

	@Override
	public T requiredPass(String user) throws Exception {
		T t = null;
		
		Query q = mysqlEntity.createNamedQuery(Login.FIND_USER);
		q.setParameter("user", user);
		
		try {
			t = (T) q.getSingleResult();
		} catch (NoResultException e) {
			t = null;
		}
		
		
		return t;
	}
	@Override
	public T searchPersonByDocument(String documento) throws Exception {
		T t = null;
		Query q = mysqlEntity.createNamedQuery(Persona.FIND_BY_DOCUMENT);
		q.setParameter("doc", documento);
		try {
			t = (T) q.getSingleResult();
		} catch (Exception e) {
			t = null;
		}
		
		return t;
	}

}
