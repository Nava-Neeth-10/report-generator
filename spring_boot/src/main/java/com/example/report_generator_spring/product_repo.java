package com.example.report_generator_spring;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface product_repo extends JpaRepository<product, Integer> {
	@Query("Select p.id from product p where p.productname=?1")
	Integer ProductId(String productname);
}