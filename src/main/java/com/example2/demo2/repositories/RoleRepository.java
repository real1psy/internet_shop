package com.example2.demo2.repositories;

import com.example2.demo2.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findRoleByName(String name);

    @Query("SELECT COUNT(it) FROM Role it WHERE it.id=?1")
    Long checkRole(Long id);
}
