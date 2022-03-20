package com.raysofthesun.poswebjava.customer2.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raysofthesun.poswebjava.customer2.enums.CustomerRelationship;
import com.raysofthesun.poswebjava.customer2.models.person.Person;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("customers")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonDeserialize(builder = Customer.Builder.class)
public class Customer extends Person {

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String agentId;

    private IncomeSource incomeSource = new IncomeSource();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean deleted;

    private CustomerRelationship relationshipWithCustomer = CustomerRelationship.SELF;

//    public static Builder fromRawCustomer(RawCustomer rawCustomer, String agentId) {
//        return Builder.fromRawCustomer(rawCustomer, agentId);
//    }
//
//    @JsonPOJOBuilder
//    public static class Builder {
//        final private String id;
//        final private String agentId;
//
//        private int age;
//        private String lastName;
//        private String firstName;
//        private String middleName;
//        private String occupation;
//        private String dateOfBirth;
//        private PersonGender gender;
//        private String monthlyIncome;
//        private Salutation salutation;
//        private Optional<Contact> primaryEmailAddress;
//        private Optional<Contact> primaryMobileNumber;
//
//        private Builder(String agentId) {
//            this.id = UUID.randomUUID().toString();
//            this.agentId = agentId;
//        }
//
//        private Builder(RawCustomer rawCustomer, String agentId) {
//            this(agentId);
//            this.gender = rawCustomer.getGender();
//            this.lastName = rawCustomer.getLastName();
//            this.firstName = rawCustomer.getFirstName();
//            this.middleName = rawCustomer.getMiddleName();
//            this.occupation = rawCustomer.getOccupation();
//            this.salutation = rawCustomer.getTitle();
//            this.dateOfBirth = rawCustomer.getDateOfBirth();
//            this.withPrimaryEmailAddress(rawCustomer.getEmailAddress())
//                    .withPrimaryMobileNumber(rawCustomer.getMobileNumber());
//        }
//
//
//        public static Builder fromRawCustomer(RawCustomer rawCustomer, String agentId) {
//            return new Builder(rawCustomer, agentId);
//        }
//
//        public Builder withLastName(String lastName) {
//            this.lastName = lastName;
//
//            return this;
//        }
//
//        public Builder withFirstName(String firstName) {
//            this.firstName = firstName;
//
//            return this;
//        }
//
//        public Builder withMiddleName(String middleName) {
//            this.middleName = middleName;
//            return this;
//        }
//
//        public Builder withOccupation(String occupation) {
//            this.occupation = occupation;
//            return this;
//        }
//
//        public Builder withMonthlyIncome(String monthlyIncome) {
//            this.monthlyIncome = monthlyIncome;
//            return this;
//        }
//
//        public Builder withAge(int age) {
//            this.age = age;
//            return this;
//        }
//
//        public Builder withPrimaryEmailAddress(String emailAddress) {
//            this.primaryEmailAddress = emailAddress.length() > 0
//                    ? Optional.of(new Contact(emailAddress, "", ContactType.PRIMARY_EMAIL))
//                    : Optional.empty();
//            return this;
//        }
//
//        public Builder withPrimaryMobileNumber(String mobileNumber) {
//            this.primaryMobileNumber = Optional.ofNullable(mobileNumber).isPresent() && mobileNumber.length() > 0
//                    ? Optional.of(new Contact(mobileNumber, "", ContactType.PRIMARY_EMAIL))
//                    : Optional.empty();
//            return this;
//        }
//
//        public Customer build() {
//            final ContactInfo contactInfo = new ContactInfo();
//            final PersonalInfo personalInfo = new PersonalInfo(lastName, firstName, middleName,
//                    salutation, age, dateOfBirth, gender);
//            final IncomeSource incomeSource = new IncomeSource(occupation);
//
//            primaryMobileNumber.ifPresent(contact -> contactInfo.setMobileNumbers(List.of(contact)));
//            primaryEmailAddress.ifPresent(contact -> contactInfo.setEmailAddresses(List.of(contact)));
//
//            Customer customer = new Customer();
//            customer.setId(id);
//            customer.setAgentId(this.agentId);
//            customer.setContactInfo(contactInfo);
//            customer.setPersonalInfo(personalInfo);
//            customer.setRelationshipWithCustomer(CustomerRelationship.SELF);
//            customer.setIncomeSource(incomeSource);
//
//            return customer;
//        }

//    }
}
