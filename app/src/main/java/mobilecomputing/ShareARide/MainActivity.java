package mobilecomputing.ShareARide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserLocation.setContext(getApplicationContext());
        Intent newIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(newIntent);
    }



/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public final class FeedReaderContract {
        // To prevent someone from accidentally instantiating the contract class,
        // give it an empty constructor.
        public FeedReaderContract() {}

        /* Inner class that defines the table contents */
       /* public abstract class FeedEntry implements BaseColumns {
            public static final String TABLE_NAME = "usertable";
            //public static final String COLUMN_NAME_NULLABLE = null;
            public static final String COLUMN_NAME_NAME = "Name";
            public static final String COLUMN_NAME_EMAIL = "Email";
            public static final String COLUMN_NAME_PHONE = "Phone";
            public static final String COLUMN_NAME_ADDRESS = "Address";
            private static final String TEXT_TYPE = " TEXT";
            private static final String COMMA_SEP = ",";
            private static final String SQL_CREATE_ENTRIES =
                    "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                            FeedEntry._ID + " INTEGER PRIMARY KEY," +
                            FeedEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                            FeedEntry.COLUMN_NAME_EMAIL + TEXT_TYPE + COMMA_SEP +
                            FeedEntry.COLUMN_NAME_PHONE + TEXT_TYPE + COMMA_SEP +
                            FeedEntry.COLUMN_NAME_ADDRESS + TEXT_TYPE + COMMA_SEP +" )";

            private static final String SQL_DELETE_ENTRIES =
                    "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
        }

        public class FeedReaderDbHelper extends SQLiteOpenHelper {
            // If you change the database schema, you must increment the database version.
            public static final int DATABASE_VERSION = 1;
            public static final String DATABASE_NAME = "FeedReader.db";

            public FeedReaderDbHelper(Context context) {
                super(context, DATABASE_NAME, null, DATABASE_VERSION);
            }
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(MainActivity.FeedReaderContract.FeedEntry.SQL_CREATE_ENTRIES);
            }
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                // This database is only a cache for online data, so its upgrade policy is
                // to simply to discard the data and start over
                db.execSQL(FeedEntry.SQL_DELETE_ENTRIES);
                onCreate(db);
            }
            public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                onUpgrade(db, oldVersion, newVersion);
            }
        }

    }*/
}


