package com.simplon.api.Controller;

import com.simplon.api.RestEntity.AlertDTO;
import com.simplon.api.Security.CurrentUser;
import com.simplon.api.Security.UserPrincipal;
import com.simplon.api.Service.AlertService;
import com.simplon.api.exception.ResourceNotFoundException;
import com.simplon.api.exception.TechnicalException;
import com.simplon.entity.Alert;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("alert")
@SecurityRequirement(name = "simplon_ville")
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

        Optional<Alert> result = alertService.findById(id);

        return ResponseEntity.ok(result.get());
    }

    @PostMapping()
    public String saveAlert(@Valid @RequestBody AlertDTO alertDTO) throws TechnicalException {

        String result = alertService.save(alertDTO);

        return result;
    }


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/fix")
    public ResponseEntity<?> fixAlert(@CurrentUser UserPrincipal userPrincipal, @RequestBody AlertDTO alertDTO) throws TechnicalException {

        var result = alertService.alertFix(userPrincipal, alertDTO);

        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/delete")
    public ResponseEntity<?> deleteAlert(@Valid @RequestBody AlertDTO alertDTO) throws TechnicalException {

        alertService.deleteAlert(alertDTO);

        return ResponseEntity.ok("Alert delete");
    }
}
