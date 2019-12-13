package org.ciudadesabiertas.utils;

import org.apache.commons.lang3.StringUtils;
import org.sede.core.rest.CheckeoParametros;

public class PageInfo
{
	private String requestURL;
	private int pageSize;
	private long page=-1;
	private String fields;
	private String q;
	private String sort;
	private String extraParams;
	
	public PageInfo()
	{
		super();
		pageSize=550;
		page=CheckeoParametros.defaultPage;
	}
	
	
	
	public PageInfo(PageInfo pageInfo)
	{
		super();
		this.requestURL = pageInfo.requestURL;
		this.pageSize = pageInfo.pageSize;
		this.page = pageInfo.page;
		this.fields = pageInfo.fields;
		this.q = pageInfo.q;
		this.sort = pageInfo.sort;
		this.extraParams = pageInfo.extraParams;
	}



	@Override
	public String toString()
	{
		String URL=requestURL+"?";
		
		boolean firstParam=true;
		
		if (Util.validValue(q))
		{
			if (firstParam)
			{
				firstParam=false;				
			}else {
				URL+="&";
			}
			URL+="q=" + q;
		}
		if (pageSize>0)
		{	
			if (firstParam)
			{
				firstParam=false;				
			}else {
				URL+="&";
			}
			URL+="pageSize=" + pageSize;
		}
		if (page>=0)
		{
			if (firstParam)
			{
				firstParam=false;				
			}else {
				URL+="&";
			}
			URL+="page=" + page;
		}
		if (Util.validValue(fields))
		{
			if (firstParam)
			{
				firstParam=false;				
			}else {
				URL+="&";
			}
			URL+="fields=" + fields;
		}		
		if (Util.validValue(sort))
		{
			if (firstParam)
			{
				firstParam=false;				
			}else {
				URL+="&";
			}
			URL+="sort=" + sort;
		}
		if (Util.validValue(extraParams))
		{
			if (firstParam)
			{
				firstParam=false;				
			}else {
				URL+="&";
			}
			URL+=extraParams;
		}
		
		if (firstParam)
		{
			URL=StringUtils.chop(URL);
		}
		
		return URL;
	}
	
	
	
	public String getExtraParams()
	{
		return extraParams;
	}



	public void setExtraParams(String extraParams)
	{
		this.extraParams = extraParams;
	}



	public String getRequestURL()
	{
		return requestURL;
	}
	public void setRequestURL(String requestURL)
	{
		this.requestURL = requestURL;
	}
	public int getPageSize()
	{
		return pageSize;
	}
	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}
	public long getPage()
	{
		return page;
	}
	public void setPage(long page)
	{
		this.page = page;
	}
	public String getFields()
	{
		return fields;
	}
	public void setFields(String fields)
	{
		this.fields = fields;
	}
	public String getQ()
	{
		return q;
	}
	public void setQ(String q)
	{
		this.q = q;
	}
	public String getSort()
	{
		return sort;
	}
	public void setSort(String sort)
	{
		this.sort = sort;
	}
	
	
}
