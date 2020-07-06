package com.journaldev.bootifulmongodb.dal;

import com.journaldev.bootifulmongodb.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String>{
    public Comment getById(String id);

    public List<Comment> getByTo(String to);
}
