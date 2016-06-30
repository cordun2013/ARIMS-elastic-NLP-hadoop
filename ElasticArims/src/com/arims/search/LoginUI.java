package com.arims.search;



import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;




@ManagedBean
@RequestScoped
public class LoginUI {
	
	
	final static Logger logger = LoggerFactory.getLogger(LoginUI.class);
	
	
	
	//private @ManagedProperty("#{webstate}")
	//Webstate webstate;
	
	private String userName;
	private String pwd;


	
	 public String validateUsernamePassword() {
		 
		 pwd = CryptWithMD5.cryptWithMD5(pwd);
		 char result = 0;
		 if(userName.equalsIgnoreCase("cmawebuser") && pwd.equals("bdb7f828db1c4bbafaa33559bae213e")){
			 result ='Y';
		 }
		 
	      
	        if ('Y'==result) {
	        	FacesContext fCtx = FacesContext.getCurrentInstance();
	        	HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(false);
	        	session.invalidate();   
	        	fCtx.getExternalContext().getSession(true); 
	        	getWebstate().setUser(userName);
   
	        }
	        else  {
	        	FacesContext.getCurrentInstance().addMessage(
	                    null,
	                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                            "Incorrect Username or Password",""));
	            return "login";
	        }
	        
	        
	        return "secure/search?faces-redirect=true";
	    }
	 
	 
	
	 
	 
	    //logout event, invalidate session
	    public String logout() {
	    	FacesContext facesContext = FacesContext.getCurrentInstance();
	    	HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
	    	getWebstate().setUser(null);
	        session.invalidate();
	        return "login";
	    }

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getPwd() {
			return pwd;
		}

		public void setPwd(String pwd) {
			this.pwd = pwd;
		}



		public Webstate getWebstate() {
			FacesContext fCtx = FacesContext.getCurrentInstance();
			ServletContext sc = (ServletContext) fCtx.getExternalContext().getContext();
			ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
        	Webstate webstate = (Webstate) ac.getBean("webstate");
			return webstate;
		}
		
}
