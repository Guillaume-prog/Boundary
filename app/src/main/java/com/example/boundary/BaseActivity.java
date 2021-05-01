package com.example.boundary;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Guillaume ROUSSIN on 25/11/20
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;

    private String tag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(
                getString(R.string.preferences_key),
                Context.MODE_PRIVATE
        );
        editor = sharedPreferences.edit();

        tag = "";
    }

    protected void setTag(String tag) {
        this.tag = "-" + tag;
    }

    protected void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void toast(int messageId) {
        toast(getString(messageId));
    }

    protected void log(String message) {
        Log.d("LOGS" + tag, message);
    }
}
