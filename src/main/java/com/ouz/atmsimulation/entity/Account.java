package com.ouz.atmsimulation.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "ACCOUNT")
public class Account extends BaseEntity{

    @Column(name="ACCOUNT_NUMBER")
    private Long accountNumber;

    @Column(name="PIN")
    private Long pin;

    @Column(name="BALANCE")
    private BigDecimal balance;

    @Column(name="OVERDRAFT")
    private BigDecimal overdraft;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BANK_ID", referencedColumnName = "ID")
    private Bank bank;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "account",cascade = {CascadeType.MERGE,CascadeType.PERSIST},orphanRemoval = true)
    private Set<Transaction> transactionSet;

    public boolean validatePin(Long pin){
        return Objects.equals(this.pin, pin);
    }

    @Override
    public String toString() {
        return "Account{" +
                ", accountNumber =" + accountNumber +
                ", pin = ******" +
                ", balance =" + balance +
                ", overdraft =" + overdraft +
                ", bank = " + bank +
                ", transactions =" + transactionSet +
                '}';
    }

    public boolean checkTotalAmountIsEnough(BigDecimal amount) {
        return this.getBalance().add(this.getOverdraft()).compareTo(amount)>=0;
    }
}
