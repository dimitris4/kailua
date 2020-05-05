package com.example.demo.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class Contract {

    // define fields

    private int contractId;


    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;


    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    private int maxKm;

    private int actualKm;

    private Car car;

    private Renter renter;


    // define constructors

    public Contract() {}

    public Contract(int contractId, LocalDate startDate, LocalDate endDate, int maxKm, int actualKm, Car car, Renter renter) {
        this.contractId = contractId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxKm = maxKm;
        this.actualKm = actualKm;
        this.car = car;
        this.renter = renter;
    }

    // define getter/setter


    public Renter getRenter() {
        return renter;
    }

    public Car getCar() {
        return car;
    }

    public int getContractId() {
        return contractId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getMaxKm() {
        return maxKm;
    }

    public int getActualKm() {
        return actualKm;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setMaxKm(int maxKm) {
        this.maxKm = maxKm;
    }

    public void setActualKm(int actualKm) {
        this.actualKm = actualKm;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public void setRenter(Renter renter) {
        this.renter = renter;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "contractId=" + contractId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", maxKm=" + maxKm +
                ", actualKm=" + actualKm +
                ", car=" + car +
                ", renter=" + renter +
                '}';
    }

}
