package com.example2.demo2.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="SoldItem")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoldItem {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="count")
    private int count;

    @Column(name="ordered_date")
    private Date orderedDate;

    @ManyToOne
    private Item item;

}
