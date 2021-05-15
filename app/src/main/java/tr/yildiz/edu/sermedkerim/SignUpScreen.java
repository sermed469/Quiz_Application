package tr.yildiz.edu.sermedkerim;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SignUpScreen extends AppCompatActivity {

    EditText name, surname, email, password1, password2, phone;
    ImageView avatar;
    TextView dateofbirth;
    Button signup;
    Bitmap selectedImageBitmap;
    ArrayList<EditText> editTexts = new ArrayList<>();
    DatePickerDialog.OnDateSetListener dateSetListener;
    static final int REQUEST_IMAGE_OPEN = 100;
    Uri URI = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        getReferences();

        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        editTexts.add(name);
        editTexts.add(surname);
        editTexts.add(email);
        editTexts.add(password1);
        editTexts.add(password2);
        editTexts.add(phone);

        dateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(SignUpScreen.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,dateSetListener,year,month,day);
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                dateofbirth.setText(dayOfMonth + "/" + month + "/" + year);
            }
        };

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, REQUEST_IMAGE_OPEN);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNotEmptyField()){
                    if(isEmailValid()){
                        if (isSameUser()){
                            if (password1.getText().toString().matches(password2.getText().toString())){
                                if(URI != null){

                                    String hashedPassword = hashPassword(password1.getText().toString());

                                    Person person = new Person(name.getText().toString(),surname.getText().toString(),dateofbirth.getText().toString(),email.getText().toString(),hashedPassword,phone.getText().toString(),selectedImageBitmap);
                                    /*Person.personList.add(person);
                                    Person.setPersonList(Person.personList);*/

                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    selectedImageBitmap.compress(Bitmap.CompressFormat.PNG,50, byteArrayOutputStream);
                                    byte[] bytes = byteArrayOutputStream.toByteArray();

                                    SharedPreferences sharedPreferences = getSharedPreferences("Shared",MODE_PRIVATE);

                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                    editor.putString("name",name.getText().toString());
                                    editor.putString("email",email.getText().toString());
                                    editor.putString("photo", Arrays.toString(bytes));
                                    editor.apply();

                                    ContentValues values = new ContentValues();
                                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_NAME, name.getText().toString());
                                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SURNAME, surname.getText().toString());
                                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_EMAIL, email.getText().toString());
                                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PHONE, phone.getText().toString());
                                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DATEOFBIRTH, dateofbirth.getText().toString());
                                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PASSWORD, hashedPassword);
                                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_AVATAR, bytes);

                                    long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);

                                    Intent intent = new Intent(SignUpScreen.this,ShowScreen.class);
                                    intent.putExtra("name",name.getText().toString());
                                    intent.putExtra("avatar",bytes);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"You must select an image",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Passwords don't match",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"This user is already signed up",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Email is not valid",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"All input fields must be non empty",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_OPEN && resultCode == RESULT_OK){
            URI = data.getData();
            try {
                if(Build.VERSION.SDK_INT >= 28){
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),URI);
                    selectedImageBitmap = ImageDecoder.decodeBitmap(source);
                    avatar.setImageBitmap(selectedImageBitmap);
                }
                else{
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),URI);
                    avatar.setImageBitmap(selectedImageBitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void getReferences(){

        name = findViewById(R.id.editTextName);
        surname = findViewById(R.id.editTextSurname);
        email = findViewById(R.id.editTextEmailSignUp);
        dateofbirth = findViewById(R.id.textView);
        phone = findViewById(R.id.editTextPhone);
        password1 = findViewById(R.id.editTextPasswordSignUp);
        password2 = findViewById(R.id.editTextPasswordSignUp2);
        avatar = findViewById(R.id.selectAvatarImageView);
        signup = findViewById(R.id.SignUpButton);
    }

    public boolean isNotEmptyField(){

        for(EditText editText : editTexts){
            if (editText.getText().toString().matches("")){
                return  false;
            }
        }

        if(dateofbirth.getText().toString().matches("dd/mm/yy")){
            return false;
        }

        return true;
    }

    public boolean isSameUser(){

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

        if (itemIds.size() == 0){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isEmailValid(){
        String Email = email.getText().toString();
        if(Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            return true;
        }
        else{
            return false;
        }
    }

    public static String hashPassword(String password){

        StringBuffer buffer = new StringBuffer();

        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(password.getBytes());
            byte[] bytes = md.digest();

            for (int i = 0;i<bytes.length;i++) {
                buffer.append(Integer.toHexString(0xFF & bytes[i]));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }
}