package org.ciudadesabiertas.utils;

import java.util.ArrayList;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "groupby")
public class GroupBySearch {
	
//	@ApiParam(required = true, name = "fields")
	private String fields;
	private String where;
	private String sort;
//	@ApiParam(required = true, name = "group")
	private String group;
	private String having;

	
	public static final String SEPARATOR= ","; 
	public static final String ASC="+";
	public static final String DESC="-";
	
	public GroupBySearch()
	{
		super();
		this.fields = "";
		this.where = "";
		this.sort = "";
		this.group = "";
		this.having = "";
	}
	

	public GroupBySearch(GroupBySearch g)
	{
		super();
		this.fields = g.fields;
		this.where = g.where;
		this.sort = g.sort;
		this.group = g.group;
		this.having = g.having;
	}

	public String getFields() {
		return fields;
	}
	
	public void setFields(String fields) {
		this.fields = fields;
	}
	
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	
	
	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public String getHaving() {
		return having;
	}

	public void setHaving(String having) {
		this.having = having;
	}

	public List<String> extractField(){
		 List<String> result = new ArrayList<String>();
		if (this.fields!=null && !"".equals(this.fields)) {
			String[] listado= fields.split(SEPARATOR);
			for (String obj: listado) {
				result.add(obj);
			}
		}
			
		return result;
	}
	
	public List<Sort> extractOrder(){
		 List<Sort> result = new ArrayList<Sort>();
		if (this.sort!=null && !"".equals(this.sort)) {
			String[] listado= sort.split(SEPARATOR);
			for (String obj: listado) {
				if (obj.startsWith(DESC)) {
					result.add( new Sort (obj.substring(1, obj.length()),true) );
						
				}else if (obj.startsWith(ASC)) {
					result.add( new Sort (obj.substring(1, obj.length()),false) );					
				}
				else {
					result.add( new Sort (obj,false) );
				}
			}
		}
			
		return result;
	}
	
	public String createQuery(String tabla) {
		String result = null;		
		boolean error = false;
		if (fields!=null && !"".equals(fields)) {
			result = "Select " + fields + " from " + tabla + " ";						
		}else {
			result = "[ERROR]";
			error=true;
		}
		if (!error) {
			if (where!=null && !"".equals(where)) {
				result+= " where " +  where + " ";
			}	
		}
		if (!error) {
			if (group!=null && !"".equals(group)) {
				result+= " group by " + group + " ";
			}	
		}
		if (!error) {
			if (having!=null && !"".equals(having)) {
				result+= " having " + having + " ";
			}	
		}
		if (!error) {
			if (sort!=null && !"".equals(sort)) {
				
				List<Sort> listadoOrden = extractOrder();
				if (!listadoOrden.isEmpty()) {
					result+= " Order by ";
					int count =1;
					for (Sort order: listadoOrden) {
						result += order.getProperty() + " " + (order.isDesc() ? " DESC " : " ASC ");
						if (count<listadoOrden.size()) {
							result += ", ";
						}
					}
				}
			}		
		}
		
		
		return result;
		
	}


	@Override
	public String toString()
	{
		return "GroupBySearch [fields=" + fields + ", where=" + where + ", sort=" + sort + ", group=" + group + ", having=" + having + "]";
	}
	
	
	

}
