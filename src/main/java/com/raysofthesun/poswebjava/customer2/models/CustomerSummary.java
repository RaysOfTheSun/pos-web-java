package com.raysofthesun.poswebjava.customer2.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerSummary {
    private String id;
    private String gender;
    private String lastName;
    private String firstName;
    private String dateOfBirth;
    private String emailAddress = "";
    private String mobileNumber = "";
}
