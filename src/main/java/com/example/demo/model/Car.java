package com.example.demo.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class Car {

    // define fields
    private int carId;

    @NotNull
    @Size(min=1, max=10, message = "Registration number must be between 1 and 10 characters.")
    private String registrationNumber;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate firstRegistration;

    private int odometer;

    private String fuel;

    @Size(min=1, max=30, message = "Model name must be between 1 and 30 characters.")
    private String model;

    @Size(min=1, max=30, message = "Brand name must be between 1 and 30 characters.")
    private String brand;

    private String rentalType;

    private String status;


    // define constructors

    public Car() {}

    public Car(int carId, String registrationNumber, LocalDate firstRegistration, int odometer, String fuel, String model,
               String brand, String rentalType, String status) {
        this.carId = carId;
        this.registrationNumber = registrationNumber;
        this.firstRegistration = firstRegistration;
        this.odometer = odometer;
        this.fuel = fuel;
        this.model = model;
        this.brand = brand;
        this.rentalType = rentalType;
        this.status = status;
    }

    // define getter/setter

    public int getCarId() {
        return carId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public LocalDate getFirstRegistration() {
        return firstRegistration;
    }

    public int getOdometer() {
        return odometer;
    }

    public String getFuel() {
        return fuel;
    }

    public String getModel() {
        return model;
    }

    public String getBrand() {
        return brand;
    }

    public String getRentalType() {
        return rentalType;
    }

    public String getStatus() {
        return status;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setFirstRegistration(LocalDate firstRegistration) {
        this.firstRegistration = firstRegistration;
    }

    public void setOdometer(int odometer) {
        this.odometer = odometer;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setRentalType(String rentalType) {
        this.rentalType = rentalType;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    // define tostring

    @Override
    public String toString() {
        return "Car{" +
                "id=" + carId +
                ", registration_number='" + registrationNumber + '\'' +
                ", first_registration=" + firstRegistration +
                ", odometer=" + odometer +
                ", fuelId=" + fuel +
                ", modelId=" + model +
                ", brandId=" + brand +
                ", rentalTypeId=" + rentalType +
                ", status=" + status +
                '}';
    }
}
