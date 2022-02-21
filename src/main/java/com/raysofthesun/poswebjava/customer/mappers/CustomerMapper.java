package com.raysofthesun.poswebjava.customer.mappers;

import com.raysofthesun.poswebjava.customer.constants.ContactType;
import com.raysofthesun.poswebjava.customer.models.Customer;
import com.raysofthesun.poswebjava.customer.models.CustomerSummary;
import com.raysofthesun.poswebjava.customer.models.RawCustomer;
import com.raysofthesun.poswebjava.customer.models.person.Contact;
import com.raysofthesun.poswebjava.customer.models.person.ContactInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
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

    @Mapping(source = "title", target = "personalInfo.salutation")
    @Mapping(source = "gender", target = "personalInfo.gender")
    @Mapping(source = "lastName", target = "personalInfo.lastName")
    @Mapping(source = "firstName", target = "personalInfo.firstName")
    @Mapping(source = "middleName", target = "personalInfo.middleName")
    @Mapping(source = "dateOfBirth", target = "personalInfo.dateOfBirth")
    @Mapping(source = ".", target = "contactInfo.mobileNumbers", qualifiedByName = "rawMobileToContact")
    @Mapping(source = ".", target = "contactInfo.emailAddresses", qualifiedByName = "rawEmailToContact")
    Customer mapRawCustomerToCustomer(RawCustomer rawCustomer);

    @Named("rawMobileToContact")
    default List<Contact> mapRawCustomerEmailToCustomerMobileNumber(RawCustomer rawCustomer) {
        Optional<String> rawMobileNumber = Optional.ofNullable(rawCustomer.getMobileNumber());
        Contact emailContact = rawMobileNumber.isPresent()
                ? new Contact(rawMobileNumber.get(), "", ContactType.PRIMARY_MOBILE)
                : null;

        return rawMobileNumber.isPresent() ? List.of(emailContact) : List.of();
    }

    @Named("rawEmailToContact")
    default List<Contact> mapRawCustomerEmailToCustomerEmail(RawCustomer rawCustomer) {
        Optional<String> rawCustomerEmail = Optional.ofNullable(rawCustomer.getEmailAddress());
        Contact emailContact = rawCustomerEmail.isPresent()
                ? new Contact(rawCustomerEmail.get(), "", ContactType.PRIMARY_EMAIL)
                : null;

        return rawCustomerEmail.isPresent() ? List.of(emailContact) : List.of();
    }

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
