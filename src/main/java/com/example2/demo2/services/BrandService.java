package com.example2.demo2.services;

import com.example2.demo2.entities.Brand;

import java.util.List;

public interface BrandService {
    List<Brand> listAllBrands();
    Brand listBrand(Long id);
    void deleteBrand(Long id);
    void saveBrand(Brand brand);

    boolean BrandExists(Long id);
    List<Brand>findBrandsCountryId(Long id);


}
