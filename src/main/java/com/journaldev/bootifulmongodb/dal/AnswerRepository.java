package com.journaldev.bootifulmongodb.dal;

import com.journaldev.bootifulmongodb.model.Answer;
import com.journaldev.bootifulmongodb.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends MongoRepository<Answer, String> , PagingAndSortingRepository<Answer,String> {

    public List<Answer> getByUser(String user);

    public Page<Answer> findAllByUser(String user , Pageable pageable);
    public Page<Answer> findAllByIdIsNotAndUser(String id ,String user , Pageable pageable);
    public List<Answer> findAllByQuestion(String question);

    public  Answer getById(String id);

}
