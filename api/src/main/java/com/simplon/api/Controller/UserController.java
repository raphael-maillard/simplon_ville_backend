package com.simplon.api.Controller;


import com.simplon.api.RestEntity.UserDTO;
import com.simplon.api.Security.CurrentUser;
import com.simplon.api.Security.UserPrincipal;
import com.simplon.api.Service.UserService;
import com.simplon.api.exception.BadRequestException;
import com.simplon.api.exception.ResourceNotFoundException;
import com.simplon.api.exception.TechnicalException;
import com.simplon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("user")
@PreAuthorize("hasRole('USER')")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<UserDTO> getUser(
           @Valid @RequestBody UserDTO email) throws ResourceNotFoundException {
        return new ResponseEntity<>(userService.getUser(email),HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Boolean> createUser(
          @Valid @RequestBody UserDTO userParameterDTO) throws BadRequestException, TechnicalException {
        return new ResponseEntity<>(userService.saveUser(userParameterDTO), HttpStatus.CREATED);
    }

    @PostMapping("/delete")
    public ResponseEntity<Boolean> deleteUser(
           @Valid @RequestBody UserDTO email)throws TechnicalException{
        return new ResponseEntity<>(userService.deleteUser(email),HttpStatus.OK);
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<Boolean> updateUserPassword(
            @Valid @RequestBody UserDTO userNewLoginDTO,
            @CurrentUser  UserPrincipal currentUser
            ) throws BadRequestException,TechnicalException{
        return new ResponseEntity<>(userService.updateUser(userNewLoginDTO,currentUser),HttpStatus.OK);
    }

    @GetMapping("/me")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) throws ResourceNotFoundException {
        return userService.getCurrentUser(userPrincipal.getId());
    }

}
