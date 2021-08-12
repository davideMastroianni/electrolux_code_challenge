package it.dmastro.ecc.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.dmastro.ecc.dataobject.appliance.ApplianceDTO;
import it.dmastro.ecc.service.IApplianceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;

@RestController
@RequestMapping("/api/v1/appliances")
@Slf4j
public class ApplianceController {

  static final String APPLIANCE_CONNECTED = "Appliance %s is connected";

  private final IApplianceService applianceService;

  public ApplianceController(IApplianceService applianceService) {
    this.applianceService = applianceService;
  }

  @Operation(summary = "Set appliance as connected")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK",
          content = { @Content(mediaType = "application/json",
              schema = @Schema(implementation = ApplianceDTO.class)) }),
      @ApiResponse(responseCode = "422", description = "Bad Request",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Problem.class))),
      @ApiResponse(responseCode = "404", description = "Not Found",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Problem.class))),
      @ApiResponse(responseCode = "500", description = "Internal Server Error",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Problem.class))) })
  @PostMapping(value = "/{applianceId}/connections", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApplianceDTO> setConnected(@PathVariable String applianceId) {
    ApplianceDTO appliance = applianceService.getAppliance(applianceId);
    applianceService.updateApplianceConnectionTime(appliance);
    return ResponseEntity.status(HttpStatus.OK).body(appliance);
  }

  @Operation(summary = "Save appliance")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK",
          content = { @Content(mediaType = "application/json",
              schema = @Schema(implementation = ApplianceDTO.class)) }),
      @ApiResponse(responseCode = "422", description = "Bad Request",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Problem.class))),
      @ApiResponse(responseCode = "404", description = "Not Found",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Problem.class))),
      @ApiResponse(responseCode = "500", description = "Internal Server Error",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Problem.class))) })
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApplianceDTO> saveAppliance(@RequestBody ApplianceDTO applianceDTO) {
    ApplianceDTO appliance = applianceService.saveAppliance(applianceDTO);
    applianceService.updateApplianceConnectionTime(appliance);
    return ResponseEntity.status(HttpStatus.OK).body(appliance);
  }

}
