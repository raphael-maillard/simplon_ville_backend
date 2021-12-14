package com.simplon.api.controller;

import com.simplon.api.exception.ResourceNotFoundException;
import com.simplon.api.exception.TechnicalException;
import com.simplon.api.restEntity.AlertDTO;
import com.simplon.api.security.CurrentUser;
import com.simplon.api.security.UserPrincipal;
import com.simplon.api.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * The type Alert controller.
 */
@RestController
@RequestMapping("alert")
@SecurityRequirement(name = "simplon_ville")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@PreAuthorize("hasRole('USER')")
public class AlertController {

    @Autowired
    private AlertService alertService;

    /**
     * Gets alerts.
     *
     * @return the alerts
     * @throws ResourceNotFoundException the resource not found exception
     */
    @Operation(summary = "Find all Alerts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alert Found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AlertDTO.class))}),
            @ApiResponse(responseCode = "204", description = "Alerts no found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized to access at the request")})
    @GetMapping("")
    public List<AlertDTO> getAlerts() throws ResourceNotFoundException {

        return alertService.findAll();

    }

    /**
     * Gets alert by id.
     *
     * @param id the id
     * @return the alert by id
     * @throws ResourceNotFoundException the resource not found exception
     */
    @Operation(summary = "Find alert by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alert Found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AlertDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id Alert"),
            @ApiResponse(responseCode = "401", description = "Unauthorized to access at the request"),
            @ApiResponse(responseCode = "404", description = "Alert not found")})
    @GetMapping("/{id}")
    public ResponseEntity<AlertDTO> getAlertById(@PathVariable String id) throws ResourceNotFoundException {

        return ResponseEntity.ok(alertService.findById(id));
    }

    /**
     * Save alert string.
     *
     * @param alertDTO the alert dto
     * @return the string
     * @throws TechnicalException the technical exception
     */
    @Operation(summary = "Create alert")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Alert created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlertDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request the fields are not completed"),
            @ApiResponse(responseCode = "404", description = "Alert not found")})
    @PreAuthorize("hasRole('ANONYMOUS')")
    @PostMapping()
    public String saveAlert(@Valid @RequestBody AlertDTO alertDTO) throws TechnicalException {

        String result = alertService.save(alertDTO);

        return result;
    }

    /**
     * Fix alert response entity.
     *
     * @param userPrincipal the user principal
     * @param alertDTO      the alert dto
     * @return the response entity
     * @throws TechnicalException the technical exception
     */
    @Operation(summary = "Change status alert by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alert status with success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlertDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request "),
            @ApiResponse(responseCode = "401", description = "Unauthorized to access at the request"),
            @ApiResponse(responseCode = "404", description = "Alert not found")})
    @PostMapping("/fix")
    public ResponseEntity<?> fixAlert(@CurrentUser UserPrincipal userPrincipal, @RequestBody AlertDTO alertDTO) throws TechnicalException {

        ResponseEntity<Integer> result = alertService.alertFix(userPrincipal, alertDTO);

        return ResponseEntity.ok(result);
    }

    /**
     * Delete alert response entity.
     *
     * @param alertDTO the alert dto
     * @return the response entity
     * @throws TechnicalException the technical exception
     */
    @Operation(summary = "Delete alert by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alert delete with success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlertDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized to access at the request"),
            @ApiResponse(responseCode = "404", description = "Alert not found")})
    @PostMapping("/delete")
    public ResponseEntity<?> deleteAlert(@Valid @RequestBody AlertDTO alertDTO) throws TechnicalException {

        alertService.deleteAlert(alertDTO);

        return ResponseEntity.ok("Alert delete");
    }
}
