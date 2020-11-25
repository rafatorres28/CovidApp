package com.usta.cmapp.control;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.event.FlowEvent;

import com.covidapp_mysql.model.Ciudade;
import com.covidapp_mysql.model.Empresa;
import com.covidapp_mysql.model.Enfermedade;
import com.covidapp_mysql.model.Persona;
import com.covidapp_mysql.model.Registro;
import com.covidapp_mysql.model.TipoDocumento;
import com.usta.cmapp.constants.EnumDataBase;
import com.usta.cmapp.constants.EnumRhBlood;
import com.usta.covidApp_ejb.service.ServicesDao;

@ManagedBean(name = "regis")
@SessionScoped
public class RegisterController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Properties properties;
	private Registro registerPerson;
	private String usLoged;
	private String userAccess;

	private Persona personRegis;
	private List<Enfermedade> listaEnfermedades;
	private List<Empresa> listaEmpresa;
	private String document;
	private boolean visiblePanel;
	private Float imc;
	private String resultaImc;

	@EJB
	private ServicesDao<Object> servicesDao;

	/**
	 * Constructor class
	 */
	public RegisterController() {
		properties = new Properties();
		userAccess = null;
		registerPerson = new Registro();
		listaEnfermedades = new ArrayList<Enfermedade>();
		listaEmpresa = new ArrayList<Empresa>();
		document = null;
		visiblePanel = false;
		imc = 0.0F;
		resultaImc = null;
		personRegis = new Persona();
		chargeProperties();
	}

	/**
	 * Init variables
	 */
	private void chargeProperties() {
		try {
			properties.load(RegisterController.class.getResourceAsStream("messages.properties"));
			userAccess = ((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
					.get(LoginController.USER_AUTENTICH)).toUpperCase();
			usLoged = ((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
					.get(LoginController.AUTH_KEY)).toUpperCase();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					properties.getProperty("errorGeneral"), properties.getProperty("errorCargaPropiedades")));
		}
	}

	/**
	 * Este metodo se inicializa tan pronto se carga la clase y despues que crea el
	 * constructor No recibe invocacion de ningun cliente, el cliente es el mismo
	 * servidor cuando la aplicacion es inicializada
	 */
	@PostConstruct
	public void chargeData() {
		try {
			List<Object> d = servicesDao.findAll(Enfermedade.class, EnumDataBase.MYSQL.getId());
			for (Object o : d) {
				listaEnfermedades.add((Enfermedade) o);
			}
			List<Object> c = servicesDao.findAll(Empresa.class, EnumDataBase.MYSQL.getId());
			for (Object o : c) {
				listaEmpresa.add((Empresa) o);
			}
		} catch (Exception e) {
			// TODO
		}
	}

	/**
	 * valida los registros que sean de caracter obligatorio y valida que la fecha
	 * de nacimiento sea menor a la dia de hoy
	 * 
	 * @return
	 */
	public void validatePerson() {
		try {
			if (document != null && !document.equals("") && (registerPerson.getIdEnfermedad() > 0)
					&& (registerPerson.getIdEmpresa() > 0)) {
				personRegis = (Persona) servicesDao.searchPersonByDocument(document, EnumDataBase.MYSQL.getId());
				if (personRegis != null && !personRegis.equals("")) {
					registerPerson.setIdPersona(personRegis.getIdPersona());
					visiblePanel = true;
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
							properties.getProperty(""), properties.getProperty("")));
				}

			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					properties.getProperty(""), properties.getProperty("")));
		}
	}

	public void regisInto() {

		try {
			if (personRegis.getPeso() > 0 && registerPerson.getTemperatura() > 0) {
				if (registerPerson.getTemperatura() < 38 && registerPerson.getTemperatura() > 35) {
					registerPerson.setIngreso(Byte.valueOf("0"));
					registerPerson.setFechaIngreso(new Date());
					registerPerson.setSintomas(Byte.valueOf("0"));
					servicesDao.create(registerPerson, EnumDataBase.MYSQL.getId());
					imc = Float.valueOf(personRegis.getPeso())
							/ Float.valueOf((float) Math.pow(Double.valueOf(personRegis.getEstatura()), 2));

					resultaImc = personRegis.getGenero().equals("male")
							? (imc > 25 ? "SOBREPESO".concat(imc.toString()) : "NORMAL".concat(imc.toString()))
							: (imc > 24 ? "SOBREPESO".concat(imc.toString()) : "NORMAL".concat(imc.toString()));
				}
			}
		} catch (Exception e) {

		}
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public Registro getRegisterPerson() {
		return registerPerson;
	}

	public void setRegisterPerson(Registro registerPerson) {
		this.registerPerson = registerPerson;
	}

	public String getUsLoged() {
		return usLoged;
	}

	public void setUsLoged(String usLoged) {
		this.usLoged = usLoged;
	}

	public String getUserAccess() {
		return userAccess;
	}

	public void setUserAccess(String userAccess) {
		this.userAccess = userAccess;
	}

	public Persona getPersonRegis() {
		return personRegis;
	}

	public void setPersonRegis(Persona personRegis) {
		this.personRegis = personRegis;
	}

	public List<Enfermedade> getListaEnfermedades() {
		return listaEnfermedades;
	}

	public void setListaEnfermedades(List<Enfermedade> listaEnfermedades) {
		this.listaEnfermedades = listaEnfermedades;
	}

	public List<Empresa> getListaEmpresa() {
		return listaEmpresa;
	}

	public void setListaEmpresa(List<Empresa> listaEmpresa) {
		this.listaEmpresa = listaEmpresa;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public boolean isVisiblePanel() {
		return visiblePanel;
	}

	public void setVisiblePanel(boolean visiblePanel) {
		this.visiblePanel = visiblePanel;
	}

	public Float getImc() {
		return imc;
	}

	public void setImc(Float imc) {
		this.imc = imc;
	}

	public String getResultaImc() {
		return resultaImc;
	}

	public void setResultaImc(String resultaImc) {
		this.resultaImc = resultaImc;
	}

	public ServicesDao<Object> getServicesDao() {
		return servicesDao;
	}

	public void setServicesDao(ServicesDao<Object> servicesDao) {
		this.servicesDao = servicesDao;
	}

}
