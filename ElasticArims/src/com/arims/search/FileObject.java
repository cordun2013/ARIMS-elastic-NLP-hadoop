package com.arims.search;

public class FileObject {
	private String docNumber;
	private String docTitle;
	private String category;
	
	public String getDocNumber() {
		return docNumber;
	}
	public void setDocNumber(String fileName) {
		this.docNumber = fileName;
	}
	public String getDocTitle() {
		return docTitle;
	}
	public void setDocTitle(String fileContent) {
		this.docTitle = fileContent;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}

}
