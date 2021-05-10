package tr.yildiz.edu.sermedkerim;

import java.util.ArrayList;

public class Question {

    String question;
    ArrayList<String> choices;
    String Answer;

    public static ArrayList<Question> questions = new ArrayList<>();

    public Question(String question, ArrayList<String> choices, String answer) {
        this.question = question;
        this.choices = choices;
        Answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public static ArrayList<Question> getQuestions(){

        return questions;
    }

    public static void setQuestions(ArrayList<Question> questions){

        Question.questions = questions;
    }
}
