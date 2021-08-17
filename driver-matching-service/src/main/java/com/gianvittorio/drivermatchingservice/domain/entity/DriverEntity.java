package com.gianvittorio.drivermatchingservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("drivers")
public class DriverEntity {

    @Id
    @Column("id")
    private Long id;

    @NotNull
    @Column("firstname")
    private String firstname;

    @NotNull
    @Column("lastname")
    private String lastname;

    @NotNull
    @Column("document")
    private String document;

    @NotNull
    @Column("phone")
    private String phone;

    @NotNull
    @Column("email")
    private String email;

    @NotNull
    @Column("category")
    private String category;

    @NotNull
    @Column("location")
    private String location;

    @NotNull
    @Column("is_available")
    private Boolean isAvailable;

    @NotNull
    @Column("rating")
    private Integer rating;
}
