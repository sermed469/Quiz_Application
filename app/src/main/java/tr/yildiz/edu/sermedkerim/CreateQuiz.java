package tr.yildiz.edu.sermedkerim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class CreateQuiz extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText time2, score2;
    Spinner difficulty2;
    Button updateSettings;
    RecyclerView chooseRecycler;
    SelectQuestionAdapter selectQuestionAdapter;
    ArrayList<Question> questions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);

        getReferences();

        SharedPreferences sharedPreferences = getSharedPreferences("Shared",MODE_PRIVATE);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficulty2.setAdapter(adapter);
        difficulty2.setOnItemSelectedListener(this);

        time2.setText(sharedPreferences.getString("time","0"));
        score2.setText(sharedPreferences.getString("score","0"));
        String dif = sharedPreferences.getString("difficulty","2");

        switch (dif){
            case "Two":
                difficulty2.setSelection(0);
                break;
            case "Three":
                difficulty2.setSelection(1);
                break;
            case "Four":
                difficulty2.setSelection(2);
                break;
            case "Five":
                difficulty2.setSelection(3);
                break;
            default:
                break;
        }

        for(Question q : Question.getQuestions()){
            if(q.getChoices().size() == difficulty2.getSelectedItemPosition() + 2){
                questions.add(q);
            }
        }

        chooseRecycler = findViewById(R.id.ChooseRecylerView);
        chooseRecycler.setLayoutManager(new LinearLayoutManager(CreateQuiz.this));
        selectQuestionAdapter = new SelectQuestionAdapter(questions,CreateQuiz.this);
        chooseRecycler.setAdapter(selectQuestionAdapter);

        updateSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("time",time2.getText().toString());
                editor.putString("score",score2.getText().toString());
                editor.putString("difficulty",difficulty2.getItemAtPosition(difficulty2.getSelectedItemPosition()).toString());
                editor.apply();
            }
        });
    }

    public void getReferences(){

        time2 = findViewById(R.id.QuizTimeEditText2);
        score2 = findViewById(R.id.QuestionScoreEditText2);
        difficulty2 = findViewById(R.id.spinner2);
        updateSettings = findViewById(R.id.updateSettingsButton);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        questions.clear();

        for(Question q : Question.getQuestions()){
            if(q.getChoices().size() == position + 2){
                questions.add(q);
            }
        }

        selectQuestionAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}