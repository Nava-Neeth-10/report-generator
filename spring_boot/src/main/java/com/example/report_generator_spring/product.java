package com.example.report_generator_spring;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class product {
	@Id
	Integer productid;
	String  productname,description,createdby,createdon,modifiedby,modifiedon;
	
	public void set_all(int ProductID,String ProductName,String Description, String CreatedBy,String CreatedOn, String ModifiedBy,String ModifiedOn)
	{
		this.productid=ProductID;
		this.productname=ProductName;
		this.description=Description;
		this.createdby=CreatedBy;
		this.createdon=CreatedOn;
		this.modifiedby=ModifiedBy;
		this.modifiedon=ModifiedOn;
	}
}