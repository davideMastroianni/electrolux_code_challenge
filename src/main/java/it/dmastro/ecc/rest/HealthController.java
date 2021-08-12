package it.dmastro.ecc.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;

@RestController
@RequestMapping("/health")
@Slf4j
public class HealthController {

  @Operation(summary = "Health api")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK",
          content = { @Content(mediaType = "application/json",
              schema = @Schema(implementation = String.class)) }),
      @ApiResponse(responseCode = "400", description = "Bad Request",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Problem.class))),
      @ApiResponse(responseCode = "404", description = "Not Found",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Problem.class))),
      @ApiResponse(responseCode = "500", description = "Internal Server Error",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Problem.class))) })
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getHealth() {
    log.info("Ecc is up and running");
    return ResponseEntity.status(HttpStatus.OK).body("OK");
  }
}
