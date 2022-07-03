package com.ouz.atmsimulation.controller.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ATMOperationDTO{

    @Schema(example = "123456789", required = true)
    private Long accountNumber;

    @Schema(example = "1234", required = true)
    private Long pin;

    @Schema(example = "870", required = true)
    private BigDecimal amount;
}
