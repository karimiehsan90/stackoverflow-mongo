package com.journaldev.bootifulmongodb.controller;

import com.journaldev.bootifulmongodb.dal.AnswerRepository;
import com.journaldev.bootifulmongodb.dal.CommentRepository;
import com.journaldev.bootifulmongodb.dal.UserRepository;
import com.journaldev.bootifulmongodb.datamanagement.ActionResult;
import com.journaldev.bootifulmongodb.model.Answer;
import com.journaldev.bootifulmongodb.model.Comment;
import com.journaldev.bootifulmongodb.model.User;
import com.journaldev.bootifulmongodb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService service;

    @RequestMapping("/setForAns")
    public ActionResult<Comment> setCommentAns( @RequestParam("token") String from,
                                                @RequestParam("answer") String answer,
                                                @RequestParam("body") String body){
        ActionResult<Comment> result = new ActionResult<>();
        User user = service.getByToken(from);
        Answer a = answerRepository.getById(answer);
        if (a != null && user != null){
            Comment comment = new Comment();
            comment.setTo(answer);
            comment.setUser(user.getUserId());
            comment.setBody(body);
            commentRepository.save(comment);
            result.setSuccess(true);
            result.setData(comment);
            if (a.getUser() != null){
                User f = userRepository.getByUserId(a.getUser());
                service.sendEmail("new comment to your answer from " + user.getUserId() , body, f.getEmail());
            }
        }
        else {
            result.setMessage("answer or user not found");
        }
        return result;
    }

    @RequestMapping("/setForComment")
    public ActionResult<Comment> setCommentCom( @RequestParam("token") String from,
                                                @RequestParam("comment") String comment,
                                                @RequestParam("body") String body){
        ActionResult<Comment> result = new ActionResult<>();
        User user = service.getByToken(from);
        Comment c = commentRepository.getById(comment);
        if (c != null && user != null){
            Comment newcomment = new Comment();
            newcomment.setTo(comment);
            newcomment.setUser(user.getUserId());
            newcomment.setBody(body);
            commentRepository.save(newcomment);
            result.setSuccess(true);
            result.setData(newcomment);
        }
        else {
            result.setMessage("comment or user not found");
        }
        return result;
    }

    @RequestMapping("/show")
    public ActionResult<List> showComments(@RequestParam("to") String to){
        ActionResult<List> result = new ActionResult<>();
        Answer a = answerRepository.getById(to);
        Comment c = commentRepository.getById(to);
        List<Comment> comments = commentRepository.getByTo(to);
        if (a != null || c != null) {
            result.setData(comments);
            result.setMessage("Here's Comments");
            result.setSuccess(true);
        }else {
            result.setMessage("No Such comments or answer");
        }
        return result;
    }
}
