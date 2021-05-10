package tr.yildiz.edu.sermedkerim;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddQuestionScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText question, choice1, choice2, choice3, choice4, choice5, answer;
    TextView datasource;
    Button addFile, upload, show;
    Spinner spinner;
    String result;
    ArrayList<Question> questions = new ArrayList<>();
    static final int REQUEST_IMAGE_OPEN = 1;
    Uri URI = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question_screen);

        getReferences();

        Intent intent = getIntent();
        result = intent.getStringExtra("update");

        if(result != null && result.matches("yes")){
            int index = intent.getIntExtra("position",-1);
            questions = Question.getQuestions();
            Question q = questions.get(index);
            question.setText(q.question);
            choice1.setText(q.getChoices().get(0));
            choice2.setText(q.getChoices().get(1));
            choice3.setText(q.getChoices().get(2));
            choice4.setText(q.getChoices().get(3));
            choice4.setText(q.getChoices().get(4));
            answer.setText(q.getAnswer());

            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Question.questions.get(index).setAnswer(question.getText().toString());

                    ArrayList<String> choices = new ArrayList<>();
                    choices.add(choice1.getText().toString());
                    choices.add(choice2.getText().toString());
                    choices.add(choice3.getText().toString());
                    choices.add(choice4.getText().toString());
                    choices.add(choice5.getText().toString());

                    Question.questions.get(index).setChoices(choices);

                    Question.questions.get(index).setAnswer(answer.getText().toString());

                    Intent intentToList = new Intent(AddQuestionScreen.this,QuestionListActivity.class);
                    startActivity(intentToList);
                }
            });
        }
        else{

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.numbers, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);

            addFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Intent intentToGallery = new Intent();
                    intentToGallery.setType("image/*");
                    intentToGallery.setAction(Intent.ACTION_GET_CONTENT);
                    intentToGallery.putExtra("return-data",true);
                    startActivityForResult(Intent.createChooser(intentToGallery,"Complete action using"), REQUEST_IMAGE_OPEN);*/
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType("image/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent, REQUEST_IMAGE_OPEN);
                }
            });

            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(isEmpty()){
                        if(isChoicesNotSame()){
                            if(isAnswerTypeCorrect()){
                                ArrayList<Question> questions = new ArrayList<>();

                                ArrayList<String> choices = new ArrayList<>();
                                choices.add(choice1.getText().toString());
                                choices.add(choice2.getText().toString());
                                if(choice3.isEnabled()){
                                    choices.add(choice3.getText().toString());
                                }
                                if(choice4.isEnabled()){
                                    choices.add(choice4.getText().toString());
                                }
                                if(choice5.isEnabled()){
                                    choices.add(choice5.getText().toString());
                                }

                                Question q = new Question(question.getText().toString(),choices,answer.getText().toString());
                                questions = Question.getQuestions();
                                questions.add(q);
                                Question.setQuestions(questions);

                                Intent intent = new Intent(AddQuestionScreen.this,ShowScreen.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Answer does not match any of the choices",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Choices must be different",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"All fields must be non empty",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URI.toString()));

                    startActivity(mapIntent);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_OPEN && resultCode == RESULT_OK){
            URI = data.getData();
            datasource.setText(URI.getLastPathSegment());
            datasource.setVisibility(View.VISIBLE);
        }
    }

    public void getReferences(){
        question = findViewById(R.id.editTextQuestion);
        choice1 = findViewById(R.id.editTextChoice1);
        choice2 = findViewById(R.id.editTextChoice2);
        choice3 = findViewById(R.id.editTextChoice3);
        choice4 = findViewById(R.id.editTextChoice4);
        choice5 = findViewById(R.id.editTextChoice5);
        answer = findViewById(R.id.editTextAnswer);
        addFile = findViewById(R.id.AddFileButton);
        upload = findViewById(R.id.UploadQuestionButton);
        datasource = findViewById(R.id.DataSourceText);
        show = findViewById(R.id.showButton);
        spinner = findViewById(R.id.spinner);
    }

    public boolean isEmpty(){
        if(question.getText().toString().matches("")){
            return false;
        }
        if(choice1.getText().toString().matches("")){
            return false;
        }
        if(choice2.getText().toString().matches("")){
            return false;
        }
        if(choice3.isEnabled()){
            if(choice3.getText().toString().matches("")){
                return false;
            }
        }
        if(choice4.isEnabled()){
            if(choice4.getText().toString().matches("")){
                return false;
            }
        }
        if(choice5.isEnabled()){
            if(choice5.getText().toString().matches("")){
                return false;
            }
        }
        if(answer.getText().toString().matches("")){
            return false;
        }

        return true;
    }

    public boolean isAnswerTypeCorrect(){
        if(answer.getText().toString().matches(choice1.getText().toString())){
            return true;
        }
        if(answer.getText().toString().matches(choice2.getText().toString())){
            return true;
        }
        if(choice3.isEnabled()){
            if(answer.getText().toString().matches(choice3.getText().toString())){
                return true;
            }
        }
        if(choice4.isEnabled()){
            if(answer.getText().toString().matches(choice4.getText().toString())){
                return true;
            }
        }
        if(choice5.isEnabled()){
            if(answer.getText().toString().matches(choice5.getText().toString())){
                return true;
            }
        }
        return false;
    }

    public boolean isChoicesNotSame(){

        ArrayList<EditText> editTexts = new ArrayList<>();
        editTexts.add(choice1);
        editTexts.add(choice2);
        editTexts.add(choice3);
        editTexts.add(choice4);
        editTexts.add(choice5);

        ArrayList<String> choices = new ArrayList<>();

        for(EditText editText : editTexts){
            if(editText.isEnabled()){
                choices.add(editText.getText().toString());
            }
        }

        int i, j;

        for (i = 0; i < choices.size(); i++){
            for (j = i+1; j < choices.size(); j++){
                if(choices.get(i).matches(choices.get(j))){
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String text = parent.getItemAtPosition(position).toString();

        switch (text){
            case "Two":
                choice3.setEnabled(false);
                choice4.setEnabled(false);
                choice5.setEnabled(false);
                break;
            case "Three":
                choice3.setEnabled(true);
                choice4.setEnabled(false);
                choice5.setEnabled(false);
                break;
            case "Four":
                choice3.setEnabled(true);
                choice4.setEnabled(true);
                choice5.setEnabled(false);
                break;
            case "Five":
                choice3.setEnabled(true);
                choice4.setEnabled(true);
                choice5.setEnabled(true);
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}