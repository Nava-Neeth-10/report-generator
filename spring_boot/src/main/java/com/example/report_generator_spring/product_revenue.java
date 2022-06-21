package com.example.report_generator_spring;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
public class product_revenue {
	@Id
	Integer productrevenueid;
	Integer productid,quarter,year,month,profit;
	String updatedon;
	
	public void set_all(int productrevenueid,int productid,int quarter,int year,int month,int profit,String updatedon)
	{
		this.productrevenueid=productrevenueid;
		this.productid=productid;
		this.quarter=quarter;
		this.year=year;
		this.month=month;
		this.profit=profit;
		this.updatedon=updatedon;
	}
}