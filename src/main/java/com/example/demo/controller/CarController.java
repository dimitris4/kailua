package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/cars")
public class CarController {

    private CarService service;

    // since we have only one constructor @auto-wired is optional
    @Autowired
    public CarController(CarService service) {
        this.service = service;
    }


    // create a mapping for "/list"
    @GetMapping("/list")
    public String listCars(Model model) throws SQLException {
        // get renters from the database
        List<Car> theCars = service.findAll();
        // add to the spring model
        model.addAttribute("cars", theCars);
        return "cars/list";
    }

    @GetMapping("/showForm")
    public String showForm(Model theModel) {
        Car car = new Car();
        List<String> fuels = service.findAllFuelTypes();
        List<String> rentalTypes = service.findAllRentalTypes();
        List<String> statuses = service.findAllStatuses();
        theModel.addAttribute("fuels", fuels);
        theModel.addAttribute("rentalTypes", rentalTypes);
        theModel.addAttribute("statuses", statuses);
        theModel.addAttribute("car", car);
        return "cars/add-form";
    }

    // create new renter
    @PostMapping("/showForm")
    public String save(@Valid @ModelAttribute Car car, BindingResult bindingResult, Model theModel) {
        if (bindingResult.hasErrors()) {
            List<String> fuels = service.findAllFuelTypes();
            List<String> rentalTypes = service.findAllRentalTypes();
            List<String> statuses = service.findAllStatuses();
            theModel.addAttribute("fuels", fuels);
            theModel.addAttribute("rentalTypes", rentalTypes);
            theModel.addAttribute("statuses", statuses);
            return "cars/add-form";
        }
        service.saveCar(car, "add");
        System.out.println("new car added: " + car);
        return "redirect:/cars/list";
    }

    // show form for update
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") int carId, Model theModel) {
        Car car = service.findById(carId);
        List<String> fuels = service.findAllFuelTypes();
        List<String> rentalTypes = service.findAllRentalTypes();
        List<String> statuses = service.findAllStatuses();
        theModel.addAttribute("car", car);
        theModel.addAttribute("fuels", fuels);
        theModel.addAttribute("rentalTypes", rentalTypes);
        theModel.addAttribute("statuses", statuses);
        theModel.addAttribute("car", car);
        return "cars/update";
    }

    @PostMapping("/update/{id}")
    public String updateRenter(/*@PathVariable("id") int carId,*/ @Valid Car car, BindingResult bindingResult, Model theModel) {
        if (bindingResult.hasErrors()) {
            List<String> fuels = service.findAllFuelTypes();
            List<String> rentalTypes = service.findAllRentalTypes();
            List<String> statuses = service.findAllStatuses();
            theModel.addAttribute("fuels", fuels);
            theModel.addAttribute("rentalTypes", rentalTypes);
            theModel.addAttribute("statuses", statuses);
            // car.setCarId(carId);
            return "cars/update";
        }
        System.out.println(car);
        service.saveCar(car, "update");
        return "redirect:/cars/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteRenter(@PathVariable("id") int carId) {
        if (service.hasActiveContracts(carId)) {
            return "cars/cannot-delete";
        } else {
            service.deleteCar(carId);
        }
        return "redirect:/cars/list";
    }
}
