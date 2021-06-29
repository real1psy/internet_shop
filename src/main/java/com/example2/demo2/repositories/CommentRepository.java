package com.example2.demo2.repositories;

import com.example2.demo2.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@Transactional
public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findAllByItem_IdOrderByAddedDateAsc(Long id);
}
