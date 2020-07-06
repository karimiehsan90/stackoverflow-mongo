package com.journaldev.bootifulmongodb.datamanagement;

import com.journaldev.bootifulmongodb.model.Answer;
import com.journaldev.bootifulmongodb.model.Question;

import java.util.List;

public class QuestionObject {
    private Question question;
    private List<Answer> answers;

    public QuestionObject(Question question, List<Answer> answers) {
        this.question = question;
        this.answers = answers;
    }

    public QuestionObject() {
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
