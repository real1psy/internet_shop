package com.example2.demo2.services.impls;

import com.example2.demo2.entities.Brand;
import com.example2.demo2.repositories.BrandRepository;
import com.example2.demo2.services.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public List<Brand> listAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Brand listBrand(Long id) {
        return brandRepository.getOne(id);
    }

    @Override
    public void deleteBrand(Long id) {
        brandRepository.deleteById(id);
    }

    @Override
    public void saveBrand(Brand brand) {
        brandRepository.save(brand);
    }

    @Override
    public boolean BrandExists(Long id){
        if(brandRepository.checkBrand(id)>0){
            return true;
        }
        return false;
    }

    @Override
    public List<Brand> findBrandsCountryId(Long id){
        return brandRepository.findAllByCountry_Id(id);
    }

}
