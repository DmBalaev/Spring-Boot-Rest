package com.dm.rest.controllers;

import com.dm.rest.payload.requests.RegistrationRequest;
import com.dm.rest.payload.requests.UpdateInfoRequest;
import com.dm.rest.payload.response.ApplicationResponse;
import com.dm.rest.payload.response.UserInfo;
import com.dm.rest.security.CustomUserDetails;
import com.dm.rest.service.UserService;
import com.dm.rest.util.UserConvector;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountControllerRest {
    private final UserService userService;
    private final UserConvector convector;

    @Operation(
            tags = "Account operations",
            summary = "Return all user (Only ADMIN privileges)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Get all users"),
                    @ApiResponse(responseCode = "403", description = "Has no authority", content = @Content)
            },
            security = @SecurityRequirement(name = "BearerJWT")
    )
    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserInfo> getAllAccounts(){
        return convector.convertAllToDto(userService.getAllUsers());
    }

    @Operation(
            tags = "Account operations",
            summary = "Create account (Only ADMIN privileges)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful created account"),
                    @ApiResponse(responseCode = "403", description = "Has no authority", content = @Content)
            },
            security = @SecurityRequirement(name = "BearerJWT")
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserInfo> addAccount(@Valid @RequestBody RegistrationRequest request){
        return ResponseEntity.ok(convector.convertToDto(userService.createUser(request)));
    }


    @Operation(
            tags = "Account operations",
            summary = "Delete account (Only ADMIN privileges)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful deleted account"),
                    @ApiResponse(responseCode = "403", description = "Has no authority", content = @Content)
            },
            security = @SecurityRequirement(name = "BearerJWT")
    )
    @DeleteMapping("/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApplicationResponse> deleteUser(@PathVariable(value = "username") String username) {
        ApplicationResponse applicationResponse = userService.deleteUser(username);

        return new ResponseEntity< >(applicationResponse, HttpStatus.OK);
    }


    @Operation(
            tags = "Account operations",
            summary = "Update user info",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful user info"),
                    @ApiResponse(responseCode = "403", description = "Has no authority", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
            },
            security = @SecurityRequirement(name = "BearerJWT")
    )
    @PutMapping("/{email}")
    @PreAuthorize("hasRole('ADMIN') or #email == authentication.principal.username")
    public ResponseEntity<UserInfo> updateUser(@RequestBody UpdateInfoRequest update,
                                               @PathVariable String username) {
        UserInfo info = convector.convertToDto(userService.updateUser(update, username));

        return new ResponseEntity< >(info, HttpStatus.OK);
    }



    @Operation(
            tags = "Account operations",
            summary = "Give administrator rights (Only ADMIN privileges)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully granted administrator rights"),
                    @ApiResponse(responseCode = "403", description = "Has no authority", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
            },
            security = @SecurityRequirement(name = "BearerJWT")
    )
    @PutMapping("/{username}/giveAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApplicationResponse> giveAdmin(@PathVariable(name = "username") String username) {
        ApplicationResponse applicationResponse = userService.giveAdmin(username);

        return new ResponseEntity< >(applicationResponse, HttpStatus.OK);
    }


    @Operation(
            tags = "Account operations",
            summary = "Take administrator rights (Only ADMIN privileges)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Admin rights removed successfully"),
                    @ApiResponse(responseCode = "403", description = "Has no authority", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
            },
            security = @SecurityRequirement(name = "BearerJWT")
    )
    @PutMapping("/{username}/takeAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApplicationResponse> takeAdmin(@PathVariable(name = "username") String username) {
        ApplicationResponse applicationResponse = userService.takeAdmin(username);

        return new ResponseEntity< >(applicationResponse, HttpStatus.OK);
    }


    @Operation(
            tags = "Account operations",
            summary = "Information about the current user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully getting information about the current user")
            },
            security = @SecurityRequirement(name = "BearerJWT")
    )
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserInfo> getCurrentUser(@AuthenticationPrincipal CustomUserDetails currentUser){
        UserInfo userInfo = userService.getCurrentUser(currentUser);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }
}
