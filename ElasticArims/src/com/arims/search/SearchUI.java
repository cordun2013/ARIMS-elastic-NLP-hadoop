package com.arims.search;

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.util.ArrayList;


import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;


@ManagedBean
@SessionScoped
public class SearchUI {
	


	
	private String searchText;
	private List<FileObject> fileObjects = null;
	private List<String> indexArray = null;
	private String selectedName;
	private String selectedIndex;
	ResourceBundle env = ResourceBundle.getBundle("local");
	private static final int DEFAULT_BUFFER_SIZE = 10240; 

	 public String executeSearch() {
		 
		 String elastic_url = env.getString("elastic_base_url") + "/" + selectedIndex +"/_search";
		 System.out.println("elastic_url " + elastic_url );
		 Client client = Client.create();
		  fileObjects = new ArrayList<FileObject>();	
			WebResource webResource = client
			   .resource(elastic_url); //"http://localhost:9200/arims-enh-pdf-2016.06/_search");
			
			String postingString = "{\"query\":{\"query_string\":{\"query\":\""+ searchText + "\"}}}";
			
			System.out.println("post string" + postingString );
			ClientResponse response = null;
			try{
			response =  webResource.type("application/json")
					   .post(ClientResponse.class, postingString);
			}
			catch(Exception e){
				System.out.println(e);
				FacesContext.getCurrentInstance().addMessage(
	                    null,
	                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                            "Elasticsearch Down",""));
				return null;
			}
			if (response.getStatus() != 200) {
				System.out.println("Error response from arims-enh-pdf-2016.06/_search call");
				FacesContext.getCurrentInstance().addMessage(
	                    null,
	                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                            "Error from Elastic search",""));
				return null;
				}
				String output = response.getEntity(String.class);
			    JSONObject jsonObject = new JSONObject(output);
			    JSONObject hits = jsonObject.getJSONObject("hits");
			    JSONArray matchList = hits.getJSONArray("hits");
				System.out.println("matchList" + matchList );
				for(int i=0;i<matchList.length();i++){
					JSONObject source = matchList.getJSONObject(i).getJSONObject("_source");
					FileObject fileObject = new FileObject();
					fileObject.setDocNumber(source.getString("DocNumber"));
					fileObject.setCategory(source.getString("Category"));
					fileObject.setDocTitle(source.getString("DocTitle").substring(0, 25));
					
					System.out.println("source" + source );
					fileObjects.add(fileObject);
				}

	        return null;
	    }
	 
	public List<String> getElasticIndices() {
		//System.out.print("Inside getIndices");
		if (indexArray == null) {
			String elastic_base_url = env.getString("elastic_base_url");
			Client client = Client.create();
			indexArray = new ArrayList<String>();
			WebResource webResource = client.resource(elastic_base_url
					+ "/_cat/indices");

			ClientResponse response = null;
			try {
				response = webResource.type("application/json").get(
						ClientResponse.class);

			} catch (Exception e) {
				System.out.println(e);
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Elasticsearch Down", ""));
				return null;
			}
			String output = response.getEntity(String.class);
			System.out.println(" response = " + output);
			JSONArray matchList = new JSONArray(output);
			System.out.println("matchList" + matchList);
			for (int i = 0; i < matchList.length(); i++) {
				String indexName = new String();
				JSONObject object = (JSONObject) matchList.get(i);
				indexName = object.getString("index");
				indexArray.add(indexName);
				System.out.println("indexName" + indexName);
			}
		}

		return indexArray;
	}
	 
	 public void openDocument() {

		 FacesContext facesContext = FacesContext.getCurrentInstance();
	        ExternalContext externalContext = facesContext.getExternalContext();
	        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
	        String fileName = selectedName+".pdf";
	        String file_location_path = env.getString("file_location_path");
	        
	        File file = new File(file_location_path,fileName);//"/Users/suthanchakkamadathil/Documents/ARMY_CMA_ARIMS_Project/ARIMS_Data/OriginalPDFs/", fileName);
	        BufferedInputStream input = null;
	        BufferedOutputStream output = null;
	        try {
	            // Open file.
	            input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);

	            // Init servlet response.
	            response.reset();
	            response.setContentType("application/pdf");
	            response.setContentLength((int) file.length());
	            response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
	            output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

	            // Write file contents to response.
	            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
	            int length;
	            while ((length = input.read(buffer)) > 0) {
	                output.write(buffer, 0, length);
	            }
	            // Finalize task.
	            output.flush();
	            
	        } catch (IOException e) {
				System.out.println("Error displaying the PDF "  + e);
			} finally {
	            close(output);
	            close(input);
	            //file.delete();
	        }

	        facesContext.responseComplete();

	    }
	 public void openDocument_new(){
	        String fileName = selectedName+".pdf";
	        File file = new File("/Users/suthanchakkamadathil/Documents/ARMY_CMA_ARIMS_Project/ARIMS_Data/OriginalPDFs/", fileName);
	        if (Desktop.isDesktopSupported()) {
	            try {
	                //File myFile = new File(file);
	                Desktop.getDesktop().open(file);
	            } catch (IOException ex) {
	                // no application registered for PDFs
	            	System.out.println("Error displaying the PDF "  + ex);
	            }
	          
	        }
		 
	 }
	 
	 	private static void close(Closeable resource) {
	        if (resource != null) {
	            try {
	                resource.close();
	            } catch (IOException e) {
	            	System.out.println("Error cleaning up "  + e);
	            }
	        }
	    }
	 
	    //logout event, invalidate session
	    public String logout() {
	    	    return "login";
	    }


		public String getSearchText() {
			return searchText;
		}


		public void setSearchText(String searchText) {
			this.searchText = searchText;
		}


		public List<FileObject> getFileObjects() {
			return fileObjects;
		}


		public void setFileObjects(List<FileObject> fileObjects) {
			this.fileObjects = fileObjects;
		}


		public String getSelectedName() {
			return selectedName;
		}


		public void setSelectedName(String selectedName) {
			this.selectedName = selectedName;
		}

		public String getSelectedIndex() {
			return selectedIndex;
		}

		public void setSelectedIndex(String selectedIndex) {
			this.selectedIndex = selectedIndex;
		}


	
	
	
}
