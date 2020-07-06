package com.journaldev.bootifulmongodb.dal;

import com.journaldev.bootifulmongodb.model.Question;
import com.journaldev.bootifulmongodb.model.QuestionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends MongoRepository<Question, String> , PagingAndSortingRepository<Question,String> {

    public Question getById(String id);

    // public List<Question> getByFromAndType(String from, QuestionType type);
    public Page<Question> findAllByFromAndType(String from, QuestionType type, Pageable pageable);

    public Page<Question> findAllByTopicsIsContaining(String[] topic , Pageable pageable);

    public Page<Question> findAll(Pageable pageable);
}
