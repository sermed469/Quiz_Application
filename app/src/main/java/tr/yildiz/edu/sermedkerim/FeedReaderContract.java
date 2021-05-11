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
}
