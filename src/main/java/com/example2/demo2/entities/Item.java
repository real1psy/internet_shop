package com.example2.demo2.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name="Item")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="item_id")
    private Long id;

    @Column(name="item_name")
    private String name;

    @Column(name="item_description", columnDefinition="TEXT")
    private String description;

    @Column(name="item_price")
    private double price;

    @Column(name="item_rating")
    private int rating;

    @Column(name="item_count")
    private int count;


    @Column(name="item_small_pic_url", columnDefinition="TEXT")
    private String smallPicUrl;

    @Column(name="item_large_pic_url", columnDefinition="TEXT")
    private String largePicUrl;

    @Column(name="item_added_date")
    private Date addedDate;

    @Column(name="item_in_top_page")
    private boolean inTopPage;


    @ManyToOne(fetch = FetchType.EAGER)
    private Brand brand;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Category> categories;





}
