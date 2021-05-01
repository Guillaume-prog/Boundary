package com.example.boundary;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.internal.ManufacturerUtils;

/**
 * Created by Guillaume ROUSSIN on 12/08/20
 */
@RequiresApi(api = Build.VERSION_CODES.Q)
public class Permission {

    public static final String TAG = "PermissionChecker";

    // Permission List
    private static int requestCode = 1234;

    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
    };

    /* Permission checking */

    public static boolean requestPermissions(AppCompatActivity act) {
        // Check the permissions
        boolean permissionsGranted = true;

        for(String perm : PERMISSIONS) {
            Log.d(TAG, "permissionCheck: " + perm + " - " + checkPermission(act, perm));
            if(!checkPermission(act, perm)) permissionsGranted = false;
        }

        if(!permissionsGranted) act.requestPermissions(PERMISSIONS, requestCode);

        return permissionsGranted;
    }

    private static boolean checkPermission(AppCompatActivity act, String perm) {
        return (ActivityCompat.checkSelfPermission(act, perm) == PackageManager.PERMISSION_GRANTED);
    }

    /* Async check of returned permissions */

    public static boolean accepted(int code, int results[]) {
        if(results.length == 0 || code != requestCode)
            return false;

        for(int res : results)
            if(res != PackageManager.PERMISSION_GRANTED) return false;

        return true;
    }

    public static void statusCheck(Activity act) {
        final LocationManager manager = (LocationManager) act.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(act);
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        act.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        dialog.cancel();
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
