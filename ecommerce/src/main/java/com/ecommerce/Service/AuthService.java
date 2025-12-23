package com.ecommerce.Service;

import com.ecommerce.ApiResponse.JwtResponse;
import com.ecommerce.Dto.LoginRequestDto;
import com.ecommerce.Entity.User;
import com.ecommerce.Repository.UserRepository;
import com.ecommerce.Role;
import com.ecommerce.Util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String register(User user){

        log.info("Attempting to register new user with username: {} and email: {}",
                user.getUserName(), user.getEmail());
        Optional<User> userNameExist = userRepository.findByUserName(user.getUserName());

        if(userNameExist.isPresent()){
            log.info("UserName Already Exist, register Request Fail");
            return "UserName Already Existed please Enter different userName";
        }

        Optional<User> emailExist = userRepository.findByEmail(user.getEmail());

        if(emailExist.isPresent()){
            log.info("Email Already existed, Email: {} ",user.getEmail());
            return "Email Already existed please Login with same email";
        }


        try {
            // Save the user
            user.setRole(Role.USER);
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            User savedUser = userRepository.save(user);

            log.info("User registered successfully - Username: {}, Email: {}",
                    savedUser.getUserName(), savedUser.getEmail());

            log.debug("Full user details saved: {}", savedUser);
            return "User registered successfully";

        } catch (Exception e) {
            log.error("Registration failed for user: {}. Error: {}",
                    user.getUserName(), e.getMessage(), e);
            return "Registration failed due to an error. Please try again.";
        }
    }

    public JwtResponse login(LoginRequestDto loginRequest){
        log.info("Login service request for username: {}", loginRequest.getUserName());

        User user = userRepository.findByUserName(loginRequest.getUserName())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if(!passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())){
            log.warn("Invalid login attempt for username: {}", loginRequest.getUserName());
            throw new RuntimeException("Invalid Password or UserName");
        }

        String token = jwtUtil.generateToken(user.getUserName(),user.getRole());


        Date issuedAt = new Date();
        Date expiry = jwtUtil.getExpiryDate();

        log.info("Login Service Request successful for username: {}", user.getUserName());

        return JwtResponse.builder()
                .token(token)
                .userName(user.getUserName())
                .role(user.getRole().name())
                .issuedAt(issuedAt)
                .expiry(expiry)
                .build();
    }

}
