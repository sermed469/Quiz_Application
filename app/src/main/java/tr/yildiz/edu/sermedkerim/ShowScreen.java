package tr.yildiz.edu.sermedkerim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ShowScreen extends AppCompatActivity {

    TextView userInfo;
    ImageView avatar;
    FloatingActionButton button;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_screen);

        userInfo = findViewById(R.id.userInfoText);
        avatar = findViewById(R.id.imageView);
        button = findViewById(R.id.floatingActionButton);
        button.setRippleColor(Color.MAGENTA);

        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        SharedPreferences sharedPreferences = getSharedPreferences("Shared",MODE_PRIVATE);

        String stringArray = sharedPreferences.getString("photo", null);

        if (stringArray != null) {
            String[] split = stringArray.substring(1, stringArray.length()-1).split(", ");
            byte[] array = new byte[split.length];
            for (int i = 0; i < split.length; i++) {
                array[i] = Byte.parseByte(split[i]);
            }

            bitmap = BitmapFactory.decodeByteArray(array,0,array.length);
        }

        avatar.setImageBitmap(bitmap);

        userInfo.setText(sharedPreferences.getString("name","Your Name"));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addQuestionIntent = new Intent(ShowScreen.this,AddQuestionScreen.class);
                startActivity(addQuestionIntent);
            }
        });
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