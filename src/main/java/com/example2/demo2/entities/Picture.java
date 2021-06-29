package com.example2.demo2.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="Pictures")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Picture {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="picture_id")
    private Long id;

    @Column(name="pucture_url")
    private String url;

    @Column(name="picture_added_date")
    private Date addedDate;

    @ManyToOne(fetch = FetchType.EAGER)
    private Item item;
}
