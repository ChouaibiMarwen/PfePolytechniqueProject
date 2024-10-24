package com.smarty.pfeserver.Services.User;


import com.smarty.pfeserver.Enum.User.RoleEnum;
import com.smarty.pfeserver.Models.Auth.Role;
import com.smarty.pfeserver.Models.Auth.UserDevice;
import com.smarty.pfeserver.Models.File.MediaModel;
import com.smarty.pfeserver.Models.Tools.Address;
import com.smarty.pfeserver.Models.Tools.BankInformation;
import com.smarty.pfeserver.Models.User.users;
import com.smarty.pfeserver.Models.country.Root;
import com.smarty.pfeserver.Models.country.State;
import com.smarty.pfeserver.Repository.Auth.RoleRepository;
import com.smarty.pfeserver.Repository.Country.CityRepository;
import com.smarty.pfeserver.Repository.Tools.PersonalInformationRepository;
import com.smarty.pfeserver.Repository.User.UserRepository;
import com.smarty.pfeserver.Request.Tools.AddressRequest;
import com.smarty.pfeserver.Request.Tools.BankInformationRequest;
import com.smarty.pfeserver.Request.User.SignInRequest;
import com.smarty.pfeserver.Response.Project.DynamicResponse;
import com.smarty.pfeserver.Response.Auth.JwtResponse;
import com.smarty.pfeserver.Services.Country.CountriesServices;
import com.smarty.pfeserver.Services.Tools.AddressServices;
import com.smarty.pfeserver.Services.Tools.BankAccountService;
import com.smarty.pfeserver.Services.auth.RefreshTokenService;
import com.smarty.pfeserver.Services.auth.UserDeviceService;
import com.smarty.pfeserver.Tools.Exception.NotFoundException;
import com.smarty.pfeserver.Tools.Util.BaseController;
import com.smarty.pfeserver.Tools.Util.TokenUtil;
import com.smarty.pfeserver.Tools.Util.UserPrincipal;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
public class UserService extends BaseController implements UserDetailsService {
    private final Log logger = LogFactory.getLog(UserService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PersonalInformationRepository personalInformationRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDeviceService deviceService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CountriesServices  countriesServices;
    @Autowired
    private AddressServices addressServices;

    public users saveUser(users user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            Role userRole = roleRepository.findByRole(RoleEnum.ROLE_USER);
            user.setRole(userRole);
            return userRepository.save(user);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException("not found data");
        }
    }



