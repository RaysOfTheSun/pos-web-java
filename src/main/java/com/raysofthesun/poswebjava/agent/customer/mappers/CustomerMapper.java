package com.raysofthesun.poswebjava.agent.customer.mappers;

import com.raysofthesun.poswebjava.agent.customer.constants.ContactType;
import com.raysofthesun.poswebjava.agent.customer.models.Customer;
import com.raysofthesun.poswebjava.agent.customer.models.CustomerSummary;
import com.raysofthesun.poswebjava.agent.customer.models.person.Contact;
import com.raysofthesun.poswebjava.agent.customer.models.person.ContactInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

@Mapper
public interface CustomerMapper {
    CustomerMapper MAPPER = Mappers.getMapper(CustomerMapper.class);

    @Mapping(source = "personalInfo.gender", target = "gender")
    @Mapping(source = "personalInfo.lastName", target = "lastName")
    @Mapping(source = "personalInfo.firstName", target = "firstName")
    @Mapping(source = "personalInfo.dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "customer.contactInfo", target = "emailAddress", qualifiedByName = "emailAddress")
    @Mapping(source = "customer.contactInfo", target = "mobileNumber", qualifiedByName = "mobileNumber")
    CustomerSummary mapCustomerToCustomerSummary(Customer customer);

    @Named("emailAddress")
    default String getCustomerEmailAddress(ContactInfo contactInfo) {
        Optional<Contact> primaryEmailAddress = contactInfo
                .getEmailAddresses()
                .stream()
                .filter((emailAddress) -> emailAddress.getType().equals(ContactType.PRIMARY_EMAIL))
                .findFirst();

        return primaryEmailAddress.isPresent() ? primaryEmailAddress.get().getValue() : "";
    }

    @Named("mobileNumber")
    default String getCustomerPrimaryMobile(ContactInfo contactInfo) {
        Optional<Contact> primaryEmailAddress = contactInfo
                .getMobileNumbers()
                .stream()
                .filter(contact -> contact.getType().equals(ContactType.PRIMARY_EMAIL))
                .findFirst();
        return primaryEmailAddress.isPresent() ? primaryEmailAddress.get().getValue() : "";
    }
}