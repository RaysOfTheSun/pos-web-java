package com.raysofthesun.poswebjava.customer2.feign.application.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiApplicationMeta {
    private String id;
    private String name;
    private String customerId;
    private String insuredName;
    private String creationDate;
    private String lastModifiedDate;
    private String status;
}
