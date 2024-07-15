package com.ing.hubs.entity.student;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column (name = "street_address")
    private String streetAddress;
    @Column
    private String city;
    @Column(name = "postal_code")
    private String postalCode;
    @Column
    private String country;

    @OneToOne(mappedBy = "location")
    private Student student;
}
