package com.busal.basicAccountHandling.controllers;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.busal.basicAccountHandling.DataTransferObject.UserDTO;
import com.busal.basicAccountHandling.models.Inventory;
import com.busal.basicAccountHandling.models.LoginDetail;
import com.busal.basicAccountHandling.models.Subscription;
import com.busal.basicAccountHandling.models.UserAccount;
import com.busal.basicAccountHandling.repository.*;

import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class AccountsController {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[~!@#$%^&*()\\-_=+\\[\\]{}\\\\|;:'\",<.>/?])[A-Za-z\\d~!@#$%^&*()\\-_=+\\[\\]{}\\\\|;:'\",<.>/?]{8,}$");

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
    private boolean isValidPassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    @Autowired
    AccountsRepository accRepo;

    @Autowired
    FindAccountRepository findAccRepo;

    @Autowired
    InventoryRepository inventoryRepo;

    @Autowired
    SubscriptionRespository subscriptionRepo;
    
    @Autowired
    private ModelMapper modelMapper;

    @ApiIgnore
    @RequestMapping(value="/")
    public void redirect(HttpServletResponse response) throws IOException{
        response.sendRedirect("/swagger-ui.html");
    }

    @GetMapping("/displayAllAccounts")
    public List<UserAccount> getAllAccounts(){
        return accRepo.findAll();
    }

    @PostMapping("/createAccount")
    public ResponseEntity<Map<String, String>> createAccount(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult){
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();
        Map<String, String> response = new HashMap<>();

        if(!isValidEmail(email)){
            response.put("Message", "Invalid Email Format");
            return ResponseEntity.badRequest().body(response);
        }

        if(!isValidPassword(password)){
            response.put("Message", "Password must have: At least 8 characters long, at least one uppercase letter,  at least one lowercase letter, at least one digit, at least one special character");
            return ResponseEntity.badRequest().body(response);
        }

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        
        List<UserAccount> existingAccounts = findAccRepo.findByEmail(email);
        if (!existingAccounts.isEmpty() && existingAccounts.get(0).getEmail().equals(email)) {
            response.put("Message", "Account with the email already exists");
            return ResponseEntity.badRequest().body(response);
        }

        UserAccount userAccount = new UserAccount();
        userAccount.setEmail(userDTO.getEmail());
        userAccount.setbarangay(userDTO.getBarangay());
        userAccount.setBirthdate(userDTO.getBirthdate());
        userAccount.setCity(userDTO.getCity());
        userAccount.setFirstName(userDTO.getFirstName());
        userAccount.setLastName(userDTO.getLastName());
        userAccount.setMiddleName(userDTO.getMiddleName());
        userAccount.setPassword(userDTO.getPassword());
        userAccount.setProvince(userDTO.getProvince());
        userAccount.setStreet(userDTO.getStreet());
        userAccount.setType(userDTO.getType());

        accRepo.save(userAccount);
        response.put("Message", "Account created successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/deleteUserByEmail/{email}")
    public ResponseEntity<String> deleteUserByEmail(@PathVariable String email) {
        if (accRepo.existsByEmail(email)) {
            accRepo.deleteByEmail(email);
            return ResponseEntity.ok("User account deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User account not found");
        }
    }

    @PutMapping("/updateAccount/{email}")
    public ResponseEntity<String> updateUser(@Valid @RequestBody UserDTO updateUserRequest) {
        UserAccount existingUser = accRepo.findByEmail(updateUserRequest.getEmail());
        if (existingUser != null) {
            modelMapper.map(updateUserRequest, existingUser);
            accRepo.deleteByEmail(existingUser.getEmail());
            accRepo.save(existingUser);
            return ResponseEntity.ok("User account updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User account not found");
        }
    }

    @GetMapping("/findAccountByEmail")
    public ResponseEntity<Object> findAccount(@RequestParam String email) {
        List<UserAccount> accounts = findAccRepo.findByEmail(email);
        if (email.equals(accounts.get(0).getEmail())) {
            return new ResponseEntity<>(accounts.get(0), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Collections.singletonMap("Message", "Account not found"), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/loginAccount")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginDetail loginDetail) {

        String email = loginDetail.getEmail();
        String password = loginDetail.getPassword();

        Map<String, String> response = new HashMap<>();

        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            response.put("Message", "Either of the parameters is null");
            return ResponseEntity.badRequest().body(response);
        }

        try{
            UserAccount user = findAccRepo.findByEmail(email).get(0);
            if (user != null) {
                if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                    response.put("Message", "Log in successfully");
                    return ResponseEntity.ok(response);
                } else {
                    response.put("Message", "Invalid Credentials");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }
            }else{
                response.put("Message", "Incorrect Email or Password");
                return ResponseEntity.badRequest().body(response);
            }
        }catch(Exception e){
            response.put("Message", "User does not exist");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
