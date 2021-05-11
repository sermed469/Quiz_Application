package tr.yildiz.edu.sermedkerim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    Button signin,signup;
    public ArrayList<Person> personList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getReferences();

        personList = Person.getPersonList();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                String[] projection = {
                        BaseColumns._ID,
                        FeedReaderContract.FeedEntry.COLUMN_NAME_NAME,
                        FeedReaderContract.FeedEntry.COLUMN_NAME_SURNAME,
                        FeedReaderContract.FeedEntry.COLUMN_NAME_EMAIL,
                        FeedReaderContract.FeedEntry.COLUMN_NAME_PHONE,
                        FeedReaderContract.FeedEntry.COLUMN_NAME_DATEOFBIRTH,
                        FeedReaderContract.FeedEntry.COLUMN_NAME_PASSWORD,
                        FeedReaderContract.FeedEntry.COLUMN_NAME_AVATAR,
                };

                String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_EMAIL + " = ?";
                String[] selectionArgs = { email.getText().toString() };

                String sortOrder =
                        FeedReaderContract.FeedEntry.COLUMN_NAME_NAME + " DESC";

                Cursor cursor = db.query(
                        FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
                        projection,             // The array of columns to return (pass null to get all)
                        selection,              // The columns for the WHERE clause
                        selectionArgs,          // The values for the WHERE clause
                        null,                   // don't group the rows
                        null,                   // don't filter by row groups
                        sortOrder               // The sort order
                );

                List itemIds = new ArrayList<>();
                ArrayList<String> Passwords = new ArrayList<>();
                ArrayList<String> names = new ArrayList<>();
                ArrayList<byte[]> avatars = new ArrayList<>();
                while(cursor.moveToNext()) {
                    long itemId = cursor.getLong(
                            cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));

                    Passwords.add(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_PASSWORD)));
                    names.add(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_NAME)));
                    avatars.add(cursor.getBlob(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_AVATAR)));
                    itemIds.add(itemId);
                }
                cursor.close();

                if(itemIds.size() != 0){

                    if(Passwords.get(0).matches(SignUpScreen.hashPassword(password.getText().toString()))){
                        Toast.makeText(getApplicationContext(),"Welcome " + names.get(0),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,ShowScreen.class);
                        intent.putExtra("name",names.get(0));
                        intent.putExtra("avatar",avatars.get(0));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(MainActivity.this,"Password is wrong",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this,"There is no user with this email",Toast.LENGTH_SHORT).show();
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignUpScreen.class);
                startActivity(intent);
            }
        });
    }

    public void getReferences(){

        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        signin = findViewById(R.id.buttonSignIn);
        signup = findViewById(R.id.buttonSignUp);
    }

}