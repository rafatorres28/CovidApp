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
import com.covidapp_mysql.model.Persona;
import com.covidapp_mysql.model.TipoDocumento;
import com.usta.cmapp.constants.EnumDataBase;
import com.usta.cmapp.constants.EnumRhBlood;
import com.usta.covidApp_ejb.service.ServicesDao;

@ManagedBean(name = "principal")
@SessionScoped 
public class PrincipalController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Properties properties;
	private String usLoged;
	private String userAccess;
	private final static String PAGE_PRINCIPAL = "../login/login.faces";
	
	private Persona newPerson;
	private List<TipoDocumento> typesDocuments;
	private List<Ciudade> listCities;
	private List<EnumRhBlood> rhlist;
	private TipoDocumento documentType;
	private Ciudade city;
	private String phone;
	
	@EJB
	private ServicesDao<Object> servicesDao;
	
	/**
	 * Constructor class
	 */
	public PrincipalController() {
		try {
			properties = new Properties();
			userAccess = null;
			newPerson = new Persona();
			city = new Ciudade();
			typesDocuments = new ArrayList<TipoDocumento>();
			listCities = new ArrayList<Ciudade>();
			rhlist = new ArrayList<EnumRhBlood>();
			documentType = new TipoDocumento();
			chargeProperties();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Init variables
	 */
	private void chargeProperties() {
		try {
			properties.load(PrincipalController.class.getResourceAsStream("messages.properties"));
			userAccess = ((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(LoginController.USER_AUTENTICH)).toUpperCase();
			usLoged = ((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(LoginController.AUTH_KEY)).toUpperCase();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, properties.getProperty("errorGeneral"), properties.getProperty("errorCargaPropiedades")));
		}
	}
	/**
	 * Este metodo se inicializa tan pronto  se carga la clase
	 * y despues que crea el constructor
	 * No recibe invocacion de ningun cliente, el cliente
	 * es el mismo servidor cuando la aplicacion es 
	 * inicializada
	 */
	@PostConstruct
	public void chargeData() {
		try {
			List<Object> d = servicesDao.findAll(TipoDocumento.class,EnumDataBase.MYSQL.getId());
			for (Object o : d) {
				typesDocuments.add((TipoDocumento) o);
			}
			List<Object> c = servicesDao.findAll(Ciudade.class,EnumDataBase.MYSQL.getId());
			for (Object o : c) {
			 listCities.add((Ciudade) o);
			}
			rhlist.add(EnumRhBlood.ABNEG);
			rhlist.add(EnumRhBlood.ANEG);
			rhlist.add(EnumRhBlood.APOS);
			rhlist.add(EnumRhBlood.ABPOS);
			rhlist.add(EnumRhBlood.BNEGA);
			rhlist.add(EnumRhBlood.BPOS);
			rhlist.add(EnumRhBlood.ONGE);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, properties.getProperty("errorGeneral"), properties.getProperty("errorCargaPropiedades")));
			
		}
	}
	
	/**
	 * valida para hacer el paso a la siguiente pestania
	 * @param event
	 * @return
	 */
	public String onFlowProcess(FlowEvent event) {
		String result = null;
		
		if(validateDataPerson()) {
			result = event.getNewStep();
		}else {
			result = event.getOldStep();
		}
		
		return result;
	}
	
	/**
	 * valida los registros que sean de caracter obligatorio y valida que la fecha
	 * de nacimiento sea menor a la dia de hoy 
	 * @return
	 */
	private boolean validateDataPerson() {
		boolean flag = false;
		if((newPerson.getDocumento()!=null && !newPerson.getDocumento().equals(""))
				&& (newPerson.getPrimerNombre()!=null && !newPerson.getPrimerNombre().equals(""))
				&& (newPerson.getPrimerApellido()!= null && !newPerson.getPrimerApellido().equals(""))
				&& (newPerson.getFechaNacimiento()!=null && !newPerson.getFechaNacimiento().equals(""))
				&& (phone!=null && phone.equals(""))) {
		
				Date today = new Date();
				if(newPerson.getFechaNacimiento().before(today)) {
					flag = true;
				}else {
					flag = false;
				}
			
		}else {
			flag = false;
		}
		
		return flag;
	}
	
	public void logout() {
		try {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("principal");
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(LoginController.USER_AUTENTICH);
		FacesContext.getCurrentInstance().getExternalContext().redirect(PAGE_PRINCIPAL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createPerson() {
		try {
			if(validateDataPerson()) {
				newPerson.setTelefono(new BigDecimal(phone));
				 servicesDao.create(newPerson, EnumDataBase.MYSQL.getId());
				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, properties.getProperty(""), properties.getProperty("")));
			}else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, properties.getProperty(""), properties.getProperty("")));
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, properties.getProperty(""), properties.getProperty("")));
		}
	}
	
	/*setter and getter*/
	public String getUserAccess() {
		return userAccess;
	}
	public void setUserAccess(String userAccess) {
		this.userAccess = userAccess;
	}
	public Persona getNewPerson() {
		return newPerson;
	}
	public void setNewPerson(Persona newPerson) {
		this.newPerson = newPerson;
	}
	public Properties getProperties() {
		return properties;
	}
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	public String getUsLoged() {
		return usLoged;
	}
	public void setUsLoged(String usLoged) {
		this.usLoged = usLoged;
	}
	public List<TipoDocumento> getTypesDocuments() {
		return typesDocuments;
	}
	public void setTypesDocuments(List<TipoDocumento> typesDocuments) {
		this.typesDocuments = typesDocuments;
	}
	public List<Ciudade> getListCities() {
		return listCities;
	}
	public void setListCities(List<Ciudade> listCities) {
		this.listCities = listCities;
	}
	public List<EnumRhBlood> getRhlist() {
		return rhlist;
	}
	public void setRhlist(List<EnumRhBlood> rhlist) {
		this.rhlist = rhlist;
	}
	public TipoDocumento getDocumentType() {
		return documentType;
	}
	public void setDocumentType(TipoDocumento documentType) {
		this.documentType = documentType;
	}
	public Ciudade getCity() {
		return city;
	}
	public void setCity(Ciudade city) {
		this.city = city;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public ServicesDao<Object> getServicesDao() {
		return servicesDao;
	}
	public void setServicesDao(ServicesDao<Object> servicesDao) {
		this.servicesDao = servicesDao;
	}
	
}
