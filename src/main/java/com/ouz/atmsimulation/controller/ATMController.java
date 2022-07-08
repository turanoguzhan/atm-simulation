package com.ouz.atmsimulation.controller;

import com.ouz.atmsimulation.controller.dto.ATMOperationDTO;
import com.ouz.atmsimulation.controller.dto.ATMSuccessMessageDTO;
import com.ouz.atmsimulation.enums.TransactionType;
import com.ouz.atmsimulation.exception.ExceptionMessage;
import com.ouz.atmsimulation.service.ATMServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/atm")
public class ATMController {

    private final ATMServiceImpl atmService;

    public ATMController(ATMServiceImpl atmService) {
        this.atmService = atmService;
    }

    @GetMapping("/expose")
    @PreAuthorize(value="${ADMIN}")
    @Operation(description = "it returns account current information if account valid and pin is correct.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Accounr",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ATMSuccessMessageDTO.class)) }),
            @ApiResponse(responseCode = "500",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessage.class)) }),
            @ApiResponse(responseCode = "403",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessage.class)) }),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessage.class)) }) })
    public ResponseEntity accountInfo(@Parameter(name="ATM Operation DTO",
            required = true,
            schema = @Schema(allOf = ATMOperationDTO.class),
            content = @Content(mediaType = "application/json")
    ) @RequestBody ATMOperationDTO dto){
        return atmService.exposeAccount(dto.getAccountNumber(),dto.getPin());
    }

    @PutMapping( "/withdraw")
    @PreAuthorize(value="${ADMIN}")
    @Operation(description = "Withdraw operation. You can get money from your authorized account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ATMSuccessMessageDTO.class)) }),
            @ApiResponse(responseCode = "204",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionMessage.class)) }),
            @ApiResponse(responseCode = "400",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessage.class)) }),
            @ApiResponse(responseCode = "403",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessage.class)) }),
            @ApiResponse(responseCode = "404",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessage.class)) }),
            @ApiResponse(responseCode = "416",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessage.class)) })})
    public ResponseEntity withdraw(@Parameter(name="ATM Operation DTO",
            required = true,
            schema = @Schema(allOf = ATMOperationDTO.class),
            content = @Content(mediaType = "application/json")
    ) @RequestBody ATMOperationDTO dto){
        return atmService.atmOperation(dto.getAccountNumber(),dto.getPin(),dto.getAmount(),TransactionType.WITHDRAW);
    }

    @PutMapping( "/deposit")
    @PreAuthorize(value="${ADMIN}")
    @Operation(description = "Deposit operation. You can add money to your authorized account. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ATMSuccessMessageDTO.class)) }),
            @ApiResponse(responseCode = "204",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessage.class)) }),
            @ApiResponse(responseCode = "400",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessage.class)) }),
            @ApiResponse(responseCode = "403",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessage.class)) }),
            @ApiResponse(responseCode = "404",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessage.class)) }),
            @ApiResponse(responseCode = "416",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionMessage.class)) })})
    public ResponseEntity deposit(@Parameter(name="ATM Operation DTO",
            required = true,
            schema = @Schema(allOf = ATMOperationDTO.class),
            content = @Content(mediaType = "application/json")
    )@RequestBody ATMOperationDTO dto){
        return atmService.atmOperation(dto.getAccountNumber(),dto.getPin(),dto.getAmount(), TransactionType.DEPOSIT);
    }

    @GetMapping("/sayHi")
    @PreAuthorize(value="${ADMIN}")
    public ResponseEntity sayHello(){
        return ResponseEntity.ok("Hi !");
    }
}