    public users saveTechnicien(users user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            Role userRole = roleRepository.findByRole(RoleEnum.ROLE_TECHNICIEN);
            user.setRole(userRole);
            return userRepository.save(user);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException("not found data");
        }
    }

    public users findTop() {
        try {
            return this.userRepository.findTopByOrderByIdDesc();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }
    }

    public users saveAdmin(users users) {
        try {
            users.setPassword(passwordEncoder.encode(users.getPassword()));
            users.setActive(true);
            Role userRole = roleRepository.findByRole(RoleEnum.ROLE_ADMIN);
            users.setRole(userRole);
            return userRepository.save(users);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException("not found data");
        }
    }



    public users UpdateUser(users users) {
        try {
            return userRepository.save(users);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException("not found data");
        }
    }

    public void update_password(users user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            this.userRepository.save(user);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException("not found data");
        }
    }


    public static String generateRandomNumberString(int length) {
        // Define characters for digits (0-9)
        String digits = "0123456789";

        // Create a Random object
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(digits.length());
            char randomDigit = digits.charAt(randomIndex);
            sb.append(randomDigit);
        }
        // Convert StringBuilder to a String
        return sb.toString();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        users user = this.userRepository.findByEmail(username);
        if (user == null) {
            user = this.userRepository.findByUsername(username);
        }
        if (user == null)
            user = this.userRepository.findByPhonenumber(username);

        if (user == null && username != null) {
            String[] parts = username.split("@");
            user = new users();
            String name = parts[0];
            user.setEmail(username);
            user.setUsername(name);
            user.setName(name);
            user.setActive(true);
            Role userRole = roleRepository.findByRole(RoleEnum.ROLE_USER);
            user.setRole(userRole);
            user = this.userRepository.save(user);
        }
        if (user != null) {
            return UserPrincipal.create(user);
        } else {
            throw new UsernameNotFoundException("Email not found");
        }
    }

    public static boolean isValidEmail(String email) {
        // Define the regular expression pattern for a valid email address
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";

        // Compile the pattern
        Pattern pattern = Pattern.compile(emailRegex);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(email);

        // Return true if the email matches the pattern, otherwise return false
        return matcher.matches();
    }

    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
        for (Role role : userRoles) {
            roles.add(new SimpleGrantedAuthority(role.getRole().name()));
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
        return grantedAuthorities;
    }

    private UserDetails buildUserForAuthentication(users users, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(users.getUsername(), users.getPassword(),
                users.getActive(), true, true, true, authorities);
    }

    public String GenerateUserName(String name, Long iduser) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(4);
        for (int i = 0; i < 4; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        String username = trimAllWhitespace(name).toUpperCase(Locale.ROOT) + iduser + sb;
        return username;

    }

    public void Delete(users user) {
        try {
            this.userRepository.delete(user);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("Could not find this user"));
        }
    }

    public static String trimAllWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }

        int len = str.length();
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (!Character.isWhitespace(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static boolean hasLength(String str) {
        return (str != null && !str.isEmpty());
    }

    public users findByUserName(String username) {
        try {
            users user = this.userRepository.findByUsername(username);
            if (user == null)
                user = this.userRepository.findByEmail(username);
            return user;
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No user found with username [%s] in our data base", username));
        }

    }

    public Long totalUsers() {
        try {
            Role userRole = roleRepository.findByRole(RoleEnum.ROLE_USER);
            return this.userRepository.countAllByRole(userRole);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException("hourbor id not found data");
        }
    }



    public Long totalUsersByState(Boolean state) {
        try {
            Role userRole = roleRepository.findByRole(RoleEnum.ROLE_USER);
            return this.userRepository.countAllByRoleAndActive(userRole, state);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException("hourbor id not found data");
        }
    }



    public List<users> allUsers(Boolean active) {
        try {
            Role userRole = roleRepository.findByRole(RoleEnum.ROLE_USER);

            return this.userRepository.findAllByRoleAndActive(userRole, active);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException("hourbor id not found data");
        }
    }


    public List<users> allusersByRole(RoleEnum role) {
        try {
            Role userRole = roleRepository.findByRole(role);
            if(userRole == null)
                throw new NotFoundException("Role is not found");

            return this.userRepository.findByRoleAndDeletedIsFalse(userRole);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException("hourbor id not found data");
        }
    }


    public List<users> allusers() {
        try {
            return this.userRepository.findByDeletedIsFalseAndActiveIsTrue();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException("hourbor id not found data");
        }
    }

    public Long countusersbyRole(RoleEnum roleEnums) {
         Role role =  this.roleRepository.findByRole(roleEnums);
        return this.userRepository.countAllByRole(role);
    }

    public List<users> getUsersByRoles(List<RoleEnum> roleEnums) {
        try {
            List<Role> roles = roleEnums.stream()
                    .map(roleRepository::findByRole)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (roles.isEmpty()) {
                return Collections.emptyList();
            }

            return userRepository.findByRoleInAndDeletedIsFalse(roles);
        } catch (Exception ex) {
            throw new RuntimeException("Error retrieving users by roles: " + roleEnums, ex);
        }
    }

    public users findbyemail(String email) {
        try {
            return userRepository.findByEmail(email);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }

    }


   public DynamicResponse filterAllUser(int page, int size, Boolean active, String name, RoleEnum role, Boolean verified) {
       try {
           Role userRole = roleRepository.findByRole(role);
           Page<users> user = null;

           if (name == null && active == null && verified == null) {
               user = this.userRepository.findByRoleAndActiveAndDeletedOrderByTimestmpDesc(PageRequest.of(page, size), userRole, true, false);
           } else if (name == null && active == null && verified != null) {
               user = this.userRepository.findByRoleAndActiveAndDeletedAndVerifiedOrderByTimestmpDesc(PageRequest.of(page, size), userRole, true, false, verified);
           } else if (name != null && active == null && verified == null) {
               // Partial name search (case-insensitive)
               user = this.userRepository.findAllByRoleAndNameContainingIgnoreCaseAndDeletedOrderByTimestmpDesc(PageRequest.of(page, size), userRole, name, false);
           } else if (name != null && active == null && verified != null) {
               // Partial name search with verified
               user = this.userRepository.findAllByRoleAndNameContainingIgnoreCaseAndDeletedAndVerifiedOrderByTimestmpDesc(PageRequest.of(page, size), userRole, name, false, verified);
           } else if (name == null && active != null && verified == null) {
               user = this.userRepository.findByRoleAndActiveAndDeletedOrderByTimestmpDesc(PageRequest.of(page, size), userRole, active, false);
           } else if (name == null && active != null && verified != null) {
               user = this.userRepository.findByRoleAndActiveAndDeletedAndVerifiedOrderByTimestmpDesc(PageRequest.of(page, size), userRole, active, false, verified);
           } else if (name != null && active != null && verified == null) {
               // Partial name search with active status
               user = this.userRepository.findAllByRoleAndActiveAndNameContainingIgnoreCaseAndDeletedOrderByTimestmpDesc(PageRequest.of(page, size), userRole, active, name, false);
           } else if (name != null && active != null && verified != null) {
               // Partial name search with active status and verified
               user = this.userRepository.findAllByRoleAndActiveAndNameContainingIgnoreCaseAndDeletedAndVerifiedOrderByTimestmpDesc(PageRequest.of(page, size), userRole, active, name, false, verified);
           }

           return new DynamicResponse(user.getContent(), user.getNumber(), user.getTotalElements(), user.getTotalPages());
       } catch (NoSuchElementException ex) {
           throw new NotFoundException("No data found");
       }
   }






    public DynamicResponse findAllDeletedUsers(int page, int size, String name) {
        try {
            Role userRole = roleRepository.findByRole(RoleEnum.ROLE_USER);
            Page<users> user = null;
            if (name == null)
                user = this.userRepository.findByRoleAndDeletedAndUsernameNotLikeIgnoreCaseOrderByTimestmpDesc(PageRequest.of(page, size), userRole, true, "%DELETED%");
            else if (name != null)
                user = this.userRepository.findAllByRoleAndEmailLikeIgnoreCaseAndDeletedAndUsernameNotLikeIgnoreCaseOrderByTimestmpDesc(PageRequest.of(page, size), userRole, "%" + name + "%", true, "%DELETED%");
            else
                user = this.userRepository.findAllByRoleAndNameContainingIgnoreCaseAndDeletedAndUsernameNotLikeIgnoreCaseOrderByTimestmpDesc(PageRequest.of(page, size), userRole, name, false, "%DELETED%");

            return new DynamicResponse(user.getContent(), user.getNumber(), user.getTotalElements(), user.getTotalPages());
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }

    }


    public DynamicResponse findAllAdmins(int page, int size, Boolean active, String name) {
        try {
            Role userRole = roleRepository.findByRole(RoleEnum.ROLE_ADMIN);
            Page<users> user = null;
            if (name == null && active == null)
                user = this.userRepository.findByRoleOrderByTimestmpDesc(PageRequest.of(page, size), userRole);
            else if (name != null && active == null)
                user = this.userRepository.findAllByRoleAndEmailLikeIgnoreCaseAndDeletedAndUsernameNotLikeIgnoreCaseOrderByTimestmpDesc(PageRequest.of(page, size), userRole, "%" + name + "%", false, "%DELETED%");
            else if (name == null && active != null)
                user = this.userRepository.findByRoleAndActiveAndDeletedOrderByTimestmpDesc(PageRequest.of(page, size), userRole, active, false);
            else
                user = this.userRepository.findAllByRoleAndActiveAndEmailLikeIgnoreCaseAndDeletedOrderByTimestmpDesc(PageRequest.of(page, size), userRole, active, "%" + name + "%", false);

            return new DynamicResponse(user.getContent(), user.getNumber(), user.getTotalElements(), user.getTotalPages());
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }

    }

    public users findFirstAdmin() {
        Role userRole = roleRepository.findByRole(RoleEnum.ROLE_ADMIN);
        users firstAdmin = userRepository.findTopByRoleAndDeletedIsFalse(userRole);

        return firstAdmin;
    }


    public List<users> findAllAdmin() {
        try {
            Role userRole = roleRepository.findByRole(RoleEnum.ROLE_ADMIN);
            return this.userRepository.findAllByRole(userRole);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }

    }


    public int countusers() {
        try {
            return userRepository.findAll().size();

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }
    }

    public Long Count() {
        try {
            return userRepository.count();

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }
    }




    public JwtResponse authprocessUsernameAndPassword(SignInRequest signInRequest) throws JSONException {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = loadUserByUsername(signInRequest.getUsername());
        users user = userRepository.findByUsername(userDetails.getUsername());
        if (user == null)
            return null;
        List<UserDevice> userDeviceList = this.deviceService.findbyuser(user);
        if (userDeviceList != null && !userDeviceList.isEmpty())
            this.deviceService.deleteexpirationtoken(userDeviceList, userDetails);
        String tokenGeneration = tokenUtil.generateToken(userDetails);
        String token = tokenGeneration;
//        Date expiraydate = tokenGeneration.getExpirationdate();
        String refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername()).getToken();
        String roles;
        try {
            roles = userDetails.getAuthorities().stream().findFirst().get().toString();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("user messing data"));
        }

        UserDevice device = new UserDevice(user, signInRequest.getDeviceType(), signInRequest.getDeviceId(), signInRequest.getIp(), token);
        if (signInRequest.getTokendevice() != null) {
            device.setTokendevice(signInRequest.getTokendevice());
        }

        UserDevice result_device = this.deviceService.save(device);
        JwtResponse response = new JwtResponse(token, refreshToken, roles, result_device.getDeviceId(), result_device.getDeviceType(), result_device.getIp(), null);
        return response;
    }


    public List<users> findAll() {
        try {
            return this.userRepository.findAll();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }
    }


    public boolean existbyemail(String email) {
        try {
            return userRepository.existsByEmail(email);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }

    }

    public users findByPhonenumber(String email) {
        try {
            return userRepository.findByPhonenumber(email);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }

    }

    public boolean existbyphonenumber(String phonenumber) {
        try {
            return userRepository.existsByPhonenumber(phonenumber);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }

    }

    public boolean existbyid(Long id) {
        try {
            return userRepository.existsById(id);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }

    }

    public users findById(Long userid) {
        try {
            if(!this.userRepository.existsById(userid))
                throw new NotFoundException(String.format("user with id " + userid + " is not found " ));
            return userRepository.findById(userid).get();

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }
    }

    public void updatepassword(users user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);
    }

    public users updateVerifiedUser(Long userid){
        users user =  findById(userid);
        Boolean currentVerified = user.getVerified();

        user.setVerified(currentVerified == null || !currentVerified);

        return this.userRepository.save(user);

    }

    public users updateActivatedUser(Long userid){
        users user =  findById(userid);
        user.setActive(!user.getActive());
        return this.userRepository.save(user);

    }


    public Set<Address> getUserAddress(Long iduser){
       users user = findById(iduser);
       return  user.getAddresses();

    }



    public BankInformation addBankAccounToUser(users user, BankInformationRequest bankInformationRequest) {
        BankInformation bankInformation = new BankInformation();
        bankInformation.setBankname(bankInformationRequest.getBank_name());
        bankInformation.setAccountname(bankInformationRequest.getAccountHolderName());
        bankInformation.setIban(bankInformationRequest.getIban());
        bankInformation.setRip(bankInformationRequest.getAcountNumber());
        MediaModel resourceMedia = null;
        bankInformation.setUser(user);
        return this.bankAccountService.saveBankInformation(bankInformation);

    }



    public Address addAddressToUser(users user, AddressRequest addressRequest) {
        Address address = new Address();
        address.setAddressline1(addressRequest.getAddressline1());
        address.setAddressline2(addressRequest.getAddressline2());
        address.setPostcode(addressRequest.getPostcode());
        address.setBuilding(addressRequest.getBuilding());
        address.setUnitnumber(addressRequest.getUnitnumber());
        address.setStreetname(addressRequest.getStreetname());
        address.setPrimaryaddress(addressRequest.getPrimaryaddress());
        State city = this.countriesServices.Statebyname(addressRequest.getCityName());
        Root country = this.countriesServices.countrybyname(addressRequest.getCountryName());
        if(city==null || country==null)
            return null;
        address.setCity(city);
        address.setCountry(country);
        address.setUser(user);
        return this.addressServices.save(address);

    }

    public users deleteUser(users user){
        long date = new Date().getTime();
        user.setActive(false);
        user.setDeleted(true);
        user.setEmail(user.getEmail()+date);
        user.setPhonenumber(user.getPhonenumber()+date);
        user.setUsername(user.getUsername()+date);
         return UpdateUser(user);
    }

}
