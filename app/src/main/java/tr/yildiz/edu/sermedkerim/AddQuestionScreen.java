package tr.yildiz.edu.sermedkerim;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;
import java.util.ArrayList;

public class AddQuestionScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText question, choice1, choice2, choice3, choice4, choice5, answer;
    TextView datasource;
    Button addFile, upload, show;
    Spinner spinner;
    Spinner choiceFile;
    String result;
    Bitmap selectedImageBitmap;
    ArrayList<Question> questions = new ArrayList<>();
    static final int REQUEST_IMAGE_OPEN = 1;
    Uri URI = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question_screen);

        getReferences();

        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

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

            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.file, android.R.layout.simple_spinner_item);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            choiceFile.setAdapter(adapter2);
            choiceFile.setOnItemSelectedListener(this);

            addFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String fileType = (String) choiceFile.getItemAtPosition(choiceFile.getSelectedItemPosition());
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType(fileType+"/*");
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

                                SharedPreferences sharedPreferences = getSharedPreferences("Shared",MODE_PRIVATE);

                                String SharedEmail = sharedPreferences.getString("email", null);

                                ContentValues values = new ContentValues();
                                values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_QUESTION, question.getText().toString());
                                values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICEONE, choice1.getText().toString());
                                values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICETWO, choice2.getText().toString());

                                ArrayList<Question> questions = new ArrayList<>();

                                ArrayList<String> choices = new ArrayList<>();
                                choices.add(choice1.getText().toString());
                                choices.add(choice2.getText().toString());
                                if(choice3.isEnabled()){
                                    choices.add(choice3.getText().toString());
                                    values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICETHREE, choice3.getText().toString());
                                }
                                else{
                                    values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICETHREE, "");
                                }
                                if(choice4.isEnabled()){
                                    choices.add(choice4.getText().toString());
                                    values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICEFOUR, choice4.getText().toString());
                                }
                                else{
                                    values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICEFOUR, "");
                                }
                                if(choice5.isEnabled()){
                                    choices.add(choice5.getText().toString());
                                    values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICEFIVE, choice5.getText().toString());
                                }
                                else{
                                    values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICEFIVE, "");
                                }

                                Question q = null;

                                if(choiceFile.getItemAtPosition(choiceFile.getSelectedItemPosition()).toString().matches("image")){
                                    q = new Question(question.getText().toString(),choices,answer.getText().toString(),URI,"image");
                                }
                                else if(choiceFile.getItemAtPosition(choiceFile.getSelectedItemPosition()).toString().matches("video")){
                                    q = new Question(question.getText().toString(),choices,answer.getText().toString(),URI,"video");
                                }
                                questions = Question.getQuestions();
                                questions.add(q);
                                Question.setQuestions(questions);

                                values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_ANSWER, answer.getText().toString());
                                if(URI != null){
                                    values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_ATTACHMENT, URI.toString());
                                    values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_ATTACHMENTTYPE, choiceFile.getItemAtPosition(choiceFile.getSelectedItemPosition()).toString());
                                }
                                else{
                                    values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_ATTACHMENT, "");
                                    values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_ATTACHMENTTYPE, "");
                                }
                                values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_PERSONEMAIL,SharedEmail);

                                long newRowId = db.insert(FeedReaderContract.FeedEntry2.TABLE_NAME, null, values);

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
        choiceFile = findViewById(R.id.spinnerFileType);
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