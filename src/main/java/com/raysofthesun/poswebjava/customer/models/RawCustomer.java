package com.raysofthesun.poswebjava.customer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.raysofthesun.poswebjava.customer.enums.PersonGender;
import com.raysofthesun.poswebjava.customer.enums.Salutation;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RawCustomer {
    @JsonIgnore
    private String id = UUID.randomUUID().toString();
    private String lastName;
    private String firstName;
    private String middleName;
    private String occupation;
    private String dateOfBirth;
    private String mobileNumber;
    private String emailAddress;
    private String monthlyIncome;
    private Salutation title;
    private PersonGender gender;
}
