package com.example2.demo2.services;

import com.example2.demo2.entities.Country;

import java.util.List;

public interface CountryService {

    List<Country> listAllCountries();
    Country listCountry(Long id);
    void deleteCountry(Long id);
    void saveCountry(Country country);

    boolean CountryExists(Long id);

}
