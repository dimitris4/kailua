package com.example.demo.service;

import com.example.demo.model.Car;
import com.example.demo.model.Contract;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.ContractRepository;
import com.example.demo.repository.RenterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractService {

    private ContractRepository contractRepository;
    private CarRepository carRepository;
    private RenterRepository renterRepository;

    public ContractService(ContractRepository contractRepository, CarRepository carRepository, RenterRepository renterRepository) {
        this.contractRepository = contractRepository;
        this.carRepository = carRepository;
        this.renterRepository = renterRepository;
    }

    public List<Contract> findAll() {
        return contractRepository.fetchAll();
    }

    public List<Car> findAvailableCars(Contract contract) {

        //System.out.println(contract);
        return carRepository.findAvailableCars(contract);

    }

    public int addContract(Contract theContract) {
        System.out.println("Inside add contract method in the contract service: " + " " + theContract.getStartDate() + " " + theContract.getEndDate());
        return contractRepository.addContract(theContract);
    }

    public Contract findById(int contractId) {
        return contractRepository.findById(contractId);
    }

    public void updateContract(Contract contract) {
        contractRepository.updateContract(contract);
    }

    public void deleteContract(Contract contract) {
        contractRepository.delete(contract);
    }
}
