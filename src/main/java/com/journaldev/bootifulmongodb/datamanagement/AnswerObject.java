package com.journaldev.bootifulmongodb.datamanagement;

import com.journaldev.bootifulmongodb.model.Answer;
import com.journaldev.bootifulmongodb.model.Question;
import com.journaldev.bootifulmongodb.model.User;

import java.util.List;

public class AnswerObject {
    private Answer answer;
    private User user;
    private List<Answer> others ;


    public List<Answer> getOthers() {
        return others;
    }

    public void setOthers(List<Answer> others) {
        this.others = others;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
}
