package org.sede.core.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="day")
public class CalendarDay {
	private Integer day;
	private Integer count;
	public CalendarDay(Integer day, Integer count) {
		this.day = day;
		this.count = count;
	}
	public Integer getDay() {
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "CalendarDay [day=" + day + ", count=" + count + "]";
	}
	
	
}
