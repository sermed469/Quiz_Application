package tr.yildiz.edu.sermedkerim;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable {

    String question;
    ArrayList<String> choices;
    String Answer;
    String attachment;
    String attachmentType;

    public Question(String question, ArrayList<String> choices, String answer, String attachment, String attachmentType) {
        this.question = question;
        this.choices = choices;
        Answer = answer;
        this.attachment = attachment;
        this.attachmentType = attachmentType;
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

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getAttachment() {
        return attachment;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }
}
