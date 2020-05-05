package com.example.demo.controller;

import com.example.demo.model.Car;
import com.example.demo.model.Contract;
import com.example.demo.model.CountryList;
import com.example.demo.model.Renter;
import com.example.demo.service.CarService;
import com.example.demo.service.ContractService;
import com.example.demo.service.RenterService;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.model.IModel;

import javax.validation.Valid;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

@Controller
@RequestMapping("/contracts")
public class ContractController {

    private RenterService renterService;
    private ContractService contractService;
    private CarService carService;

    public ContractController(RenterService renterService, ContractService contractService, CarService carService) {
        this.renterService = renterService;
        this.contractService = contractService;
        this.carService = carService;
    }

    // create a mapping for "/list"
    @GetMapping("/list")
    public String listContracts(Model model) {
        // get contracts from the database
        List<Contract> theContracts = contractService.findAll();
        // add to the spring model
        model.addAttribute("contracts", theContracts);
        return "contracts/list";
    }

    @GetMapping("/create/selectDates")
    public String showDateForm(Model theModel, Contract theContract) {
        theModel.addAttribute("contract", theContract);
        return "contracts/create/select-dates";
    }

    // create new renter
    @PostMapping("/create/selectDates")
    public String saveDates(Model model, @Valid Contract theContract/*, BindingResult bindingResult*/) {
        // System.out.println(theContract);

        if (theContract.getEndDate()==null || theContract.getStartDate()==null || theContract.getStartDate().isBefore(java.time.LocalDate.now()) ||
                theContract.getStartDate().isAfter(theContract.getEndDate())) {
            return "contracts/create/select-dates";

        } else {

            //System.out.println(theContract.getStartDate());
            //System.out.println(theContract.getEndDate());

            String startDate = theContract.getStartDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
            String endDate = theContract.getEndDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));

            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);

            List<Car> availableCars = contractService.findAvailableCars(theContract);
            model.addAttribute("availableCars", availableCars);

            model.addAttribute("contract", theContract);
            return "contracts/create/list-available-cars";
        }
    }

    @GetMapping("/create/{startDate}/{endDate}/selectCar/{carId}")
    public String showRenterPage(@PathVariable ("startDate") String startDate,
                                 @PathVariable ("endDate") String endDate,
                                 @PathVariable ("carId") int carId,
                                 Model theModel) {
        theModel.addAttribute("startDate", startDate);
        theModel.addAttribute("endDate", endDate);
        theModel.addAttribute("carId", carId);
        return "contracts/create/new-or-existing-renter";
    }

    // create new renter
    // contracts/create/2020-05-03/2020-05-06/selectCar/3/new-renter

    @GetMapping("/create/{startDate}/{endDate}/selectCar/{carId}/new-renter")
    public String showRenterForm(@PathVariable ("startDate") String startDate,
                                 @PathVariable ("endDate") String endDate,
                                 @PathVariable ("carId") int carId,
                                 CountryList countryList,
                                 Model theModel) {
        Renter theRenter = new Renter();
        theModel.addAttribute("renter", theRenter);
        theModel.addAttribute("startDate", startDate);
        theModel.addAttribute("endDate", endDate);
        theModel.addAttribute("carId", carId);
        theModel.addAttribute("countryList", countryList.getCountries());
        return "contracts/create/add-new-renter";
    }

    @PostMapping("/create/{startDate}/{endDate}/selectCar/{carId}/new-renter")
    public String saveNewRenter(@PathVariable ("startDate") String startDate,
                               @PathVariable ("endDate") String endDate,
                               @PathVariable ("carId") int carId,
                               Model theModel,
                               @Valid Renter theRenter, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "contracts/create/add-new-renter";
        } else {
            Contract theContract = new Contract();

            int renterId = renterService.addRenter(theRenter);
            theContract.setRenter(theRenter);

            Car theCar = carService.findById(carId);
            theContract.setCar(theCar);

            System.out.println("Inside save new renter function before the formatter: " + " " + startDate + " " + endDate);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDateConverted = LocalDate.parse(startDate, formatter);
            LocalDate endDateConverted = LocalDate.parse(endDate, formatter);

            System.out.println("Inside save new renter function after the formatter: " + " " + startDate + " " + endDate);

            theContract.setStartDate(startDateConverted);
            theContract.setEndDate(endDateConverted);

            int contractId = contractService.addContract(theContract);
            System.out.println(contractId);

            theModel.addAttribute("contract", theContract);
            theModel.addAttribute("contractId", contractId);
            //return "redirect:/contracts/list";
            return "contracts/create/add-details";
        }
    }

    @PostMapping("/create/add-details/{contractId}")
    public String saveContractInfo(@PathVariable int contractId, @Valid Contract contract) {
        Contract newContract = contractService.findById(contractId);
        newContract.setMaxKm(contract.getMaxKm());
        newContract.setActualKm(contract.getActualKm());
        System.out.println("Inside the saveContractInfo method!");
        System.out.println(newContract);
        contractService.updateContract(newContract);
        return "redirect:/contracts/list";
    }

    // updateContract
    @GetMapping("/update/{contractId}")
    public String showUpdateContract(@PathVariable ("contractId") int contractId, Model theModel){
        System.out.println("Inside get mapping for update contract method!");
        Contract contract = contractService.findById(contractId);
        System.out.println(contract);
        theModel.addAttribute("contract", contract);
        return "contracts/update-contract";
    }

    @PostMapping("/update/{contractId}")
    public String updateContract(@PathVariable ("contractId") int contractId, Contract contract) {
        System.out.println("contract=  " + contract);
        Contract contract1 = contractService.findById(contractId);
        System.out.println("contract1=  " + contract1);
        System.out.println("contractId =  " + contractId);
        if (contract.getEndDate().isBefore(contract1.getStartDate())) {
            return "redirect:/contracts/update/{contractId}";
        }
        contractService.updateContract(contract);
        return "redirect:/contracts/list";
    }


    @GetMapping("create/{startDate}/{endDate}/selectCar/{carId}/existing-renter")
    public String listRenters(@PathVariable ("startDate") String startDate,
                              @PathVariable ("endDate") String endDate,
                              @PathVariable ("carId") int carId,
                              Model theModel, @RequestParam (defaultValue = "") String driverLicenseNumber) {
        theModel.addAttribute("startDate", startDate);
        theModel.addAttribute("endDate", endDate);
        theModel.addAttribute("carId", carId);
        System.out.println(carId);
        theModel.addAttribute("renters", renterService.findByDriverLicenseNumber(driverLicenseNumber));
        return "contracts/create/select-existing-renter";
    }

    @GetMapping("/create/{startDate}/{endDate}/selectCar/{carId}/existing-renter/{renterId}")
    public String saveExistingRenter(@PathVariable ("startDate") String startDate,
                                     @PathVariable ("endDate") String endDate,
                                     @PathVariable ("carId") int carId,
                                     @PathVariable ("renterId") int renterId,
                                     Model theModel) {
        System.out.println("Inside save existing renter!");

        Contract theContract = new Contract();

        Renter theRenter = renterService.findById(renterId);

        theContract.setRenter(theRenter);

        Car theCar = carService.findById(carId);

        theContract.setCar(theCar);

        System.out.println("Inside save new renter function before the formatter: " + " " + startDate + " " + endDate);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDateConverted = LocalDate.parse(startDate, formatter);
        LocalDate endDateConverted = LocalDate.parse(endDate, formatter);

        System.out.println("Inside save new renter function after the formatter: " + " " + startDate + " " + endDate);

        theContract.setStartDate(startDateConverted);
        theContract.setEndDate(endDateConverted);

        int contractId = contractService.addContract(theContract);
        System.out.println(contractId);

        theModel.addAttribute("contract", theContract);
        theModel.addAttribute("contractId", contractId);
        //return "redirect:/contracts/list";
        return "contracts/create/add-details";
    }

    @GetMapping("/delete/{contractId}")
    public String deleteContract(@PathVariable("contractId") int contractId) {
        Contract contract = contractService.findById(contractId);
        contractService.deleteContract(contract);
        //model.addAttribute("renters", renterRepository.findAll());
        return "redirect:/contracts/list";
    }

}
