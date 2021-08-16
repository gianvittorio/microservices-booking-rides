package com.gianvittorio.drivermatchingservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("drivers")
public class DriverEntity {

    @Id
    @Column("id")
    private Long id;

    @Column("firstname")
    private String firstname;

    @Column("lastname")
    private String lastname;

    @Column("document")
    private String document;

    @Column("phone")
    private String phone;

    @Column("email")
    private String email;

    @Column("category")
    private String category;

    @Column("is_available")
    private Boolean isAvailable;

    @Column("rating")
    private Integer rating;
}
