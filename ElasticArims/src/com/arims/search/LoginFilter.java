package com.arims.search;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class LoginFilter implements Filter{
	private ApplicationContext ac;
	  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		       
	        Webstate webstate = (Webstate) ac.getBean("webstate");
	        if(webstate!=null){
		            
	        if (webstate.getUser()==null) {
	        	 String contextPath = ((HttpServletRequest)request).getContextPath();
	        	 String loginURL =contextPath + "/login.xhtml";
	        	 if ("partial/ajax".equals(((HttpServletRequest)request).getHeader("Faces-Request"))) {
	        		
	        		 ((HttpServletResponse)response).setContentType("text/xml");
	        		 ((HttpServletResponse)response).getWriter()
	        	            .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
	        	            .printf("<partial-response><redirect url=\"%s\"></redirect></partial-response>", loginURL);
	        	    }
	        	 else{
	           
	        	 ((HttpServletResponse)response).sendRedirect(loginURL);
	        	 }
	        }
	        else{
	        chain.doFilter(request, response);
	        }
	        }
	             
	    }
	 
	    public void init(FilterConfig config) throws ServletException {
	    	ac = WebApplicationContextUtils.getWebApplicationContext((ServletContext)config.getServletContext());
	    }
	 
	    public void destroy() {
	    }

	

}
