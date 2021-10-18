package com.simplon.api.Controller;

import com.simplon.api.RestEntity.AlertDTO;
import com.simplon.api.Service.AlertService;
import com.simplon.api.exception.ResourceNotFoundException;
import com.simplon.api.exception.TechnicalException;
import com.simplon.entity.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("alert")
@PreAuthorize("hasRole('ROLE_ADMIN)")
public class AlertController {

    @Autowired
    private AlertService alertService;

    @GetMapping("")
    public List<Alert> getAlerts() throws ResourceNotFoundException {

        List<Alert> listAlert = alertService.findAll();

        return listAlert;

    }

    @GetMapping("/{id}")
    public ResponseEntity<Alert> getAlertById(@PathVariable String id) throws ResourceNotFoundException {

        Alert result = alertService.findById(id).get();

        return ResponseEntity.ok(result);
    }

    @PostMapping()
    public String saveAlert(@RequestBody AlertDTO alertDTO) throws TechnicalException {

        String result = alertService.save(alertDTO);

        return result;
    }

}
