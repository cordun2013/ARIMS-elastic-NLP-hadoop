package com.arims.classify;

public class FileObject {
	private String docNumber;
	private String docTitle;
	private String category;
	private String recNumber;
	
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
		System.out.println(docTitle + "-" + category);
		this.category = category;
	}
	public String getRecNumber() {
		return recNumber;
	}
	public void setRecNumber(String recNumber) {
		this.recNumber = recNumber;
	}

}
