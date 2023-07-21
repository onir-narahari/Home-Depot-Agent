package com.example.demo.models;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "invoicewithexceptionclaims")
@Getter
@Setter
public class Claim {

    @Column(name = "exceptiontype")
    public String exceptionType;


    @Column(name = "vendornumber")
    public String vendorNumber;
    @Column(name = "vendorname")
    public String vendorName;
    @Column(name = "invoicedate")
    public Date invoiceDate;
    @Id
    @Column(name = "invoicenumber")
    public String invoiceNumber;
    @Column(name = "ponumber")
    public String poNumber;
    @Column(name = "storenumber")
    public String storeNumber;
    @Column(name ="invoiceamount" )
    public  BigDecimal invoiceAmount;
    @Column(name = "voucher")
    public String voucher;
    @Column(name = "errordesc")
    public String errorDesc;


}




