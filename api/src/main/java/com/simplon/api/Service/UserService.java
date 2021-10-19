package com.simplon.api.Service;


import com.simplon.api.Mapper.UserDTOMapper;
import com.simplon.entity.User;
import org.apache.commons.lang3.StringUtils;
import com.simplon.api.Repository.UserRepository;
import com.simplon.api.RestEntity.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.simplon.api.Utils.Constants;

import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO getUser(String email) throws Exception {

        if(StringUtils.isEmpty(email) || !Pattern.matches(Constants.mailRegex,email)){
            throw new Exception("Email can't be empty and must be a valid mail address");
        }

         UserDTO userDTO = this.getUserByEmail(email);

        if(Objects.isNull(userDTO)){
            throw new Exception("No user found with this email address");
        }

        return userDTO;

    }

    public void saveUser(UserDTO userDTO)throws Exception{

        if(Objects.isNull(userDTO.getEmail()) || !Pattern.matches(Constants.mailRegex,userDTO.getEmail())){
            throw new Exception("Email can't be empty and must be a valid mail address");
        }

        if(!Objects.isNull(this.getUserByEmail(userDTO.getEmail()))){
            throw new Exception("This email is already used by an account");
        }

        try{
            User user = new User();
            user.setUserName(userDTO.getUserName());
            user.setEmail(userDTO.getEmail());
        }catch(Exception e){
            throw e;
        }
    }

    private UserDTO getUserByEmail(String email){
        User user = userRepository.findByEmail(email);
        return UserDTOMapper.map(user);
    }
}
