package com.journaldev.bootifulmongodb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.journaldev.bootifulmongodb.dal.AnswerRepository;
import com.journaldev.bootifulmongodb.dal.QuestionRepository;
import com.journaldev.bootifulmongodb.dal.UserRepository;
import com.journaldev.bootifulmongodb.datamanagement.ActionResult;
import com.journaldev.bootifulmongodb.datamanagement.QuestionObject;
import com.journaldev.bootifulmongodb.model.Answer;
import com.journaldev.bootifulmongodb.model.Question;
import com.journaldev.bootifulmongodb.model.QuestionType;
import com.journaldev.bootifulmongodb.model.User;
import com.journaldev.bootifulmongodb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionController {
    @Autowired
    private QuestionRepository repository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService service;

    @RequestMapping("/import")
    public boolean importQuestions(){
        String input = "";
        try (Scanner scanner = new Scanner(new FileInputStream("answers.json"))) {
            while (scanner.hasNextLine()){
                input += scanner.nextLine() + "\n";
            }
            ObjectMapper mapper = new ObjectMapper();
            List<LinkedHashMap<String, String>> list = mapper.readValue(input, List.class);
            for (LinkedHashMap<String, String> map:list) {
                String[] topics = map.get("topics").trim().split("[ ]*,[ ]*");
                Question question = new Question();
                question.setTopics(topics);
                question.setType(QuestionType.PRIVATE);
                question.setQuestion(map.get("question"));
                repository.save(question);
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @RequestMapping("/ask")
    public ActionResult<Question> askQuestion(@RequestParam("body") String question,
                                              @RequestParam("topics") String topics,
                                              @RequestParam("token") String from,
                                              @RequestParam("type") QuestionType type,
                                              @RequestParam(value = "to", required = false) String to){
        ActionResult<Question> result = new ActionResult<>();
        User user = service.getByToken(from);
        if (user != null) {
            String[] ts = topics.split("[ ]*,[ ]*");
            Question q = new Question();
            q.setTopics(ts);
            q.setQuestion(question);
            q.setFrom(user.getUserId());
            q.setType(type);
            repository.save(q);
            result.setSuccess(true);
            result.setData(q);
            if (to != null){
                String[] mails = to.split("[ ]*,[ ]*");
                Set<User> tos = new HashSet<>();
                for (String mail:mails) {
                    User toMail = userRepository.getByEmail(mail);
                    if (toMail != null){
                        tos.add(toMail);
                    }
                }
                for (User t:tos) {
                    service.sendEmail("tagged in question", q.getId(), t.getEmail());
                }
            }
        }
        else {
            result.setMessage("authentication failed");
        }
        return result;
    }

    @RequestMapping("/list/page/{num}")
    public ActionResult<List<Question>> showPage(@PathVariable int num){
        ActionResult<List<Question>> result = new ActionResult<>();
        Page<Question> page = repository.findAll(new PageRequest(num, 5));
        result.setData(page.getContent());
        return result;
    }

    @RequestMapping("/question")
    public ActionResult<QuestionObject> q_page(@RequestParam("id") String id , @RequestParam("page") int pnum){
       ActionResult<QuestionObject> result = new ActionResult<>();
        Question q = repository.getById(id);
        Page<Answer> answers = answerRepository.findAllByQuestion(id,new PageRequest(pnum,5));
        if (q != null){
            result.setData(new QuestionObject(q,answers.getContent()));
            result.setSuccess(true);
        }else{
            result.setMessage("No Such Question");
        }
        return result;
    }

}
