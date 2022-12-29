package com.dm.rest.controllers;

import com.dm.rest.dto.UserDTO;
import com.dm.rest.payload.requests.RegistrationRequest;
import com.dm.rest.payload.response.ApiResponse;
import com.dm.rest.security.CustomUserDetails;
import com.dm.rest.service.UserService;
import com.dm.rest.util.UserConvector;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountControllerRest {

    private final UserService userService;
    private final UserConvector convector;

    @Autowired
    public AccountControllerRest(UserService userService, UserConvector convector) {
        this.userService = userService;
        this.convector = convector;
    }

    @GetMapping("/{username}")
    public UserDTO getAccount(@PathVariable("username") String login){
        return userService.getUserByName(login);
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getAllAccounts(){
        return userService.getAllUsers();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> addAccount(@Valid @RequestBody RegistrationRequest request,
                           @AuthenticationPrincipal CustomUserDetails principal){
        UserDTO dto = new UserDTO(request);
        UserDTO userDto = convector.convertToDto(userService.createUser(dto, principal));
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable(value = "username") String username,
                                                  @AuthenticationPrincipal CustomUserDetails principal) {
        ApiResponse apiResponse = userService.deleteUser(username, principal);

        return new ResponseEntity< >(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{username}/changePassword")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> changePassword(@PathVariable(name = "username") String username,
                                                      @AuthenticationPrincipal CustomUserDetails principal) {
        ApiResponse apiResponse = userService.changePassword(username, principal);

        return new ResponseEntity< >(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{username}/giveAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> giveAdmin(@PathVariable(name = "username") String username) {
        ApiResponse apiResponse = userService.giveAdmin(username);

        return new ResponseEntity< >(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{username}/takeAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> takeAdmin(@PathVariable(name = "username") String username) {
        ApiResponse apiResponse = userService.takeAdmin(username);

        return new ResponseEntity< >(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal CustomUserDetails currentUser){
        UserDTO dto = userService.getCurrentUser(currentUser);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
