package com.example2.demo2.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    @Column(name="user_email")
    private String email;

    @Column(name="user_password")
    private String password;

    @Column(name="user_full_name")
    private String fullName;

    @Column(name="user_picture_url",columnDefinition="TEXT")
    private String pictureURL;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;



}
