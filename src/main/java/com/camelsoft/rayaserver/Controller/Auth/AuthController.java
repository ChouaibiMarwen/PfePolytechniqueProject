package com.camelsoft.rayaserver.Controller.Auth;

import com.camelsoft.rayaserver.Models.Auth.PasswordResetToken;
import com.camelsoft.rayaserver.Models.Auth.RefreshToken;
import com.camelsoft.rayaserver.Models.Auth.UserDevice;
import com.camelsoft.rayaserver.Models.User.Supplier;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Request.User.LogOutRequest;
import com.camelsoft.rayaserver.Request.User.SignInRequest;
import com.camelsoft.rayaserver.Request.auth.TokenRefreshRequest;
import com.camelsoft.rayaserver.Response.Auth.JwtResponse;
import com.camelsoft.rayaserver.Response.Auth.OnUserLogoutSuccessEvent;
import com.camelsoft.rayaserver.Response.Tools.ApiResponse;
import com.camelsoft.rayaserver.Services.User.SupplierServices;
import com.camelsoft.rayaserver.Services.User.UserService;
import com.camelsoft.rayaserver.Services.auth.MailSenderServices;
import com.camelsoft.rayaserver.Services.auth.PasswordResetTokenServices;
import com.camelsoft.rayaserver.Services.auth.RefreshTokenService;
import com.camelsoft.rayaserver.Services.auth.UserDeviceService;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import com.camelsoft.rayaserver.Tools.Exception.TokenRefreshException;
import com.camelsoft.rayaserver.Tools.Exception.UserLogoutException;
import com.camelsoft.rayaserver.Tools.Util.BaseController;
import com.camelsoft.rayaserver.Tools.Util.TokenUtil;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;


@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/auth")
public class AuthController extends BaseController {
    private final Log logger = LogFactory.getLog(AuthController.class);

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private SupplierServices supplierServices;
    @Autowired
    private UserDeviceService userDeviceService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordResetTokenServices resetTokenServices;

    @Autowired
    private UserDeviceService deviceService;

    @Autowired
    private MailSenderServices mailSenderServices;




    String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";

