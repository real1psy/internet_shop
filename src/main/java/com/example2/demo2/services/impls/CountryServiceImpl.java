package com.example2.demo2.services.impls;

import com.example2.demo2.entities.Country;
import com.example2.demo2.repositories.CountryRepository;
import com.example2.demo2.services.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CountryServiceImpl implements CountryService {
    @Autowired
    private CountryRepository countryRepository;

    @Override
    public List<Country> listAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    public Country listCountry(Long id) {
        return countryRepository.getOne(id);
    }

    @Override
    public void deleteCountry(Long id) {
        countryRepository.deleteById(id);
    }

    @Override
    public void saveCountry(Country country) {
        countryRepository.save(country);
    }

    @Override
    public boolean CountryExists(Long id) {
        if(countryRepository.checkCountry(id)>0){
            return true;
        }
        return false;
    }
}
