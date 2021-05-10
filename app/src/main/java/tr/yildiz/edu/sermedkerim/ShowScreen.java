package tr.yildiz.edu.sermedkerim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowScreen extends AppCompatActivity {

    TextView userInfo;
    ImageView avatar;
    Button button;
    Button setting;
    Button createQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_screen);

        userInfo = findViewById(R.id.userInfoText);
        avatar = findViewById(R.id.imageView);
        button = findViewById(R.id.AddQuestionButton);
        setting = findViewById(R.id.SettingButton);
        createQuiz = findViewById(R.id.CreateQuizButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowScreen.this,AddQuestionScreen.class);
                startActivity(intent);
            }
        });

        Button listButton = findViewById(R.id.ListQuestionButton);

        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowScreen.this,QuestionListActivity.class);
                startActivity(intent);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowScreen.this,QuizSettings.class);
                startActivity(intent);
            }
        });

        createQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowScreen.this,CreateQuiz.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        avatar.setImageResource(intent.getIntExtra("avatarId",0));
        userInfo.setText(intent.getStringExtra("user"));


    }
}