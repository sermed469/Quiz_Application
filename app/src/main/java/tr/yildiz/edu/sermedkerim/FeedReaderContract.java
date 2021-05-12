package tr.yildiz.edu.sermedkerim;

import android.provider.BaseColumns;

public final class FeedReaderContract {

    private FeedReaderContract() {}

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "Person";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SURNAME = "surname";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_DATEOFBIRTH = "dateofbirth";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_AVATAR = "avatar";
    }

    public static class FeedEntry2 implements BaseColumns {
        public static final String TABLE_NAME = "Question";
        public static final String COLUMN_NAME_QUESTION = "question";
        public static final String COLUMN_NAME_CHOICEONE = "choiceone";
        public static final String COLUMN_NAME_CHOICETWO = "choicetwo";
        public static final String COLUMN_NAME_CHOICETHREE = "choicethree";
        public static final String COLUMN_NAME_CHOICEFOUR = "choicefour";
        public static final String COLUMN_NAME_CHOICEFIVE = "choicefive";
        public static final String COLUMN_NAME_ANSWER = "answer";
        public static final String COLUMN_NAME_ATTACHMENT = "attachment";
        public static final String COLUMN_NAME_ATTACHMENTTYPE = "attachmenttype";
        public static final String COLUMN_NAME_PERSONEMAIL = "personid";
    }
}
