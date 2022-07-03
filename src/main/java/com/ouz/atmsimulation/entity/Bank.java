package com.ouz.atmsimulation.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name="BANK")
public class Bank extends BaseEntity{

    @Column(name="NAME")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bank", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private Set<Account> accounts;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bank", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private Set<ATM> atms;

    @Override
    public String toString() {
        return "Bank{" +
                "name='" + name + '\'' +
                '}';
    }
}
