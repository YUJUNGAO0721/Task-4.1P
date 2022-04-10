package com.yujun.timerapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    int PRIVATE_MODE = 0;
    SharedPreferences pref;

    public SharedPreferences.Editor editor;
    public String SHARED_PREF_NAME = "db_pref";

    Context _context;

    /**
     * Start Declarations
     */
    public String PREVIOUS_STUDY_HOUR = "previousStudyHour";
    public String PREVIOUS_STUDY_UNIT = "previousStudyUnit";

    /**
     * Init Constructor
     */
    public SharedPrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(SHARED_PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * start getters and setters block
     */
    public String getPREVIOUS_STUDY_HOUR() {
        if (pref.getString(PREVIOUS_STUDY_HOUR, null) != null)
            return pref.getString(PREVIOUS_STUDY_HOUR, null);
        else
            return "00:00";
    }

    public void setPREVIOUS_STUDY_HOUR(String previousStudyHour) {
        editor.putString(PREVIOUS_STUDY_HOUR, previousStudyHour);
        editor.apply();
    }

    public String getPREVIOUS_STUDY_UNIT() {
        if (pref.getString(PREVIOUS_STUDY_UNIT, null) != null)
            return pref.getString(PREVIOUS_STUDY_UNIT, null);
        else
            return "xyz";
    }

    public void setPREVIOUS_STUDY_UNIT(String previousStudyUnit) {
        editor.putString(PREVIOUS_STUDY_UNIT, previousStudyUnit);
        editor.apply();
    }
}
