package tr.yildiz.edu.sermedkerim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowScreen extends AppCompatActivity {

    TextView userInfo;
    ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_screen);

        userInfo = findViewById(R.id.userInfoText);
        avatar = findViewById(R.id.imageView);

        Intent intent = getIntent();
        avatar.setImageResource(intent.getIntExtra("avatarId",0));
        userInfo.setText(intent.getStringExtra("user"));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.quiz_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.add_question_actionbar:
                Intent addQuestionIntent = new Intent(ShowScreen.this,AddQuestionScreen.class);
                startActivity(addQuestionIntent);
                break;
            case R.id.list_questions_actionbar:
                Intent questionListIntent = new Intent(ShowScreen.this,QuestionListActivity.class);
                startActivity(questionListIntent);
                break;
            case R.id.quiz_settings_actionbar:
                Intent QuizSettingIntent = new Intent(ShowScreen.this,QuizSettings.class);
                startActivity(QuizSettingIntent);
                break;
            case R.id.create_quiz_actionbar:
                Intent CreateQuizIntent = new Intent(ShowScreen.this,CreateQuiz.class);
                startActivity(CreateQuizIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}