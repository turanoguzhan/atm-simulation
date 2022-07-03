package com.ouz.atmsimulation.entity;

import com.ouz.atmsimulation.enums.StatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    protected Long ID;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    protected StatusType status;

    @Column(name="VERSION")
    @Version
    protected Integer version;

    @Column(name = "CREATED_BY")
    protected String created_by;

    @Column(name="CREATED_DATE")
    protected LocalDateTime created_date;

    @Column(name = "LAST_MODIFIED_BY")
    protected String last_modified_by;

    @Column(name="LAST_MODIFIED_DATE")
    protected LocalDateTime last_modified_date;

}
