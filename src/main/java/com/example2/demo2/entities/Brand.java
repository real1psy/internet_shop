package com.example2.demo2.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="Brands")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Brand {

    @Id@GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="brand_id")
    private Long id;

    @Column(name="brand_name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    private Country country;
}
