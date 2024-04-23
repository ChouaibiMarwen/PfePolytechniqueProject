package com.camelsoft.rayaserver.Response.Country;


import com.camelsoft.rayaserver.Models.country.State;
import com.camelsoft.rayaserver.Models.country.Timezone;
import com.camelsoft.rayaserver.Models.country.Translations;
import lombok.Data;

import java.util.List;

@Data
public class CountryResult {
    private List<Timezone> timezones;
    private Translations translations;
    private List<State> states;
    public CountryResult() {
    }

    public CountryResult(List<Timezone> timezones, Translations translations, List<State> states) {
        this.timezones = timezones;
        this.translations = translations;
        this.states = states;
    }

}
