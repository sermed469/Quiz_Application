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

    public static ArrayList<Person> personList = new ArrayList<>();

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

    public static ArrayList<Person> getPersonList(){

        //Person sermet = new Person("sermet","kerim","16.12.1998","sermet@mail.com",SignUpScreen.hashPassword("12345"), R.drawable.sermet);
        /*Person ali = new Person();
        Person leyla = new Person();
        Person berk = new Person();
        Person zeynep = new Person();
*/
        //personList.add(sermet);
        /*personList.add(ali);
        personList.add(leyla);
        personList.add(berk);
        personList.add(zeynep);*/

        return personList;
    }

    public static void setPersonList(ArrayList<Person> personList) {
        Person.personList = personList;
    }
}
