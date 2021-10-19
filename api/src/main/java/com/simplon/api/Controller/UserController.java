package com.simplon.api.Controller;


import com.simplon.api.RestEntity.UserDTO;
import com.simplon.api.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("user")

public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<UserDTO> getUser(
           @Valid @RequestBody UserDTO email)throws Exception{
        return new ResponseEntity<>(userService.getUser(email),HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Boolean> createUser(
          @Valid @RequestBody UserDTO userParameterDTO) throws Exception{
        return new ResponseEntity<>(userService.saveUser(userParameterDTO), HttpStatus.CREATED);
    }

    @PostMapping("/delete")
    public ResponseEntity<Boolean> deleteUser(
           @Valid @RequestBody UserDTO email)throws Exception{
        return new ResponseEntity<>(userService.deleteUser(email),HttpStatus.OK);
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<Boolean> updateUserPassword(
            @Valid @RequestBody UserDTO userNewLoginDTO
            ) throws Exception{
        return new ResponseEntity<>(userService.updateUser(userNewLoginDTO),HttpStatus.OK);
    }

}
