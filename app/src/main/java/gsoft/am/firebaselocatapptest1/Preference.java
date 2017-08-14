package gsoft.am.firebaselocatapptest1;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preference {

    private static final String PREF_KID_ID = "PREF_KID_ID";

    private static Preference sInstance;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private Preference(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mSharedPreferences.edit();
    }

    public static Preference getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new Preference(context);
        }
        return sInstance;
    }

    public void setKidId(String kidId) {
        mEditor.putString(PREF_KID_ID, kidId);
        mEditor.apply();
    }

    public String getKidId() {
        return mSharedPreferences.getString(PREF_KID_ID, null);
    }

}