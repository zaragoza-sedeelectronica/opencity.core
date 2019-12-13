package org.ciudadesabiertas.utils;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.sede.servicio.ModelAttr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties({"id"})
@XmlRootElement(name = ModelAttr.RESULTADO)
public class Result<T> {
		
	
	private int page=0;
	private int pageSize=0;
	private long totalRecords=0;
	private long pageRecords=0;
	private int status=0;
	private Date responseDate;
	private String first="";
	private String last="";
	private String next="";
	private String prev="";
	private String self="";
	private String contentMD5="";
	
			
	private List<T> records = null;
	
	
	public Result() {
		super();	
		responseDate=new Date();
	}
	

	public Date getResponseDate()
	{
		return responseDate;
	}


	public void setResponseDate(Date responseDate)
	{
		this.responseDate = responseDate;
	}


	public int getPage()
	{
		return page;
	}

	public void setPage(int page)
	{
		this.page = page;
	}

	public int getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}

	


	public List<T> getRecords()
	{
		return records;
	}


	public void setRecords(List<T> records)
	{
		this.records = records;
	}


	public long getTotalRecords() {
		return totalRecords;
	}


	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}
	


	public String getFirst()
	{
		return first;
	}


	public void setFirst(String first)
	{
		this.first = first;
	}


	public String getLast()
	{
		return last;
	}


	public void setLast(String last)
	{
		this.last = last;
	}


	public String getNext()
	{
		return next;
	}


	public void setNext(String next)
	{
		this.next = next;
	}


	public String getPrev()
	{
		return prev;
	}


	public void setPrev(String prev)
	{
		this.prev = prev;
	}


	public String getSelf()
	{
		return self;
	}


	public void setSelf(String self)
	{
		this.self = self;
	}
	
	
	
	public String getContentMD5() {
		return contentMD5;
	}


	public void setContentMD5(String contentMD5) {
		this.contentMD5 = contentMD5;
	}


	public long getPageRecords()
	{
		return pageRecords;
	}


	public void setPageRecords(long pageRecords)
	{
		this.pageRecords = pageRecords;
	}

	

	
	
	
}
