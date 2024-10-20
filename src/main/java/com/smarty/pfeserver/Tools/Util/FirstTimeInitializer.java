package com.smarty.pfeserver.Tools.Util;


import com.smarty.pfeserver.Enum.User.RoleEnum;
import com.smarty.pfeserver.Models.Auth.Privilege;
import com.smarty.pfeserver.Models.Auth.Role;
import com.smarty.pfeserver.Models.Tools.PersonalInformation;
import com.smarty.pfeserver.Models.User.users;
import com.smarty.pfeserver.Services.Country.CountriesServices;
import com.smarty.pfeserver.Services.Tools.PersonalInformationService;
import com.smarty.pfeserver.Services.User.RoleService;
import com.smarty.pfeserver.Services.auth.PrivilegeService;
import com.smarty.pfeserver.Services.User.UserService;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FirstTimeInitializer implements CommandLineRunner {
    private final Log logger = LogFactory.getLog(FirstTimeInitializer.class);
    @Autowired
    private PersonalInformationService personalInformationService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    @Autowired
    private CountriesServices countriesServices;

    @Autowired
    private PrivilegeService privilegeService;


    @Value("${spring.profiles.active}")
    private String SPRING_PROFILE;
    @Value("${spring.datasource.url}")
    private String SPRING_DATA_SOURCE;
    private static final List<String> adminPrivileges = Arrays.asList("USER_READ", "SUPPLIER_READ", "USER_WRITE", "SUPPLIER_WRITE", "SUB_ADMIN_READ", "SUB_ADMIN_WRITE", "CUSTOMER_READ", "CUSTOMER_WRITE", "AGENT_READ", "AGENT_WRITE", "EVENT_WRITE");


    @Override
    public void run(String... string) throws Exception {
        if (Objects.equals(SPRING_DATA_SOURCE, "test")) {
            logger.debug("Current profile is : " + SPRING_PROFILE);
            logger.debug("Current datasource url : " + SPRING_DATA_SOURCE);
        } else {
            logger.error("Current profile is : " + SPRING_PROFILE);
            logger.error("Current datasource url : " + SPRING_DATA_SOURCE);
        }

        Roleinit();
        initPriveleges();
        //countriesServices.ParseCountry();
        initUser();


    }

    void Roleinit() {
        for (RoleEnum model : RoleEnum.values()) {
            if (!this.roleService.existsByRole(model)) {
                Role rolemodel = new Role();
                rolemodel.setRole(model);
                this.roleService.save(rolemodel);
            }
        }
    }


    void initPriveleges() {

        if (!privilegeService.existsByName("USER_READ"))
            privilegeService.save(new Privilege("USER_READ"));

        if (!privilegeService.existsByName("USER_WRITE"))
            privilegeService.save(new Privilege("USER_WRITE"));

       /* if (!privilegeService.existsByName("SUPPLIER_READ"))
            privilegeService.save(new Privilege("SUPPLIER_READ"));

        if (!privilegeService.existsByName("SUPPLIER_WRITE"))
            privilegeService.save(new Privilege("SUPPLIER_WRITE"));

        if (!privilegeService.existsByName("SUB_ADMIN_READ"))
            privilegeService.save(new Privilege("SUB_ADMIN_READ"));

        if (!privilegeService.existsByName("SUB_ADMIN_WRITE"))
            privilegeService.save(new Privilege("SUB_ADMIN_WRITE"));


        if (!privilegeService.existsByName("PRIVILEGE_WRITE"))
            privilegeService.save(new Privilege("PRIVILEGE_WRITE"));

        if (!privilegeService.existsByName("PRIVILEGE_READ"))
            privilegeService.save(new Privilege("PRIVILEGE_READ"));

        if (!privilegeService.existsByName("LOAN_READ"))
            privilegeService.save(new Privilege("LOAN_READ"));

        if (!privilegeService.existsByName("LOAN_WRITE"))
            privilegeService.save(new Privilege("LOAN_WRITE"));

        if (!privilegeService.existsByName("INVOICE_READ"))
            privilegeService.save(new Privilege("INVOICE_READ"));

        if (!privilegeService.existsByName("INVOICE_WRITE"))
            privilegeService.save(new Privilege("INVOICE_WRITE"));

        if (!privilegeService.existsByName("CUSTOMER_READ"))
            privilegeService.save(new Privilege("CUSTOMER_READ"));

        if (!privilegeService.existsByName("CUSTOMER_WRITE"))
            privilegeService.save(new Privilege("CUSTOMER_WRITE"));

        if (!privilegeService.existsByName("PURCHASE_ORDER_READ"))
            privilegeService.save(new Privilege("PURCHASE_ORDER_READ"));

        if (!privilegeService.existsByName("PURCHASE_ORDER_WRITE"))
            privilegeService.save(new Privilege("PURCHASE_ORDER_WRITE"));

        if (!privilegeService.existsByName("CHAT_READ"))
            privilegeService.save(new Privilege("CHAT_READ"));

        if (!privilegeService.existsByName("CHAT_WRITE"))
            privilegeService.save(new Privilege("CHAT_WRITE"));

        if (!privilegeService.existsByName("AGENT_READ"))
            privilegeService.save(new Privilege("AGENT_READ"));

        if (!privilegeService.existsByName("AGENT_WRITE"))
            privilegeService.save(new Privilege("AGENT_WRITE"));

        if (!privilegeService.existsByName("EVENT_READ"))
            privilegeService.save(new Privilege("EVENT_READ"));

        if (!privilegeService.existsByName("EVENT_WRITE"))
            privilegeService.save(new Privilege("EVENT_WRITE"));

        if (!privilegeService.existsByName("REQUEST_READ"))
            privilegeService.save(new Privilege("REQUEST_READ"));

        if (!privilegeService.existsByName("REQUEST_WRITE"))
            privilegeService.save(new Privilege("REQUEST_WRITE"));

        if (!privilegeService.existsByName("GLOBAL_SETTINGS_READ"))
            privilegeService.save(new Privilege("GLOBAL_SETTINGS_READ"));

        if (!privilegeService.existsByName("GLOBAL_SETTINGS_WRITE"))
            privilegeService.save(new Privilege("GLOBAL_SETTINGS_WRITE"));

        if (!privilegeService.existsByName("STATS_READ"))
            privilegeService.save(new Privilege("STATS_READ"));

        if (!privilegeService.existsByName("STATS_WRITE"))
            privilegeService.save(new Privilege("STATS_WRITE"));

        if (!privilegeService.existsByName("PROFILE_READ"))
            privilegeService.save(new Privilege("PROFILE_READ"));

        if (!privilegeService.existsByName("PROFILE_WRITE"))
            privilegeService.save(new Privilege("PROFILE_WRITE"));

        if (!privilegeService.existsByName("NOTIFICATION_READ"))
            privilegeService.save(new Privilege("NOTIFICATION_READ"));

        if (!privilegeService.existsByName("NOTIFICATION_WRITE"))
            privilegeService.save(new Privilege("NOTIFICATION_WRITE"));

        if (!privilegeService.existsByName("VEHICLE_READ"))
            privilegeService.save(new Privilege("VEHICLE_READ"));

        if (!privilegeService.existsByName("VEHICLE_WRITE"))
            privilegeService.save(new Privilege("VEHICLE_WRITE"));

        if (!privilegeService.existsByName("SUPPLIERS_CLASSIFICATION_READ"))
            privilegeService.save(new Privilege("SUPPLIERS_CLASSIFICATION_READ"));

        if (!privilegeService.existsByName("SUPPLIERS_CLASSIFICATION_WRTIE"))
            privilegeService.save(new Privilege("SUPPLIERS_CLASSIFICATION_WRTIE"));

        if (!privilegeService.existsByName("ADS_WRITE"))
            privilegeService.save(new Privilege("ADS_WRITE"));*/


        if (this.userService.existbyemail("admin@gmail.com")) {
            users user = userService.findbyemail("admin@gmail.com");
            List<Privilege> privilegeList = this.privilegeService.findAll();
            Set<Privilege> privileges = user.getPrivileges();
            for (Privilege privilege : privilegeList) {
                if (!this.privilegeService.existsByIdAndUser(privilege.getId(), user)) {
                    logger.error(privilege.getId());
                    user.getPrivileges().add(privilege);
                }
            }
            userService.UpdateUser(user);

        }


    }


    void initUser() {

        if (!userService.existbyemail("admin@gmail.com")) {

            logger.info("No users found creating some users ...");
            PersonalInformation personalInformation = new PersonalInformation();
            personalInformation.setFirstnameen("The");
            personalInformation.setLastnameen("Admin");
            PersonalInformation information = this.personalInformationService.save(personalInformation);
            users users = new users(
                    "TheAdmin",
                    "admin@gmail.com",
                    "aze",
                    "+21612345678",
                    information
            );
             userService.saveAdmin(users);

        }

    }
}
