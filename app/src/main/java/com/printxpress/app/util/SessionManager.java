package com.printxpress.app.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.printxpress.app.model.User;

/** Lightweight session manager that remembers the logged-in user across activities. */
public class SessionManager {
    private static final String PREF = "printxpress_session";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ROLE = "role";
    private static final String KEY_ADDRESS = "address";

    private final SharedPreferences prefs;

    public SessionManager(Context ctx) {
        prefs = ctx.getApplicationContext().getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public void saveSession(User user) {
        prefs.edit()
            .putLong(KEY_USER_ID, user.getId())
            .putString(KEY_NAME, user.getName())
            .putString(KEY_EMAIL, user.getEmail())
            .putString(KEY_PHONE, user.getPhone())
            .putString(KEY_ROLE, user.getRole())
            .putString(KEY_ADDRESS, user.getAddress())
            .apply();
    }

    public boolean isLoggedIn() {
        return prefs.getLong(KEY_USER_ID, -1) > 0;
    }

    public long getUserId() { return prefs.getLong(KEY_USER_ID, -1); }
    public String getName() { return prefs.getString(KEY_NAME, ""); }
    public String getEmail() { return prefs.getString(KEY_EMAIL, ""); }
    public String getPhone() { return prefs.getString(KEY_PHONE, ""); }
    public String getRole() { return prefs.getString(KEY_ROLE, ""); }
    public String getAddress() { return prefs.getString(KEY_ADDRESS, ""); }

    public boolean isAdmin() { return User.ROLE_ADMIN.equals(getRole()); }

    public void clear() {
        prefs.edit().clear().apply();
    }
}
