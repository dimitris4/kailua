package com.example.demo.controller;

import com.example.demo.model.Contract;
import com.example.demo.model.CountryList;
import com.example.demo.model.PhoneNumber;
import com.example.demo.model.Renter;
import com.example.demo.service.ContractService;
import com.example.demo.service.RenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/renters")
public class RenterController {

    private RenterService renterService;
    private ContractService contractService;

    // since we have only one constructor @auto-wired is optional
    @Autowired
    public RenterController(RenterService renterService, ContractService contractService) {
        this.renterService = renterService;
        this.contractService = contractService;
    }

    // create a mapping for "/list"
    @GetMapping("/list")
    public String listRenters(Model model) throws SQLException {
        // get renters from the database
        List<Renter> theRenters = renterService.findAll();
        // add to the spring model
        model.addAttribute("renters", theRenters);
        return "renters/list";
    }

    @GetMapping("/showForm")
    public String showForm(Model theModel, CountryList countryList) {
        Renter renter = new Renter();
        theModel.addAttribute("renter", renter);
        theModel.addAttribute("countryList", new CountryList().getCountries());
        return "renters/add-form";
    }

    // create new renter
    @PostMapping("/showForm")
    public String save(Model model, CountryList cl, @ModelAttribute @Valid Renter renter, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("countryList", cl.getCountries());
            return "renters/add-form";
        } else {
            model.addAttribute("countryList", cl.getCountries());
            renterService.addRenter(renter);
            System.out.println("new renter added: " + renter);
            return "redirect:/renters/list";
        }
    }

    // show form for update
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") int renterId, Model model, CountryList countryList) {
        Renter renter = renterService.findById(renterId);
        model.addAttribute("countryList", countryList.getCountries());
        model.addAttribute("renter", renter);
        return "renters/update";
    }

    @PostMapping("/update/{id}")
    public String updateRenter(@PathVariable("id") int renterId, @Valid Renter theRenter, BindingResult result, Model theModel) {
        if (result.hasErrors()) {
            theModel.addAttribute("countryList", new CountryList().getCountries());
            theRenter.setRenterId(renterId);
            return "renters/update";
        }
        renterService.updateRenter(renterId, theRenter);
        return "redirect:/renters/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteRenter(@PathVariable("id") int renterId) {
        if (renterService.hasActiveContracts(renterId)) {
            return "renters/cannot-delete";
        } else {
            //Renter renter = service.findById(renterId);
            renterService.deleteRenter(renterId);
            //model.addAttribute("renters", renterRepository.findAll());
            return "redirect:/renters/list";
        }
    }

    // show telephones for renter
    @GetMapping("/telephones/{id}")
    public String listTelephones(@PathVariable("id") int renterId, Model theModel){
        List<PhoneNumber> phoneNumbers = renterService.findPhoneNumbersByRenterId(renterId);
        Renter renter = renterService.findById(renterId);
        theModel.addAttribute("renter", renter);
        theModel.addAttribute("phoneNumbers", phoneNumbers);
        return "phoneNumbers/list-telephones";
    }

    @GetMapping("/telephones/add/{id}")
    public String showAddTelephone(@PathVariable("id") int renterId, Model theModel){
        Renter renter = renterService.findById(renterId);
        PhoneNumber phoneNumber = new PhoneNumber();
        theModel.addAttribute("phoneNumber", phoneNumber);
        theModel.addAttribute("renter", renter);
        return "phoneNumbers/add-telephone";
    }

    @PostMapping("/telephones/add/{id}")
    public String saveTelephone(@PathVariable("id") int renterId, @Valid @ModelAttribute PhoneNumber phoneNumber, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "phoneNumbers/add-telephone";
        }
        Renter renter = renterService.findById(renterId);
        renterService.addTelephone(renter, phoneNumber);
        return "redirect:/renters/telephones/{id}";
    }

    @GetMapping("/telephones/update/{renterId}/{phoneId}")
    public String showUpdateForm(@PathVariable ("renterId") int renterId, @PathVariable("phoneId") int phoneId, Model theModel){
        PhoneNumber phoneNumber = renterService.findPhoneNumberById(phoneId);
        Renter renter = renterService.findById(renterId);
        theModel.addAttribute("phoneNumber", phoneNumber);
        return "phoneNumbers/update-telephone";
    }

    @PostMapping("/telephones/update/{renterId}/{phoneId}")
    public String updateTelephone(@PathVariable ("renterId") int renterId, @PathVariable ("phoneId") int phoneId, @Valid @ModelAttribute PhoneNumber phoneNumber) {
        renterService.updatePhoneNumber(phoneId, phoneNumber);
        return "redirect:/renters/telephones/{renterId}";
    }

    @GetMapping("/telephones/delete/{renterId}/{phoneId}")
    public String deletePhoneNumber(@PathVariable ("renterId") int renterId, @PathVariable("phoneId") int phoneId) {
        // PhoneNumber phoneNumber = service.findPhoneNumberById(phoneId);
        renterService.deletePhoneNumber(phoneId);
        return "redirect:/renters/telephones/{renterId}";
    }


    // Contracts...
    @GetMapping("/contracts/{renterId}")
    public String listContracts(@PathVariable("renterId") int renterId, Model theModel){
        List<Contract> contracts = renterService.findContractsByRenterId(renterId);
        Renter renter = renterService.findById(renterId);
        theModel.addAttribute("renter", renter);
        theModel.addAttribute("contracts", contracts);
        return "renters/list-contracts";
    }


    @GetMapping("/{renterId}/contracts/update/{contractId}")
    public String showUpdateContract(@PathVariable ("renterId") int renterId, @PathVariable ("contractId") int contractId, Model theModel){
        System.out.println("Inside get mapping for update contract method!");
        Contract contract = contractService.findById(contractId);
        System.out.println(contract);
        theModel.addAttribute("contract", contract);
        Renter renter = renterService.findById(renterId);
        System.out.println(renter);
        theModel.addAttribute("renter", renter);
        return "renters/update-contract";
    }


    @PostMapping("/{renterId}/contracts/update/{contractId}")
    public String updateContract(@PathVariable ("renterId") int renterId, @PathVariable ("contractId") int contractId, Contract contract, Model model) {
        System.out.println("contract=  " + contract);
        Contract contract1 = contractService.findById(contractId);
        System.out.println("contract1=  " + contract1);
        System.out.println("contractId =  " + contractId);
        if (contract.getEndDate().isBefore(contract1.getStartDate())) {
            return "redirect:/renters/{renterId}/contracts/update/{contractId}";
        }
        contractService.updateContract(contract);
        return "redirect:/renters/contracts/{renterId}";
    }
}
