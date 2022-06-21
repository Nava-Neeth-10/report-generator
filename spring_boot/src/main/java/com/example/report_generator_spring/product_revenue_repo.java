package com.example.report_generator_spring;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface product_revenue_repo extends JpaRepository<product_revenue, Integer> {
	@Query("select distinct pr.year from product_revenue pr order by pr.year")
	public List<Integer> allyear();
	
	@Query("select distinct pr.year from product_revenue pr where pr.productid=?1 order by pr.year")
	public List<Integer> productyear(Integer productid);
	
	@Query("select sum(profit) from product_revenue pr where pr.year=?1 and pr.quarter=?2")
	public Integer allrevenue(Integer year,Integer quarter);
	
	@Query("select sum(profit) from product_revenue pr where pr.year=?1 and pr.quarter=?2 and pr.productid=?3")
	public Integer revenue(Integer year,Integer quarter,Integer id);
	
	@Query("select sum(profit) from product_revenue pr where pr.year=?1 and pr.quarter=?2 and pr.month=?3 and pr.productid=?4")
	public Integer revenuemonth(Integer year,Integer quarter,Integer month,Integer id);
	
}