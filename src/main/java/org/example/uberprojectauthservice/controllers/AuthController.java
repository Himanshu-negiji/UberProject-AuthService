package org.example.uberprojectauthservice.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.uberprojectauthservice.dto.AuthRequestDto;
import org.example.uberprojectauthservice.dto.PassengerDto;
import org.example.uberprojectauthservice.dto.PassengerSignupRequestDto;
import org.example.uberprojectauthservice.services.AuthService;
import org.example.uberprojectauthservice.services.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Value("${cookie.expiry}")
    private int cookieExpiry;

    private final AuthService authService;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup/passenger")
    public ResponseEntity<PassengerDto> signup(@RequestBody PassengerSignupRequestDto passengerSignupRequestDto) {
        PassengerDto response = authService.signupPassenger(passengerSignupRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/signin/passenger")
    public ResponseEntity<?> signIn(@RequestBody AuthRequestDto authRequestDto, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getPassword(), authRequestDto.getEmail()));
        if (authentication.isAuthenticated()) {
            String jwtToken = jwtService.createToken(authRequestDto.getEmail());
            ResponseCookie responseCookie = ResponseCookie.from("jwtToken", jwtToken)
                    .httpOnly(true)
                    .secure(false)
                    .maxAge(cookieExpiry)
                    .build();
            response.setHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
            return new ResponseEntity<>("Successfull Auth", HttpStatus.OK);
        }
        return new ResponseEntity<>("Auth not Successfull", HttpStatus.OK);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validate(HttpServletRequest httpServletRequest) {
        for(Cookie cookie : httpServletRequest.getCookies()) {
            System.out.println("Name : " + cookie.getName() + "Value : " + cookie.getValue());
        }
        return new ResponseEntity<>("Successfull", HttpStatus.OK);
    }
}
