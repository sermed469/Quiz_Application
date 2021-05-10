package tr.yildiz.edu.sermedkerim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class QuestionListActivity extends AppCompatActivity {

    RecyclerView recycleview;
    QuestionAdapter questionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        recycleview = findViewById(R.id.recyclerView);
        recycleview.setLayoutManager(new LinearLayoutManager(QuestionListActivity.this));
        questionAdapter = new QuestionAdapter(Question.getQuestions(),QuestionListActivity.this);
        recycleview.setAdapter(questionAdapter);
    }


}