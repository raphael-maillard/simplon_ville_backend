package com.simplon.api.Service;


import com.simplon.api.Mapper.UserDTOMapper;
import com.simplon.entity.User;
import org.apache.commons.lang3.StringUtils;
import com.simplon.api.Repository.UserRepository;
import com.simplon.api.RestEntity.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.simplon.api.Utils.Constants;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO getUser(UserDTO email) throws Exception {

       checkEmailValidity(email.getEmail());

         UserDTO userDTO = this.getUserByEmail(email.getEmail());

        if(Objects.isNull(userDTO)){
            throw new Exception("No user found with this email address");
        }

        return userDTO;

    }


    public Boolean saveUser(UserDTO userDTO)throws Exception{

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        checkEmailValidity(userDTO.getEmail());

        if(!Objects.isNull(this.getUserByEmail(userDTO.getEmail()))){
            throw new Exception("This email is already used by an account");
        }

        try{
            User user = new User();
            user.setUserName(userDTO.getUserName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            userRepository.save(user);
            return true;
        }catch(Exception e){
            throw e;
        }
    }

    @Transactional
    public Boolean deleteUser(UserDTO email)throws Exception{

        checkEmailValidity(email.getEmail());

        try{
            userRepository.deleteUser(email.getEmail());
            return true;
        }catch(Exception e){
            throw e;
        }
    }

    public Boolean updateUser(UserDTO userNewLoginDTO) throws Exception {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        checkEmailValidity(userNewLoginDTO.getEmail());

        User user = userRepository.findByEmail(userNewLoginDTO.getEmail());

        if(Objects.isNull(user)){
            throw new Exception("No user found with this email address");
        }
        if(!passwordEncoder.matches(userNewLoginDTO.getPassword(),user.getPassword())){
            throw new Exception("Passwords did not matched");
        }

        try{
            user.setPassword(passwordEncoder.encode(userNewLoginDTO.getNewPassword()));
            userRepository.save(user);
            return true;
        }catch (Exception e){
            throw e;
        }

    }

    private boolean checkEmailValidity(String email) throws Exception {

        if(StringUtils.isEmpty(email) || !Pattern.matches(Constants.mailRegex,email)){
            throw new Exception("Email can't be empty and must be a valid mail address");
        }
        return true;
    }

    private UserDTO getUserByEmail(String email){
        User user = userRepository.findByEmail(email);
        return UserDTOMapper.map(user);
    }
}
