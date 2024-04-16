package com.camelsoft.rayaserver.Tools.Util;


import com.camelsoft.rayaserver.Enum.User.Gender;
import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Models.Auth.Privilege;
import com.camelsoft.rayaserver.Models.Auth.Role;
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Repository.Auth.RoleRepository;
import com.camelsoft.rayaserver.Repository.User.UserRepository;
import com.camelsoft.rayaserver.Services.Country.CountriesServices;
import com.camelsoft.rayaserver.Services.auth.PrivilegeService;
import com.camelsoft.rayaserver.Services.User.UserService;
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
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
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
        initUser();
        countriesServices.ParseCountry();
    }

    void Roleinit() {
        for (RoleEnum model : RoleEnum.values()) {
            if (!roleRepository.existsByRole(model.name())) {
                Role rolemodel = new Role();
                rolemodel.setRole(model.name());
                roleRepository.save(rolemodel);
            }
        }
    }


    void initPriveleges() {

        if (!privilegeService.existsByName("USER_READ"))
            privilegeService.save(new Privilege("USER_READ"));

        if (!privilegeService.existsByName("USER_WRITE"))
            privilegeService.save(new Privilege("USER_WRITE"));

        if (!privilegeService.existsByName("SUPPLIER_READ"))
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


        if (userRepository.existsByEmail("info@camel-soft.com")) {
            users user = userService.findbyemail("info@camel-soft.com");
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
        if (!userRepository.existsByEmail("admin@camel-soft.com")) {
            logger.info("No users found creating some users ...");
            users users = new users(
                    "CAADMINSO1234",
                    "admin@camel-soft.com",
                    "aze",
                    "camelsoft llc",
                    Gender.MALE,
                    "93831879",
                    "Tunisia"
            );

            userService.saveUser(users);

        }

        if (!userRepository.existsByEmail("info@camel-soft.com")) {
            logger.info("No users found creating some users ...");
            users users = new users(
                    "CASOZ1234",
                    "info@camel-soft.com",
                    "aze",
                    "info llc",
                    Gender.MALE,
                    "93831879",
                    "Tunisia"
            );
            userService.saveAdmin(users);
        }

        if (!userRepository.existsByEmail("hr@camel-soft.com")) {
            users useruser = new users(
                    "CASO0051234",
                    "hr@camel-soft.com",
                    "aze",
                    "hr llc",
                    Gender.MALE,
                    "93831879",
                    "Tunisia"
            );
            Supplier supplier = new Supplier();
            useruser.setSupplier(supplier);
            supplier.setUser(useruser);
            userService.saveSupplier(useruser);
        }


        if (!userRepository.existsByEmail("hr1@camel-soft.com")) {
            users useruser = new users(
                    "CAS1O0051234",
                    "hr1@camel-soft.com",
                    "aze",
                    "hr llc",
                    Gender.MALE,
                    "93831879",
                    "Tunisia"
            );
            Supplier supplier = new Supplier();
            useruser.setSupplier(supplier);
            supplier.setUser(useruser);
            userService.saveSupplier(useruser);
        }


    }
}
