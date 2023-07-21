package com.example.demo.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;


@Controller
@ConfigurationProperties("app")
@Getter
@Setter
@ToString
public class AppProperties {

    private String url;
    private  String userName;
    private String password;
    private String startDate;
    private String endDate;
    private String api;

//    private String url = "https://supplierhub.homedepot.com/";
//    private String driverLocation = "/Users/onirnarahari/downloads/chromedriver_mac64/chromedriver";
//
//    String initialDate = "JUN 2022";

}