package example.com.saveimage.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import example.com.saveimage.MainActivity;

public class CameraPermission implements IPermission {
    private int requestCameraPermission;
    private View mLayout;
    private Activity activity;

    public CameraPermission(int requestCameraPermission, View mLayout, Activity activity) {
        this.requestCameraPermission = requestCameraPermission;
        this.mLayout = mLayout;
        this.activity = activity;
    }

    @Override
    public void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.CAMERA)) {
            Snackbar.make(mLayout, "Camera access is required .",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.CAMERA},
                            requestCameraPermission);
                }
            }).show();

        } else {
            Snackbar.make(mLayout,
                    "Camera Permission is not available.",
                    Snackbar.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA},
                    requestCameraPermission);
        }
    }

    @Override
    public void showPreview() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(mLayout,
                    "Camera permission is available.",
                    Snackbar.LENGTH_SHORT).show();
            actionAfterPermission();
        } else {
            requestPermission();
        }
    }

    @Override
    public void actionAfterPermission() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, 0);
    }
}
