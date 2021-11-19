package com.simplon.api.Service;

import com.simplon.api.Mapper.AlertDTOMapper;
import com.simplon.api.Repository.AlertRepository;
import com.simplon.api.RestEntity.AlertDTO;
import com.simplon.api.Security.UserPrincipal;
import com.simplon.api.exception.BadRequestException;
import com.simplon.api.exception.ResourceNotFoundException;
import com.simplon.api.exception.TechnicalException;
import com.simplon.entity.Alert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@PreAuthorize("hasRole('USER')")
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    public List<AlertDTO> findAll() throws ResourceNotFoundException {

        List<Alert> result = alertRepository.findAll();

        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Not found alert register");
        }

        return result.stream().map(AlertDTOMapper::map).collect(Collectors.toList());
    }

    public AlertDTO findById(String id) throws ResourceNotFoundException {

        if (StringUtils.isEmpty(id)) {
            throw new BadRequestException("Id can't be empty");
        }

        Optional<Alert> result = alertRepository.findById(id);

        if (Objects.isNull(result) || result.isEmpty()) {
            throw new ResourceNotFoundException(" Alert not found");
        }

        return AlertDTOMapper.map(result.get());
    }

    @PreAuthorize("hasRole('ANONYMOUS')")
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

    public ResponseEntity<Integer> alertFix(UserPrincipal userPrincipal, AlertDTO alertDTO) throws TechnicalException {

        if (Objects.isNull(alertDTO)) {
            throw new BadRequestException("Alert not load");
        }

        boolean status = !alertRepository.findById(alertDTO.getId()).get().isFix();
        Integer result = alertRepository.fixIt(userPrincipal.getUsername(), LocalDateTime.now(), alertDTO.getId(), status);

        if (result == 0) {
            throw new TechnicalException("Somethings was wrong");
        }

        return ResponseEntity.ok(result);

    }

    public void deleteAlert(AlertDTO alertDTO) throws TechnicalException {
        if (Objects.isNull(alertDTO)) {
            throw new BadRequestException("Alert not load");
        }
        alertRepository.deleteById(alertDTO.getId());
    }

}
