package gsoft.am.firebaselocatapptest1;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Avetik on 17/08/07.
 */

public class FirebaseDbHelper {
    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {

            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }
}
