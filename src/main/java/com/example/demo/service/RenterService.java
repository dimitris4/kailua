package com.example.demo.service;

import com.example.demo.model.Contract;
import com.example.demo.model.PhoneNumber;
import com.example.demo.model.Renter;
import com.example.demo.repository.RenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RenterService {

    RenterRepository renterRepository;

    @Autowired
    public RenterService(RenterRepository renterRepository) {
        this.renterRepository = renterRepository;
    }

    // fetch all renters
    public List<Renter> findAll() {
        return renterRepository.findAll();
    }

    // add new renter (unknown country - unknown zip code)
    public int addRenter(Renter renter) {
        List<String> countries = renterRepository.findAllCountries();
        System.out.println(countries);
        if (!countries.contains(renter.getCountry())) {
            int countryId = renterRepository.addCountry(renter);
            int zipId = renterRepository.addZip(renter, countryId);
            int addressId = renterRepository.addAddress(renter, zipId);
            int renterId = renterRepository.addRenter(renter, addressId);
            renter.setRenterId(renterId);
            renterRepository.addPhoneNumber(renter);
            return renterId;
        } else {
            //find the country
            int countryId = renterRepository.findCountryId(renter.getCountry());
            List<String> zips = renterRepository.findAllZips();
            if (!zips.contains(renter.getZip())) {
                int zipId = renterRepository.addZip(renter, countryId);
                int addressId = renterRepository.addAddress(renter, zipId);
                int renterId = renterRepository.addRenter(renter, addressId);
                renter.setRenterId(renterId);
                renterRepository.addPhoneNumber(renter);
                return renterId;
            } else {
                int zipId = renterRepository.findZipId(renter.getZip());
                int addressId = renterRepository.addAddress(renter, zipId);
                int renterId = renterRepository.addRenter(renter, addressId);
                renter.setRenterId(renterId);
                renterRepository.addPhoneNumber(renter);
                return renterId;
            }
        }
    }

    // find renter by ID
    public Renter findById(int id) {
        return renterRepository.findById(id);
    }

    public Renter updateRenter(int id, Renter renter) {
        return renterRepository.updateRenter(id, renter);
    }


    // delete renter
    public Boolean deleteRenter(int id) {
        return renterRepository.deleteRenter(id);
    }

    public List<PhoneNumber> findPhoneNumbersByRenterId(int renterId) {
        return renterRepository.findPhoneNumbersByRenterId(renterId);
    }

    public PhoneNumber findPhoneNumberById(int phoneId) {
        return renterRepository.findPhoneNumberById(phoneId);
    }

    public void addTelephone(Renter renter, PhoneNumber phoneNumber) {
        renterRepository.addTelephone(renter, phoneNumber);
    }

    public PhoneNumber updatePhoneNumber(int phoneId, PhoneNumber phoneNumber) {
        return renterRepository.updatePhoneNumber(phoneId, phoneNumber);
    }

    public void deletePhoneNumber(int phoneId) {
        renterRepository.deletePhoneNumber(phoneId);
    }

    public List<Contract> findContractsByRenterId(int renterId) {
        return renterRepository.findContractsByRenterId(renterId);
    }

    public Contract findContractById(int contractId) {
        return renterRepository.findContractById(contractId);
    }

    public void updateContract(int contractId, Contract contract) {
        renterRepository.updateContract(contractId, contract);
    }

    public boolean hasActiveContracts(int renterId) {
        return renterRepository.findRenterIdsWithActiveContracts().contains(renterId);
    }

    public List<Renter> findByDriverLicenseNumber(String driverLicenseNumber) {
        return renterRepository.findByDriverLicenseNumber(driverLicenseNumber);
    }
}
