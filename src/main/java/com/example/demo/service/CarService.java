package com.example.demo.service;

import com.example.demo.model.Car;
import com.example.demo.model.Contract;
import com.example.demo.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }


    public List<Car> findAll() {
        return carRepository.fetchAll();
    }

    public List<String> findAllBrands() {
        return carRepository.findAllBrands();
    }

    public List<String> findAllModels() {
        return carRepository.findAllModels();
    }

    public List<String> findAllFuelTypes() {
        return carRepository.findAllFuelTypes();
    }

    public List<String> findAllRentalTypes() {
        return carRepository.findAllRentalTypes();
    }

    public List<String> findAllStatuses() {
        return carRepository.findAllStatuses();
    }

    public List<Car> findAvailableCars(Contract contract) {
        return carRepository.findAvailableCars(contract);
    }

    public Car findById(int carId) {
        return carRepository.findById(carId);
    }

    public void saveCar(Car car, String functionName) {
        int fuelId;
        int modelId;
        int brandId;
        int rentalTypeId;
        int statusId;

        List<String> models = carRepository.findAllModels();
        List<String> brands = carRepository.findAllBrands();

        fuelId = carRepository.findFuelIdByFuelName(car.getFuel());

        if (models.stream().anyMatch(car.getModel()::equalsIgnoreCase)) {
            modelId = carRepository.findModelIdByModelName(car.getModel());
        } else {
            modelId = carRepository.addModel(car);
        }

        if (brands.stream().anyMatch(car.getBrand()::equalsIgnoreCase)) {
            brandId = carRepository.findBrandIdByBrandName(car.getBrand());
        } else {
            brandId = carRepository.addBrand(car);
        }

        rentalTypeId = carRepository.findRentalTypeIdByRentalTypeName(car.getRentalType());

        statusId = carRepository.findStatusIdByStatusDescription(car.getStatus());

        if (functionName.equals("add")) {
            carRepository.addCar(car, brandId, modelId, fuelId, rentalTypeId, statusId);
        } else {
            carRepository.updateCar(car, brandId, modelId, fuelId, rentalTypeId, statusId);
        }
    }

    public Boolean deleteCar(int carId) {
        return carRepository.deleteCar(carId);
    }

    public boolean hasActiveContracts(int carId) {
        return carRepository.findCarsWithActiveContracts().contains(carId);
    }
}
