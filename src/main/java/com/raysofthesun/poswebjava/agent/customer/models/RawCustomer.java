package com.raysofthesun.poswebjava.agent.customer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RawCustomer {
    @JsonIgnore
    private String id = UUID.randomUUID().toString();
    private String title;
    private String gender;
    private String lastName;
    private String firstName;
    private String middleName;
    private String occupation;
    private String dateOfBirth;
    private String mobileNumber;
    private String emailAddress;
    private String monthlyIncome;
}
