package pp.dev.costsapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pp.dev.costsapp.model.dto.LoginRequest;
import pp.dev.costsapp.model.dto.LoginResponse;
import pp.dev.costsapp.model.dto.SignupRequest;
import pp.dev.costsapp.service.UserService;
import pp.dev.costsapp.tools.JwtHelper;

@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest requestDto) {
        userService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        String token = JwtHelper.generateToken(request.email());
        return ResponseEntity.ok(new LoginResponse(request.email(), token));
    }

}
