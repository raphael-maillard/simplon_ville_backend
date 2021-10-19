package com.simplon.api.Service;

import com.simplon.api.Repository.AlertRepository;
import com.simplon.api.RestEntity.AlertDTO;
import com.simplon.api.exception.BadRequestException;
import com.simplon.api.exception.ResourceNotFoundException;
import com.simplon.api.exception.TechnicalException;
import com.simplon.entity.Alert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
//@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    public List<Alert> findAll() throws ResourceNotFoundException {

        List<Alert> result = alertRepository.findAll();

        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Not found alert register");
        }

        return result;
    }

    public Optional<Alert> findById(String id) throws ResourceNotFoundException {

        if (StringUtils.isEmpty(id)) {
            throw new BadRequestException("Id can't be empty");
        }

        Optional<Alert> result = alertRepository.findById(id);

        if (Objects.isNull(result)) {
            throw new ResourceNotFoundException(" Alert not found");
        }

        return result;
    }

    public String save(AlertDTO alertDTO) throws TechnicalException {

        if (Objects.isNull(alertDTO)) {
            throw new BadRequestException("Your alert cant be empty");
        }

        Alert alert = new Alert();
        alert.setCause(alertDTO.getCause());
        alert.setDescription(alertDTO.getDescription());
        alert.setCity(alertDTO.getCity());
        alert.setDate(alertDTO.getDate());
        alert.setTime(alertDTO.getTime());
        alert.setLocation(alertDTO.getLocation());
        alert.setPicture(alertDTO.getPicture());
        alert.setFirstname(alertDTO.getFirstname());
        alert.setUserAddress(alertDTO.getUserAddress());
        alert.setUserZipcode(alertDTO.getUserZipcode());
        alert.setName(alertDTO.getName());
        alert.setPhoneNumber(alertDTO.getPhoneNumber());

        Alert result = alertRepository.save(alert);

        if (Objects.isNull(result)) {
            throw new TechnicalException("Something is wrong, record impossible");
        }

        return (" Your incident is send ");
    }

    public ResponseEntity<?> alertFix(Principal principal, AlertDTO alertDTO) throws TechnicalException {
        if (Objects.isNull(alertDTO)) {
            throw new BadRequestException("Alert not load");
        }

        Boolean result = alertRepository.fixIt(principal.getName(), LocalDateTime.now(), alertDTO.getId());

        if (!result) {
            throw new TechnicalException("Somethings was wrong");
        }

        return ResponseEntity.ok(result);

    }


}
