package com.example2.demo2.services.impls;

import com.example2.demo2.entities.Comment;
import com.example2.demo2.repositories.CommentRepository;
import com.example2.demo2.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> findAllByItemOOrderByAddedDateAsc(Long id){
        return commentRepository.findAllByItem_IdOrderByAddedDateAsc(id);
    }

    @Override
    public List<Comment> listAllComments() {

        return commentRepository.findAll();
    }

    @Override
    public Comment listComment(Long id) {
        return commentRepository.getOne(id);
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);

    }

    @Override
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }
}
