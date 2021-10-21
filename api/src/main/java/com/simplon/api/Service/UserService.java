package com.simplon.api.Service;


import com.simplon.api.Mapper.UserDTOMapper;
import com.simplon.api.Repository.UserRepository;
import com.simplon.api.RestEntity.UserDTO;
import com.simplon.api.Security.AuthDTOs.ApiResponse;
import com.simplon.api.Security.AuthDTOs.AuthResponseDTO;
import com.simplon.api.Security.AuthDTOs.SignUpRequest;
import com.simplon.api.Security.RestAuthenticationEntryPoint;
import com.simplon.api.Security.UserPrincipal;
import com.simplon.api.Utils.Constants;
import com.simplon.api.exception.BadRequestException;
import com.simplon.api.exception.ResourceNotFoundException;
import com.simplon.api.exception.TechnicalException;
import com.simplon.entity.AuthProvider;
import com.simplon.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.net.URI;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;


    public UserDTO getUser(UserDTO email) throws ResourceNotFoundException {

       checkEmailValidity(email.getEmail());

         UserDTO userDTO = this.getUserByEmail(email.getEmail());

        if(Objects.isNull(userDTO)){
            throw new ResourceNotFoundException("No user found with this email address");
        }

        return userDTO;

    }


    public Boolean saveUser(UserDTO userDTO)throws BadRequestException,TechnicalException{

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        checkEmailValidity(userDTO.getEmail());

        if(!Objects.isNull(this.getUserByEmail(userDTO.getEmail()))){
            throw new BadRequestException("This email is already used by an account");
        }

        try{
            User user = new User();
            user.setUserName(userDTO.getUserName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setProvider(AuthProvider.local);
            userRepository.save(user);
            return true;
        }catch(Exception e){
            throw new TechnicalException("Something went wrong during user creation : " + e.getMessage());
        }
    }

    @Transactional
    public Boolean deleteUser(UserDTO email)throws TechnicalException{

        checkEmailValidity(email.getEmail());

        try{
            userRepository.deleteUser(email.getEmail());
            return true;
        }catch(Exception e){
            throw new TechnicalException("Something went wrong during user deletion : " +e.getMessage());
        }
    }

    public Boolean updateUser(UserDTO userNewLoginDTO, UserPrincipal currentUser) throws BadRequestException, TechnicalException {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if(!passwordEncoder.matches(userNewLoginDTO.getPassword(),currentUser.getPassword())){
            throw new BadRequestException("Passwords did not matched");
        }

        try{
            userRepository.updateCurrentUserPassword(currentUser.getEmail(),passwordEncoder.encode(userNewLoginDTO.getNewPassword()));
            return true;
        }catch (Exception e){
            throw new TechnicalException("Something went wrong during password update : " + e.getMessage());
        }

    }

    public User getCurrentUser(String id) throws ResourceNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with id :" + id));
    }

    @PostConstruct
    public void resetDefaultUser(){
        logger.info("[ResetDefaultUser] Searching for default user");

        Optional<User> defaultUserDb = userRepository.findByEmail(Constants.defaultMail);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!defaultUserDb.isPresent()){
            logger.info("[ResetDefaultUser] No default user found, creating a new one");
            User defaultUser = new User();
            defaultUser.setProvider(AuthProvider.local);
            defaultUser.setUserName("ADMIN");
            defaultUser.setEmail(Constants.defaultMail);
            defaultUser.setPassword(passwordEncoder.encode("ADMIN"));
            userRepository.save(defaultUser);
            logger.info("[ResetDefaultUser] Default user created");
        }else{
            logger.info("[ResetDefaultUser] Default user found");
        }
    }

    private boolean checkEmailValidity(String email) throws BadRequestException {

        if(StringUtils.isEmpty(email) || !Pattern.matches(Constants.mailRegex,email)){
            throw new BadRequestException("Email can't be empty and must be a valid mail address");
        }
        return true;
    }

    private UserDTO getUserByEmail(String email){
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(UserDTOMapper::map).orElse(null);
    }


}
