package com.example.demo.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

public class CountryList {

    private ArrayList<String> countries = populateList();

    public ArrayList<String> getCountries() {
        return countries;
    }

    public static ArrayList<String> populateList() {
        String[] countryCodes = Locale.getISOCountries();
        ArrayList<String> countryNames = new ArrayList<>();
        for (String countryCode : countryCodes) {
            Locale obj = new Locale("", countryCode);
            countryNames.add(obj.getDisplayCountry());
        }
        Collections.sort(countryNames);
        return countryNames;
    }
}
