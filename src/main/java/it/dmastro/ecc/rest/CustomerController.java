package it.dmastro.ecc.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.dmastro.ecc.dataobject.customer.CustomerDTO;
import it.dmastro.ecc.service.ICustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;

@RestController
@RequestMapping("/api/v1/customers")
@Slf4j
public class CustomerController {

  private final ICustomerService customerService;

  public CustomerController(ICustomerService customerService) {
    this.customerService = customerService;
  }

  @Operation(summary = "Get customer by customer id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK",
          content = { @Content(mediaType = "application/json",
              schema = @Schema(implementation = CustomerDTO.class)) }),
      @ApiResponse(responseCode = "422", description = "Bad Request",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Problem.class))),
      @ApiResponse(responseCode = "404", description = "Not Found",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Problem.class))),
      @ApiResponse(responseCode = "500", description = "Internal Server Error",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Problem.class))) })
  @GetMapping(value = "/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CustomerDTO> getCustomer(@PathVariable String customerId) {
    CustomerDTO customer = customerService.getCustomer(customerId);
    return ResponseEntity.status(HttpStatus.OK).body(customer);
  }

  @Operation(summary = "save new customer")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK",
          content = { @Content(mediaType = "application/json",
              schema = @Schema(implementation = CustomerDTO.class)) }),
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
  public ResponseEntity<CustomerDTO> saveCustomer(@RequestBody CustomerDTO customerDTO) {
    CustomerDTO customer = customerService.saveCustomer(customerDTO);
    return ResponseEntity.status(HttpStatus.OK).body(customer);
  }

}
