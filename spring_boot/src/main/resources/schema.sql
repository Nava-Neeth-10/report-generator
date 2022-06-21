drop database if exists Report_Generator;
create database Report_Generator;
use Report_Generator;
create table user (id int,first_name varchar(50),last_name varchar(50),email varchar(50),username varchar(50),password varchar(50));
insert into user values (1,'pindikanti','vivek','pindikantivivek@gmail.com','vivek','ekM7s4r7zNGkX0DPBVll1A==');
create table product (ProductID INT,ProductName VARCHAR(255),Description VARCHAR(255),CreatedBy VARCHAR(255),CreatedOn DATE,ModifiedBy VARCHAR(255),ModifiedOn DATE);
CREATE TABLE Product_Revenue (ProductRevenueID INT,ProductID INT,Quarter INT,Year INT,Month INT,Profit INT,UpdatedOn DATE);