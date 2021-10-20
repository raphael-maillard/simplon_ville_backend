package com.simplon.api.Controller;

import com.simplon.api.RestEntity.AlertDTO;
import com.simplon.api.Service.AlertService;
import com.simplon.api.exception.ResourceNotFoundException;
import com.simplon.api.exception.TechnicalException;
import com.simplon.entity.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("alert")
//@PreAuthorize("hasRole('ROLE_ADMIN)")
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
    public String saveAlert(@Valid @RequestBody AlertDTO alertDTO) throws TechnicalException {

        String result = alertService.save(alertDTO);

        return result;
    }


    @PostMapping("/fix")
    public ResponseEntity<?> fixAlert(@CurrentSecurityContext Principal principal, @RequestBody AlertDTO alertDTO) throws TechnicalException {

        ResponseEntity<?> result = alertService.alertFix(principal, alertDTO);

        return ResponseEntity.ok(result);
    }
}
