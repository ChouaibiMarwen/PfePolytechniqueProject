package com.camelsoft.rayaserver.Controller.Public;


import com.camelsoft.rayaserver.Models.country.Root;
import com.camelsoft.rayaserver.Models.country.State;
import com.camelsoft.rayaserver.Response.Country.CountryResponse;
import com.camelsoft.rayaserver.Response.Country.CountryResult;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Services.Country.CountriesServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/country")
public class CountryController {
    @Autowired
    private CountriesServices countriesServices;
    @GetMapping(value = {"/all"})
    public ResponseEntity<DynamicResponse> all_Country(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) throws IOException {
        DynamicResponse result = this.countriesServices.get_all_countries(page,size);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping(value = {"/{country_id}"})
    public ResponseEntity<CountryResult> country_by_id(@PathVariable Long country_id) throws IOException {
        Root root = this.countriesServices.countrybyid(country_id);
        CountryResult result = new CountryResult(
                root.getTimezones(),
                root.getTranslations(),
                root.getStates()
        );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping(value = {"/by_name"})
    public ResponseEntity<CountryResult> country_by_name(@RequestParam("country_name") String country_name) throws IOException {
        Root root = this.countriesServices.countrybyname(country_name);
        CountryResult result = new CountryResult(
                root.getTimezones(),
                root.getTranslations(),
                root.getStates()
        );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping(value = {"/country_code_by_name"})
    public ResponseEntity<String> country_code_by_name(@RequestParam("country_name") String country_name) throws IOException {
        Root root = this.countriesServices.countrybyname(country_name);
        return new ResponseEntity<>(root.getPhone_code(), HttpStatus.OK);
    }


    @GetMapping(value = {"/cities_by_country/{country_name}"})
        public ResponseEntity<List<State>> getCitiesByCountry(@PathVariable String country_name) throws IOException {
      /*  List<State> result = this.countriesServices.getStatesOfCountry(country_name);
        return new ResponseEntity<>(result, HttpStatus.OK);*/
        Root root = this.countriesServices.countrybyname(country_name);
        return new ResponseEntity<>(root.getStates(), HttpStatus.OK);
    }




}
