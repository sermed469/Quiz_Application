package tr.yildiz.edu.sermedkerim;

import java.util.ArrayList;

public class Person {

    String name;
    String surname;
    String birth;
    String email;
    String password;
    public static ArrayList<Person> personList = new ArrayList<>();
    int avatarId;

    public Person(String name, String surname, String birth, String email, String password, int avatarId) {
        this.name = name;
        this.surname = surname;
        this.birth = birth;
        this.email = email;
        this.password = password;
        this.avatarId = avatarId;
    }


//    Bitmap avatar;

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

    public int getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
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

/*    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public Bitmap getAvatar() {
        return avatar;
    }*/

    public static ArrayList<Person> getPersonList(){

        Person sermet = new Person("sermet","kerim","16.12.1998","sermet@mail.com",SignUpScreen.hashPassword("12345"), R.drawable.sermet);
        /*Person ali = new Person();
        Person leyla = new Person();
        Person berk = new Person();
        Person zeynep = new Person();
*/
        personList.add(sermet);
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
