package com.journaldev.bootifulmongodb.controller;

import com.journaldev.bootifulmongodb.dal.AnswerRepository;
import com.journaldev.bootifulmongodb.dal.QuestionRepository;
import com.journaldev.bootifulmongodb.dal.UserRepository;
import com.journaldev.bootifulmongodb.datamanagement.ActionResult;
import com.journaldev.bootifulmongodb.datamanagement.AnswerObject;
import com.journaldev.bootifulmongodb.model.Answer;
import com.journaldev.bootifulmongodb.model.Question;
import com.journaldev.bootifulmongodb.model.User;
import com.journaldev.bootifulmongodb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/answers")
public class AnswerController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserService service;

    @RequestMapping("/set")
    public ActionResult<Answer> setAnswer(@RequestParam("token") String from,
                                          @RequestParam("question") String question,
                                          @RequestParam("text") String text){
        ActionResult<Answer> result = new ActionResult<>();
        User user = service.getByToken(from);
        Question q = questionRepository.getById(question);
        if (q != null && user != null){
            Answer answer = new Answer();
            answer.setQuestion(question);
            answer.setUser(user.getUserId());
            answer.setText(text);
            answerRepository.save(answer);
            result.setSuccess(true);
            result.setData(answer);
            if (q.getFrom() != null){
                User f = userRepository.getByUserId(q.getFrom());
                service.sendEmail("new answer to your question", text, f.getEmail());
            }
        }
        else {
            result.setMessage("question or user not found");
        }
        return result;
    }

   @RequestMapping("/UpVote")
    public ActionResult<Answer> upvote(@RequestParam("token") String token,
                                        @RequestParam("answer") String answerid){
        ActionResult<Answer> result = new ActionResult<>();
        User user = userRepository.getByUserId(token);
        Answer answer = answerRepository.getById(answerid);

        if (user != null && answer != null){
            answer.setScore(answer.getScore()+1);
            answer = answerRepository.save(answer);
            result.setMessage("UPVOTED");
            result.setData(answer);
            result.setSuccess(true);
        }else {
            result.setMessage("NO such answer or pls login to vote");
        }
        return result;
    }

    @RequestMapping("/DownVote")
    public ActionResult<Answer> downvote(@RequestParam("token") String token,
                                       @RequestParam("answer") String answerid){
        ActionResult<Answer> result = new ActionResult<>();
        User user = userRepository.getByUserId(token);
        Answer answer = answerRepository.getById(answerid);

        if (user != null && answer != null){
            answer.setScore(answer.getScore()-1);
            answer = answerRepository.save(answer);
            result.setMessage("DOWNVOTED");
            result.setData(answer);
            result.setSuccess(true);
        }else {
            result.setMessage("NO such answer or pls login to vote");
        }
        return result;
    }

    @RequestMapping("/answer")
    public ActionResult<AnswerObject> ansPage(@RequestParam("id") String id){
        ActionResult<AnswerObject> result = new ActionResult<>();
        AnswerObject answerObject = new AnswerObject();
        Answer answer = answerRepository.getById(id);
        User user = userRepository.getByUserId(answer.getUser());
        Page<Answer> others = answerRepository.findAllByIdIsNotAndUser(id,answer.getUser(),new PageRequest(0,3));
        if (answer != null){
            answerObject.setAnswer(answer);
            answerObject.setUser(user);
            answerObject.setOthers(others.getContent());
            result.setData(answerObject);
            result.setSuccess(true);
        }else {
            result.setMessage("No such Answer");
        }
        return result;
    }
}
