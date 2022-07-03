package com.ouz.atmsimulation.entity;

import com.ouz.atmsimulation.enums.BanknoteType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "ATM")
public class ATM extends BaseEntity{

   @Column(name = "ATM_NO")
   private Long atmNo;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "BANK_ID", referencedColumnName = "ID")
   private Bank bank;

   @Enumerated(EnumType.STRING)
   @Column(name="BANKNOTE_TYPE")
   private BanknoteType banknoteType;

   @Column(name="BANKNOTE_COUNT")
   private Long banknoteCount;

}
