package example.com.saveimage.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import example.com.saveimage.MainActivity;

public class ReadGalleryPermission implements IPermission {
    private int requestReadPermission;
    private View mLayout;
    private Activity activity;

    public ReadGalleryPermission(int requestReadPermission, View mLayout, Activity activity) {
        this.requestReadPermission = requestReadPermission;
        this.mLayout = mLayout;
        this.activity = activity;
    }

    @Override
    public void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Snackbar.make(mLayout, "Read access is required .",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            requestReadPermission);
                }
            }).show();

        } else {
            Snackbar.make(mLayout,
                    "Read Permission is not available..",
                    Snackbar.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    requestReadPermission);
        }
    }

    @Override
    public void showPreview() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(mLayout,
                    "Read permission is available.",
                    Snackbar.LENGTH_SHORT).show();
            actionAfterPermission();
        } else {
            requestPermission();
        }
    }

    @Override
    public void actionAfterPermission() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(galleryIntent, 1);
    }
}
