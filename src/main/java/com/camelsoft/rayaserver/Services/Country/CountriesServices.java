package com.camelsoft.rayaserver.Services.Country;


import com.camelsoft.rayaserver.Models.country.*;
import com.camelsoft.rayaserver.Repository.Country.*;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;


@Service
public class CountriesServices {
    private final Log logger = LogFactory.getLog(CountriesServices.class);
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private RootRepository rootRepository;
    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private TimezoneRepository timezoneRepository;
    @Autowired
    private TranslationsRepository translationsRepository;

    public boolean existbyname(String name) {
        try {
            return this.rootRepository.existsByName(name);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }

    }



    public City citybyid(Long id) {
        try {
            return this.cityRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }

    }

    public City citybyname(String name) {
        try {
            return this.cityRepository.findByName(name);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }

    }

    public State Statebyid(Long id) {
        try {
            return this.stateRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }

    }

    public State Statebyname(String name) {
        try {
            return this.stateRepository.findTopByNameOrderById(name);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }

    }

    public Root Rootbyid(Long id) {
        try {
            return this.rootRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }

    }

    public void ParseCountry() {

        ObjectMapper objectMapper = new ObjectMapper();


        File file = new File("src/main/resources/STATIC/countries.json");

        try {

            logger.info("country file exist : " + file.exists());
            if (!file.exists()) {
                file = new File("/var/lib/jenkins/workspace/server/src/main/resources/STATIC/countries.json");
            }
            logger.info("country file exist 2 : " + file.exists());
            JsonNode rootNode = objectMapper.readTree(file);

            for (int i = 0; i < rootNode.size(); i++) {
                String countryname = rootNode.get(i).get("name").asText();
                if (this.rootRepository.existsByName(countryname))
                    continue;
                logger.info(countryname + "  not found create now !! ... itration" + i + "/" + rootNode.size());
                List<Timezone> timezoneList = new ArrayList<>();
                for (int f = 0; f < rootNode.get(i).get("timezones").size(); f++) {
                    Timezone timezone = new Timezone(
                            rootNode.get(i).get("timezones").get(f).get("zoneName").asText(),
                            rootNode.get(i).get("timezones").get(f).get("gmtOffset").asInt(),
                            rootNode.get(i).get("timezones").get(f).get("gmtOffsetName").asText(),
                            rootNode.get(i).get("timezones").get(f).get("abbreviation").asText(),
                            rootNode.get(i).get("timezones").get(f).get("tzName").asText()
                    );
                    Timezone timezoneresult = this.timezoneRepository.save(timezone);
                    timezoneList.add(timezoneresult);
                }
                Translations translations = new Translations(
                        rootNode.get(i).get("translations").get("kr") == null ? "null" : rootNode.get(i).get("translations").get("kr").asText(),
                        rootNode.get(i).get("translations").get("br") == null ? "null" : rootNode.get(i).get("translations").get("br").asText(),
                        rootNode.get(i).get("translations").get("pt") == null ? "null" : rootNode.get(i).get("translations").get("pt").asText(),
                        rootNode.get(i).get("translations").get("nl") == null ? "null" : rootNode.get(i).get("translations").get("nl").isMissingNode() ? "null" : rootNode.get(i).get("translations").get("nl").asText(),
                        rootNode.get(i).get("translations").get("hr") == null ? "null" : rootNode.get(i).get("translations").get("hr").asText(),
                        rootNode.get(i).get("translations").get("fa") == null ? "null" : rootNode.get(i).get("translations").get("fa").asText(),
                        rootNode.get(i).get("translations").get("de") == null ? "null" : rootNode.get(i).get("translations").get("de").asText(),
                        rootNode.get(i).get("translations").get("es") == null ? "null" : rootNode.get(i).get("translations").get("es").asText(),
                        rootNode.get(i).get("translations").get("fr") == null ? "null" : rootNode.get(i).get("translations").get("fr").asText(),
                        rootNode.get(i).get("translations").get("ja") == null ? "null" : rootNode.get(i).get("translations").get("ja").asText(),
                        rootNode.get(i).get("translations").get("it") == null ? "null" : rootNode.get(i).get("translations").get("it").asText(),
                        rootNode.get(i).get("translations").get("cn") == null ? "null" : rootNode.get(i).get("translations").get("cn").asText()
                );
                Translations translationsresult = this.translationsRepository.save(translations);
                List<State> stateList = new ArrayList<>();
                for (int k = 0; k < rootNode.get(i).get("states").size(); k++) {
                    State state = new State(
                            rootNode.get(i).get("states").get(k).get("name").asText(),
                            rootNode.get(i).get("states").get(k).get("state_code").asText(),
                            rootNode.get(i).get("states").get(k).get("latitude").asText(),
                            rootNode.get(i).get("states").get(k).get("longitude").asText(),
                            rootNode.get(i).get("states").get(k).get("type").asText()
                    );
                    State stateresult = this.stateRepository.save(state);
                    List<City> cities = new ArrayList<>();
                    for (int j = 0; j < rootNode.get(i).get("states").get(k).get("cities").size(); j++) {
                        City city = new City(
                                rootNode.get(i).get("states").get(k).get("cities").get(j).get("name").asText(),
                                rootNode.get(i).get("states").get(k).get("cities").get(j).get("latitude").asText(),
                                rootNode.get(i).get("states").get(k).get("cities").get(j).get("longitude").asText()
                        );
                        cities.add(city);

                    }
                    List<City> citiesrsult = this.cityRepository.saveAll(cities);
                    stateresult.setCities(citiesrsult);
                    stateresult = this.stateRepository.save(stateresult);
                    stateList.add(stateresult);

                }

                Root root = new Root(
                        countryname,
                        rootNode.get(i).get("iso3").asText(),
                        rootNode.get(i).get("iso2").asText(),
                        rootNode.get(i).get("numeric_code").asText(),
                        rootNode.get(i).get("phone_code").asText(),
                        rootNode.get(i).get("capital").asText(),
                        rootNode.get(i).get("currency").asText(),
                        rootNode.get(i).get("currency_symbol").asText(),
                        rootNode.get(i).get("tld").asText(),
                        rootNode.get(i).get("native").asText(),
                        rootNode.get(i).get("region").asText(),
                        rootNode.get(i).get("subregion").asText(),
                        timezoneList,
                        translationsresult,
                        rootNode.get(i).get("latitude").asText(),
                        rootNode.get(i).get("longitude").asText(),
                        rootNode.get(i).get("emoji").asText(),
                        rootNode.get(i).get("emojiU").asText(),
                        stateList
                );
                this.rootRepository.save(root);


            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("error : " + e.getMessage());
            logger.info("error : " + e.getCause());
            logger.info("error : " + e.getStackTrace());
        }

    }
    public void ParseCountry01() {

        ObjectMapper objectMapper = new ObjectMapper();

        File file = new File("src/main/resources/STATIC/countries.json");
        File filear = new File("src/main/resources/STATIC/arabic.json");
        File filefr = new File("src/main/resources/STATIC/fr.json");
        File filemariem = new File("src/main/resources/STATIC/mariem.json");
//        this.countryrepository01.deleteAll();

        try {

            logger.info("country file exist : " + file.exists());
            if (!file.exists()) {
                file = new File("/var/lib/jenkins/workspace/server/src/main/resources/STATIC/countries.json");
            }
            logger.info("country file exist 2 : " + file.exists());
            JsonNode rootNode = objectMapper.readTree(file);
            JsonNode rootNodear = objectMapper.readTree(filear);
            JsonNode rootNodeMariem = objectMapper.readTree(filemariem);
            JsonNode rootNodefr = objectMapper.readTree(filefr);
            logger.info("arabic  " +"/" + rootNodear.size());
            logger.info("countries  " +"/" + rootNode.size());


            for (int i = 0; i < rootNode.size(); i++) {
                String countryname = rootNode.get(i).get("name").asText();
                JsonNode arabic = null;
                JsonNode french = null;
                JsonNode mariem = null;
                for (int j = 0; j < rootNodear.size(); j++) {
                    if (rootNodear.get(j).get("code").asText().equalsIgnoreCase(rootNode.get(i).get("iso2").asText())) {
                        arabic = rootNodear.get(j);
                        logger.info(rootNodear.get(j).get("code").asText());
                        break;
                    }
                }
                for (int k = 0; k < rootNodefr.size(); k++) {
                    if (rootNodefr.get(k).get("alpha2").asText().equalsIgnoreCase(rootNode.get(i).get("iso2").asText())) {
                        french = rootNodefr.get(k);
                        logger.info( "FR"+ rootNodefr.get(k).get("alpha2").asText());
                        break;
                    }
                }
                for (int k = 0; k < rootNodeMariem.size(); k++) {
                    if (rootNodeMariem.get(k).get("iso").asText().equalsIgnoreCase(rootNode.get(i).get("iso2").asText())) {
                        mariem = rootNodeMariem.get(k);
                        logger.info( "mariem "+ rootNodeMariem.get(k).get("iso").asText());
                        break;
                    }
                }


                    logger.info(countryname + "  not found create now /api/v1/public !! ... itration" + i + "/" + rootNode.size());
                List<Timezone> timezoneList = new ArrayList<>();
                for (int f = 0; f < rootNode.get(i).get("timezones").size(); f++) {
                    Timezone timezone = this.timezoneRepository.findTopByZonenameOrderById(rootNode.get(i).get("timezones").get(f).get("zoneName").asText());
                    timezoneList.add(timezone);
                }

                List<State> stateList = new ArrayList<>();
                for (int k = 0; k < rootNode.get(i).get("states").size(); k++) {
                    State state = this.stateRepository.findTopByNameOrderById(rootNode.get(i).get("states").get(k).get("name").asText());

                    stateList.add(state);

                }
                ArrayList<String> stringarrayList=new ArrayList<>();
                if (rootNodeMariem.size()>i) {
                    if (rootNodeMariem.get(i).get("mask").isArray()) {
                        ArrayNode arrayNode = (ArrayNode) rootNodeMariem.get(i).get("mask");
                        Iterator<JsonNode> itr = arrayNode.elements();
                        for (Iterator<JsonNode> it = itr; it.hasNext(); ) {
                            stringarrayList.add(it.next().asText());


                        }
                    } else
                        stringarrayList.add(rootNodeMariem.get(i).get("mask").asText());
                }
                country01 root = new country01(
                        countryname,
                        arabic == null ? null: arabic.get("name").asText(),
                        french == null ? null: french.get("name").asText(),
                        arabic == null ? null:arabic.get("dialCode").asText(),
                        rootNode.get(i).get("currency").asText(),
                        arabic == null ? null:arabic == null ? null:arabic.get("code").asText(),
                        stringarrayList,
                        rootNode.get(i).get("currency_symbol").asText(),
                        timezoneList,
                        rootNode.get(i).get("latitude").asText(),
                        rootNode.get(i).get("longitude").asText(),
                        rootNode.get(i).get("emoji").asText(),
                        stateList
                );
//                this.countryrepository01.save(root);


            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("error : " + e.getMessage());
            logger.info("error : " + e.getCause());
            logger.info("error : " + e.getStackTrace());
        }

    }



    public City UpdateCity(City city) {
        try {
            return this.cityRepository.save(city);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }
    }

    public City SaveCity(City city) {
        try {
            return this.cityRepository.save(city);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }
    }

    public State SaveState(State state) {
        try {
            return this.stateRepository.save(state);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }
    }

    public State UpdateState(State state) {
        try {
            return this.stateRepository.save(state);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }
    }

    public Root UpdateRoot(Root root) {
        try {
            return this.rootRepository.save(root);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }
    }

    public DynamicResponse get_all_countries(int page, int size) {

        try {
            List<Root> countrieslist = new ArrayList<Root>();
            Pageable paging = PageRequest.of(page, size);
            Page<Root> pageTuts = this.rootRepository.findAll(paging);
            countrieslist = pageTuts.getContent();
            DynamicResponse countriesResponse = new DynamicResponse(
                    countrieslist,
                    pageTuts.getNumber(),
                    pageTuts.getTotalElements(),
                    pageTuts.getTotalPages()
            );
            return countriesResponse;
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }

    }

    public DynamicResponse get_all_countries_search(String name, int page, int size) {

        try {
            String output = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
            List<Root> countrieslist = new ArrayList<Root>();
            Pageable paging = PageRequest.of(page, size);
            Page<Root> pageTuts;
            if (Objects.equals(name, "")) {
                pageTuts = this.rootRepository.findAll(paging);
            } else {
                pageTuts = this.rootRepository.findAllByNameContaining(output, paging);
            }
            countrieslist = pageTuts.getContent();
            DynamicResponse countriesResponse = new DynamicResponse(
                    countrieslist,
                    pageTuts.getNumber(),
                    pageTuts.getTotalElements(),
                    pageTuts.getTotalPages()
            );
            return countriesResponse;
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }

    }

    public Root countrybyname(String name) {

        try {
            if (!this.rootRepository.existsByName(name)) {
                throw new NotFoundException(String.format("No data found"));
            }
            return this.rootRepository.findByName(name);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }

    }

    public Root countrybyid(Long id) {

        try {
            if (!this.rootRepository.existsById(id)) {
                throw new NotFoundException(String.format("No data found"));
            }
            return this.rootRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }

    }


    public List<State> getStatesOfCountry(String name) {
       Root root = this.countrybyname(name);
        return root.getStates();
    }


}
