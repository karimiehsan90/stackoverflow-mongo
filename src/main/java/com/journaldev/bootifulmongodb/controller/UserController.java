package com.journaldev.bootifulmongodb.controller;


import com.journaldev.bootifulmongodb.dal.AnswerRepository;
import com.journaldev.bootifulmongodb.dal.QuestionRepository;
import com.journaldev.bootifulmongodb.datamanagement.ActionResult;
import com.journaldev.bootifulmongodb.datamanagement.ProfileObject;
import com.journaldev.bootifulmongodb.model.Answer;
import com.journaldev.bootifulmongodb.model.Question;
import com.journaldev.bootifulmongodb.model.QuestionType;
import com.journaldev.bootifulmongodb.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import com.journaldev.bootifulmongodb.dal.UserRepository;
import com.journaldev.bootifulmongodb.model.User;

import java.util.*;


@RestController
@RequestMapping(value = "/user")
public class UserController {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService service;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@RequestMapping("/register")
	public ActionResult<User> register(@RequestParam("email") String email,
								 		@RequestParam("password") String password,
									   	@RequestParam("interests") String is){
		ActionResult<User> result = new ActionResult<>();
		User contains = userRepository.getByEmail(email);
		if (contains != null){
			result.setMessage("already registered");
		}
		else {
			String[] interests = is.split("[ ]*,[ ]*");
			User user = new User();
			user.setEmail(email);
			user.setPassword(password);
			user.setInterests(interests);
			userRepository.save(user);
			result.setData(user);
			result.setSuccess(true);
		}
		return result;
	}

	@RequestMapping("/login")
	public ActionResult<User> login(@RequestParam("email") String email,
									@RequestParam("password") String password){
		User user = userRepository.getByEmailAndPassword(email, password);
		ActionResult<User> result = new ActionResult<>();
		result.setData(user);
		result.setSuccess(user != null);
		result.setMessage(user != null ? null : "email or password is wrong");
		return result;
	}

	@RequestMapping("/forget")
	public ActionResult<Boolean> forgetPassword(@RequestParam("email") String email){
		ActionResult<Boolean> result = new ActionResult<>();
		User user = userRepository.getByEmail(email);
		if (user != null){
			result.setSuccess(true);
			service.sendEmail("forget password", user.getPassword(), email);
		}
		else {
			result.setMessage("wrong email");
		}
		return result;
	}

	@RequestMapping("/profile")
	public ActionResult<ProfileObject> getProfile(@RequestParam("to") String toMail , @RequestParam("page") int page){
		ActionResult<ProfileObject> result = new ActionResult<>();
		User user = userRepository.getByEmail(toMail);
		if (user != null) {
			Page<Question> questions = questionRepository.findAllByFromAndType(user.getUserId(), QuestionType.PUBLIC,new PageRequest(page,3));
			Page<Answer> answers = answerRepository.findAllByUser(user.getUserId(),new PageRequest(page,3));
			ProfileObject object = new ProfileObject();
			object.setAnswers(answers.getContent());
			object.setQuestions(questions.getContent());
			result.setData(object);
			result.setSuccess(true);
		}
		else {
			result.setMessage("user not found");
		}
		return result;
	}

	@RequestMapping("/edit")
	public ActionResult<Boolean> editProfile(@RequestParam("email") String email,
											 @RequestParam("password") String password,
											 @RequestParam("token") String token){
		ActionResult<Boolean> result = new ActionResult<>();
		User user = service.getByToken(token);
		if (user != null){
			result.setSuccess(true);
			user.setEmail(email);
			user.setPassword(password);
			userRepository.save(user);
		}
		else {
			result.setMessage("authentication failed");
		}
		return result;
	}

	@RequestMapping("/main")
	public ActionResult<Set<Question>> mainPage(@RequestParam("token") String id , @RequestParam("page") int page){
		ActionResult<Set<Question>> result = new ActionResult<>();
		User user = userRepository.getByUserId(id);
		if (user != null) {
			String[] intrests =user.getInterests();
			Set<Question> set = new HashSet<>();
			set.addAll(questionRepository.findAllByTopicsIsContaining(intrests , new PageRequest(page,5)).getContent());
			result.setData(set);
			result.setSuccess(true);
		}else {
			result.setMessage("No Such User");
		}
		return result;
	}

}