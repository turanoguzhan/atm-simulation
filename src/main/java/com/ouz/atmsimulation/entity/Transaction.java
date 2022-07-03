package com.ouz.atmsimulation.entity;

import com.ouz.atmsimulation.enums.SourceType;
import com.ouz.atmsimulation.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name="TRANSACTION")
public class Transaction extends BaseEntity{

    @Column(name="DATE_TIME")
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    @Column(name="TYPE")
    private TransactionType type;

    @Column(name="AMOUNT")
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ID")
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(name="SOURCE_TYPE")
    private SourceType sourceType;

    @Override
    public String toString() {
        return "Transaction{" +
                " account = "+ account.getAccountNumber() +
                ", date-time =" + dateTime +
                ", type ='" + type.value +
                ", amount =" + amount +
                ", source =" + sourceType.value +
                '}';
    }
}
