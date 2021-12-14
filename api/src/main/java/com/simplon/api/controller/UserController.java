package com.simplon.api.controller;


import com.simplon.api.exception.BadRequestException;
import com.simplon.api.exception.ResourceNotFoundException;
import com.simplon.api.exception.TechnicalException;
import com.simplon.api.restEntity.UserDTO;
import com.simplon.api.security.CurrentUser;
import com.simplon.api.security.UserPrincipal;
import com.simplon.api.service.UserService;
import com.simplon.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * The type User controller.
 */
@RestController
@RequestMapping("user")
@PreAuthorize("hasRole('USER')")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Gets user.
     *
     * @param email the email
     * @return the user
     * @throws ResourceNotFoundException the resource not found exception
     */
    @PostMapping("")
    public ResponseEntity<UserDTO> getUser(
            @Valid @RequestBody UserDTO email) throws ResourceNotFoundException {
        return new ResponseEntity<>(userService.getUser(email), HttpStatus.OK);
    }

    /**
     * Create user response entity.
     *
     * @param userParameterDTO the user parameter dto
     * @return the response entity
     * @throws BadRequestException the bad request exception
     * @throws TechnicalException  the technical exception
     */
    @Operation(summary = "Create user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request the fields are not completed"),
            @ApiResponse(responseCode = "404", description = "Alert not found")})
    @PostMapping("/create")
    public ResponseEntity<Boolean> createUser(
            @Valid @RequestBody UserDTO userParameterDTO) throws BadRequestException, TechnicalException {
        return new ResponseEntity<>(userService.saveUser(userParameterDTO), HttpStatus.CREATED);
    }

    /**
     * Delete user response entity.
     *
     * @param email the email
     * @return the response entity
     * @throws TechnicalException the technical exception
     */
    @Operation(summary = "Delete user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User delete with success"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized to access at the request"),
            @ApiResponse(responseCode = "404", description = "User not found")})
    @PostMapping("/delete")
    public ResponseEntity<Boolean> deleteUser(
            @Valid @RequestBody UserDTO email) throws TechnicalException {
        return new ResponseEntity<>(userService.deleteUser(email), HttpStatus.OK);
    }

    /**
     * Update user password response entity.
     *
     * @param userNewLoginDTO the user new login dto
     * @param currentUser     the current user
     * @return the response entity
     * @throws BadRequestException the bad request exception
     * @throws TechnicalException  the technical exception
     */
    @Operation(summary = "Update password user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password update with success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized to access at the request"),
            @ApiResponse(responseCode = "404", description = "User not found")})
    @PostMapping("/updatePassword")
    public ResponseEntity<Boolean> updateUserPassword(
            @Valid @RequestBody UserDTO userNewLoginDTO,
            @CurrentUser UserPrincipal currentUser
    ) throws BadRequestException, TechnicalException {
        return new ResponseEntity<>(userService.updateUser(userNewLoginDTO, currentUser), HttpStatus.OK);
    }

    /**
     * Gets current user.
     *
     * @param userPrincipal the user principal
     * @return the current user
     * @throws ResourceNotFoundException the resource not found exception
     */
    @Operation(summary = "Return yourself information account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alert Found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized to access at the request")})
    @GetMapping("/me")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) throws ResourceNotFoundException {

        return userService.getCurrentUser(userPrincipal.getId());
    }

}
