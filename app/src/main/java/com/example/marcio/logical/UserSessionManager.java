package com.example.marcio.logical;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by marcio on 12/10/16.
 */
public class UserSessionManager {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public UserSessionManager(Context ctx){
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setLoggedin(boolean loggedin, String user_name){
        editor.putBoolean("loggedInmode", loggedin);
        editor.putString("nome", user_name);
        editor.commit();
    }

    public boolean loggedin(){
        return prefs.getBoolean("loggedInmode", false);
    }

    public String getName(){
        return prefs.getString("nome", "");
    }
}