    @GetMapping(value = {"/current_user"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_ADMIN') or hasRole('SUB_DEALER') or hasRole('SUB_SUB_DEALER')")
    public ResponseEntity<users> current_user() throws IOException {
        users users = userService.findByUserName(getCurrentUser().getUsername());
    /*    if(users.getSupplier() != null){
            users.getSupplier().setUserId(users.getId());
            users.getSupplier().setName(users.getPersonalinformation().getFirstnameen() + " " + users.getPersonalinformation().getLastnameen());

        }*/
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @PostMapping(value = {"/signin"})
    public ResponseEntity<JwtResponse> signin(@RequestBody(required = false) SignInRequest signInRequest) throws JSONException {
        if (!this.userService.existbyemail(signInRequest.getUsername()))
            return new ResponseEntity("Wrong userName ", HttpStatus.BAD_REQUEST);
        users usar = this.userService.findbyemail(signInRequest.getUsername());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(signInRequest.getPassword(), usar.getPassword()))
            return new ResponseEntity("Wrong Password ", HttpStatus.BAD_REQUEST);
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getUsername().toLowerCase(), signInRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userService.loadUserByUsername(signInRequest.getUsername().toLowerCase());
        users user = userService.findByUserName(userDetails.getUsername());
        List<UserDevice> userDeviceList = this.deviceService.findbyuser(user);
        for (UserDevice device : userDeviceList)
            if (device.getToken() == null)
                this.deviceService.Delete(device);

        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        String token = tokenUtil.generateToken(userDetails);
        String refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername()).getToken();
        String roles;
        try {

                roles = userDetails.getAuthorities().stream().findFirst().get().toString();
                System.out.println("roles");
                System.out.println(roles);
        } catch (NoSuchElementException ex) {
            System.out.println("role33s");
            System.out.println(ex);
            throw new NotFoundException(String.format("user messing data"));
        }
        UserDevice device = new UserDevice(user, signInRequest.getDeviceType(), signInRequest.getDeviceId(), signInRequest.getIp(), token, signInRequest.getTokendevice());
        UserDevice result_device = this.deviceService.save(device);
        JwtResponse response = new JwtResponse(token, refreshToken, roles, result_device.getDeviceId(), result_device.getDeviceType(), result_device.getIp(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = {"/add_device_token"})
    public ResponseEntity<JwtResponse> add_device_token(@RequestBody(required = false) SignInRequest signInRequest) throws JSONException {

        users user = userService.findByUserName(getCurrentUser().getUsername());
        if (user == null)
            return new ResponseEntity("this user not found", HttpStatus.NOT_FOUND);
        UserDevice device = new UserDevice(user, signInRequest.getDeviceType(), signInRequest.getDeviceId(), signInRequest.getIp(), null, signInRequest.getTokendevice());
        UserDevice result_device = this.deviceService.save(device);
        JwtResponse response = new JwtResponse(null, null, null, result_device.getDeviceId(), result_device.getDeviceType(), result_device.getIp(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        UserDevice device = this.deviceService.findbytoken(request.getToken());

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = tokenUtil.generateToken(userService.loadUserByUsername(user.getUsername()));
                    String roles = userService.loadUserByUsername(user.getUsername()).getAuthorities().stream().findFirst().get().toString();
                    if (device == null) {
                        UserDevice devices = new UserDevice(user, request.getDeviceType(), request.getDeviceId(), request.getIp(), token, request.getTokendevice());
                        this.deviceService.save(devices);
                    } else {
                        device.setToken(token);
                        this.deviceService.update(device);
                    }

                    return ResponseEntity.ok(new JwtResponse(token, requestRefreshToken, roles, device.getDeviceId(), device.getDeviceType(), device.getIp(), null));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @PostMapping(value = {"/resend_code"})
    public ResponseEntity<users> resend_code(@RequestParam String email) throws IOException, InterruptedException, MessagingException {
        users user = userService.findbyemail(email.toLowerCase());
        String token = generateRandomResetCode(4);
        PasswordResetToken resetToken = this.resetTokenServices.findbyuser(user);
        if (resetToken != null)
            this.resetTokenServices.remove_code(resetToken.getUser());

        resetToken = this.resetTokenServices.createPasswordResetTokenForUser(user, token);


        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public static String generateRandomResetCode(int length) {
        if (length <= 0)
            throw new IllegalArgumentException("Length must be greater than zero.");

        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomNumber = random.nextInt(10); // Generates random number from 0 to 9
            sb.append(randomNumber);
        }

        return sb.toString();
    }
    @PostMapping(value = {"/reset_password_first_step"})
    public ResponseEntity reset_password_first_step(@RequestParam("email") String email) throws IOException, MessagingException {
        if (!this.userService.existbyemail(email))
            return new ResponseEntity("user not found", HttpStatus.NOT_FOUND);
        users user = userService.findbyemail(email);
        String token = PasswordResetTokenServices.generateRandomResetCode(4);
        PasswordResetToken resetToken = this.resetTokenServices.findbyuser(user);
        if (resetToken != null) {
            this.resetTokenServices.remove_code(resetToken.getUser());
        }
        resetToken = this.resetTokenServices.createPasswordResetTokenForUser(user, token);
        this.mailSenderServices.sendEmailResetPassword(resetToken.getToken(), user, resetToken.getExpiryDate());
        //sendEmailWithAttachment();
        System.out.println("Done");
        return new ResponseEntity("code send it to email , the code wille be expired in : " + resetToken.getExpiryDate(), HttpStatus.OK);
    }

    @PostMapping(value = {"/validate_reset_code_second_step"})
    public ResponseEntity<String> validate_reset_code_second_step(@RequestParam("email") String email, @RequestParam("reset_code") String code) throws IOException {
        if (!this.userService.existbyemail(email))
            return new ResponseEntity("user not found", HttpStatus.NOT_FOUND);
        users user = userService.findbyemail(email);
        PasswordResetToken resetToken = this.resetTokenServices.findbyuser(user);
        if (resetToken == null) {
            return new ResponseEntity("this code is not found", HttpStatus.NOT_FOUND);
        }
        String result = this.resetTokenServices.validatePasswordResetToken(user, code);
        if (result == null)
            return new ResponseEntity<>("code correct", HttpStatus.OK);
        if (result.equals("expired"))
            return new ResponseEntity("this code is expired", HttpStatus.NOT_ACCEPTABLE);
        if (result.equals("invalidToken"))
            return new ResponseEntity("this code is not valid", HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity("this code is wrong", HttpStatus.NOT_ACCEPTABLE);
    }
    @PutMapping(value = {"/logout"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('SUPPLIER') or hasRole('SUB_SUPPLIER') or hasRole('SUB_ADMIN')")
    public ResponseEntity<ApiResponse> logoutUser(@Valid @RequestBody LogOutRequest logOutRequest) {
        users currentUser = userService.findByUserName(getCurrentUser().getUsername());
        String deviceId = logOutRequest.getDeviceInfo().getDeviceId();
        if (deviceId == null)
            return new ResponseEntity("device id not found", HttpStatus.BAD_REQUEST);

        if (!this.userDeviceService.existbytoken(logOutRequest.getToken()))
            return new ResponseEntity("user device not found", HttpStatus.BAD_REQUEST);

        if (this.userDeviceService.findbytoken(logOutRequest.getToken()) == null)
            return new ResponseEntity("user device not found", HttpStatus.BAD_REQUEST);


        UserDevice userDevice = this.userDeviceService.findbytoken(logOutRequest.getToken());
        if (userDevice != null && userDevice.getDeviceId().equals(deviceId)) {
            OnUserLogoutSuccessEvent logoutSuccessEvent = new OnUserLogoutSuccessEvent(currentUser.getEmail(), logOutRequest.getToken(), logOutRequest);
            applicationEventPublisher.publishEvent(logoutSuccessEvent);
            this.userDeviceService.Delete(userDevice);
            return ResponseEntity.ok(new ApiResponse(true, "User has successfully logged out from the system!"));
        } else {
            throw new UserLogoutException(logOutRequest.getDeviceInfo().getDeviceId(), "Invalid device Id supplied. No matching device found for the given user ");
        }

    }

    @PostMapping(value = {"/change_password_final_step"})
    public ResponseEntity change_password_final_step(@RequestParam("email") String email, @RequestParam("reset_code") String code, @RequestParam("password") String new_password) throws IOException {
        if (!this.userService.existbyemail(email))
            return new ResponseEntity("user not found", HttpStatus.NOT_FOUND);
        users user = userService.findbyemail(email);
        PasswordResetToken resetToken = this.resetTokenServices.findbyuser(user);
        if (resetToken == null) {
            return new ResponseEntity("this code is not found", HttpStatus.NOT_FOUND);
        }
        String result = this.resetTokenServices.validatePasswordResetToken(user, code);
        if (result == null) {
            this.userService.updatepassword(user, new_password);
            this.resetTokenServices.remove_code(resetToken.getUser());
            return new ResponseEntity("password updated successfully !!", HttpStatus.OK);
        }
        if (result.equals("expired"))
            return new ResponseEntity("this code is expired", HttpStatus.NOT_ACCEPTABLE);
        if (result.equals("invalidToken"))
            return new ResponseEntity("this code is not valid", HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity("this code is wrong", HttpStatus.NOT_ACCEPTABLE);
    }



  /*  @PostMapping(value = {"/send_password-via-email"})
    public ResponseEntity send_passwordviaemail(@RequestParam("email") String email) throws IOException, MessagingException {
        if (!this.userService.existbyemail(email.toLowerCase()))
            return new ResponseEntity("no user found with this email: "+ email, HttpStatus.NOT_FOUND);
        users user = userService.findbyemail(email.toLowerCase());
        String token = UserService.generateRandomNumberString(8);
        PasswordResetToken resetToken = this.resetTokenServices.findbyuser(user);
        if (resetToken != null) {
            this.resetTokenServices.remove_code(resetToken.getUser());
        }
        resetToken = this.resetTokenServices.createPasswordResetTokenForUser(user, token);
        this.mailSenderServices.emailnewpassword(user.getName(), email, );


        return new ResponseEntity("code send it to email , the code wille be expired in : " + resetToken.getExpiryDate(), HttpStatus.OK);
    }*/

}
