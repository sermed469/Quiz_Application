package tr.yildiz.edu.sermedkerim;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Person {

    String name;
    String surname;
    String birth;
    String email;
    String password;
    String phone;
    Bitmap avatar;

    public Person(String name, String surname, String birth, String email, String password, String phone, Bitmap avatar) {
        this.name = name;
        this.surname = surname;
        this.birth = birth;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getBirth() {
        return birth;
    }

    public String getPhone() {
        return phone;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

}
