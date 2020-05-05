package com.example.demo.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;

public class Renter {

    // define fields
    private int renterId;

    @NotNull
    @Size(min=2, max=25, message = "First name must be between 2 and 25 characters.")
    @Pattern(message = "Invalid input.", regexp = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$")
    private String firstName;

    @NotNull
    @Size(min=2, max=25, message = "Last name must be between 2 and 25 characters.")
    @Pattern(message = "Invalid input.", regexp = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$")
    private String lastName;

    @Email
    private String email;

    @NotNull
    //@Pattern(message = "Invalid input.", regexp = "^\\d{10}$")
    @Size(min=6, max=12, message="Must be between 6 and 12 characters.")
    private String driverLicenseNumber;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate sinceDate;

    @Size(min=1, max = 30, message = "Street name must be between 1 and 30 characters.")
    private String street;

    private int building;

    @Size(min=1, max = 10, message = "Zip code must be between 1 and 10 characters.")
    private String zip;

    @Size(min=1, max=30, message = "City must be between 1 and 30 characters.")
    private String city;

    private String country;

    @Size(min=1, max=15, message = "Telephone must be between 1 and 15 characters.")
    private String telephone;

    private HashSet<String> telephones;

    private HashSet<Contract> contracts;


    // define constructors

    public Renter() {}

    public Renter(int renterId, String firstName, String lastName, String email, String driverLicenseNumber,
                  LocalDate sinceDate, String street, int building, String zip, String city, String country, String telephone, HashSet<String> telephones, HashSet<Contract> contracts) {
        this.renterId = renterId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.driverLicenseNumber = driverLicenseNumber;
        this.sinceDate = sinceDate;
        this.street = street;
        this.building = building;
        this.zip = zip;
        this.city = city;
        this.country = country;
        this.telephone = telephone;
        this.telephones = telephones;
        this.contracts = contracts;
    }

    // define getter/setter


    public String getTelephone() {
        return telephone;
    }

    public HashSet<String> getTelephones() {
        return telephones;
    }

    public HashSet<Contract> getContracts() {
        return contracts;
    }

    public int getRenterId() {
        return renterId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getDriverLicenseNumber() {
        return driverLicenseNumber;
    }

    public LocalDate getSinceDate() {
        return sinceDate;
    }

    public String getStreet() {
        return street;
    }

    public int getBuilding() {
        return building;
    }

    public String getZip() {
        return zip;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public void setRenterId(int renterId) {
        this.renterId = renterId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDriverLicenseNumber(String driverLicenseNumber) {
        this.driverLicenseNumber = driverLicenseNumber;
    }

    public void setSinceDate(LocalDate sinceDate) {
        this.sinceDate = sinceDate;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setBuilding(int building) {
        this.building = building;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setTelephones(HashSet<String> telephones) {
        this.telephones = telephones;
    }

    public void setContracts(HashSet<Contract> contracts) {
        this.contracts = contracts;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    // define tostring


    @Override
    public String toString() {
        return "Renter{" +
                "renterId=" + renterId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", driverLicenseNumber='" + driverLicenseNumber + '\'' +
                ", sinceDate=" + sinceDate +
                ", street='" + street + '\'' +
                ", building=" + building +
                ", zip='" + zip + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", telephone='" + telephone + '\'' +
                ", telephones=" + telephones +
                ", contracts=" + contracts +
                '}';
    }
}
