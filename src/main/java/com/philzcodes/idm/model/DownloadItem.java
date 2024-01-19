package com.philzcodes.idm.model;

public class DownloadItem {
    private String name;
    private int size;
    private int priority;
    private String url; // The URL from which the item can be downloaded
   
    
	public DownloadItem(String name, int size, int priority, String url) {
		super();
		this.name = name;
		this.size = size;
		this.priority = priority;
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
    
    

}
