package com.example2.demo2.services;

import com.example2.demo2.entities.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> findAllByItemOOrderByAddedDateAsc(Long id);
    List<Comment> listAllComments();
    Comment listComment(Long id);
    void deleteComment(Long id);
    void saveComment(Comment comment);

}
