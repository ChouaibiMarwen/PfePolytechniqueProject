package com.smarty.pfeserver.Response.Country;


import com.smarty.pfeserver.Models.country.State;
import com.smarty.pfeserver.Models.country.Timezone;
import com.smarty.pfeserver.Models.country.Translations;
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

    public CountryResult(List<State> states){
        this.states = states;
    }

}
