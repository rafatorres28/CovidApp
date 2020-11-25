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
import com.covidapp_postgres.model.LoginPostgres;
import com.usta.covidApp_ejb.beans.interfaces.IObjectQueryPostgres;

@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DaoObjectPostgresql<T> implements IObjectQueryPostgres<T> {

	@PersistenceContext(unitName = "covidApp_ejb_postgres")
	EntityManager postgresEntity;

	@Override
	public void create(T t) throws Exception {
		postgresEntity.persist(t);

	}

	@Override
	public List<T> findAll(Class<T> t) throws Exception {
		List<T> object = new ArrayList<T>();

		Query queryPostgres = postgresEntity.createNamedQuery(t.getSimpleName() + ".FIND_ALL");// Armo la consulta

		object = queryPostgres.getResultList();// trae los datos de la consulta
		return object;
	}

	@Override
	public T findById(Integer id, Class<T> object) throws Exception {
		T t = null;

		// opcion1
		// Query q =
		// postgresEntity.createNamedQuery(object.getSimpleName()+"FIND_BY_ID");
		// q.setParameter("id", id);

		// opcion2
		t = postgresEntity.find(object, id);

		return t;
	}

	@Override
	public void uptade(T t) throws Exception {
		postgresEntity.merge(t);

	}

	@Override
	public void delete(Integer id, Class<T> object) throws Exception {
		T t = findById(id, object);

		if (t != null) {
			postgresEntity.remove(t);
		}
		postgresEntity.remove(id);

	}

	@Override
	public T searchUser(String user, String password) throws Exception {
		T t = null;
		try {
			Query q = postgresEntity.createNamedQuery(LoginPostgres.FIND_USER_CREDENTIALS_POSTGRESQL);

			q.setParameter("us", user);
			q.setParameter("ps", password);

			t = (T) q.getSingleResult();

		} catch (Exception e) {
			t = null;
		}

		return t;
	}

	@Override
	public T requiredPass(String user) throws Exception {
		T t = null;

		Query q = postgresEntity.createNamedQuery(LoginPostgres.FIND_USER);
		q.setParameter("us", user);

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
		Query q = postgresEntity.createNamedQuery(Persona.FIND_BY_DOCUMENT);
		q.setParameter("doc", documento);
		try {
			t = (T) q.getSingleResult();
		} catch (Exception e) {
			t = null;
		}
		
		return t;
	}

}
