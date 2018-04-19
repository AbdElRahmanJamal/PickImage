package example.com.saveimage;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import example.com.saveimage.Retrofit.IApiService;
import example.com.saveimage.Retrofit.IApiServiceInterfaceCreation;

import example.com.saveimage.Retrofit.Results;
import example.com.saveimage.permission.CameraPermission;
import example.com.saveimage.permission.ReadGalleryPermission;
import example.com.saveimage.permission.RequestPermission;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private String encoded_stringBase64 = "";
    private IApiService mService;
    private ProgressDialog progressDialog;
    private View mLayout;
    private static final int PERMISSION_REQUEST_CAMERA = 0;
    private static final int PERMISSION_REQUEST_READ = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.linear);

        imageView = (ImageView) findViewById(R.id.imageView);
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        encoded_stringBase64 = bitMapToString(bitmap);
        mService = IApiServiceInterfaceCreation.apiService();
        progressDialog = new ProgressDialog(this);
    }

    public void gallery(View view) {
        new RequestPermission(new ReadGalleryPermission(PERMISSION_REQUEST_READ, mLayout, this));
    }

    public void uploadImage(View view) {
        progressDialog.setMessage("Loading. Please wait...");
        progressDialog.show();
        // saveDataUsingVolley();
        saveDataUsingRetrofit();

    }

    public void camera(View view) {
        new RequestPermission(new CameraPermission(PERMISSION_REQUEST_CAMERA, mLayout, this));
    }

    private void saveDataUsingVolley() {
        String url = "https://psychoapp.000webhostapp.com/saveImageToDB.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("onResponse: ", "onResponse : " + response);
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_LONG).show();
                        }
                        Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("onResponse: ", "error : " + error.getLocalizedMessage());
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_LONG).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("encoded_string", encoded_stringBase64);
                map.put("image_name", "Imajge8");
                return map;
            }
        };
        requestQueue.add(request);
    }


    public void saveDataUsingRetrofit() {
        mService.createTask(encoded_stringBase64, "ImageNameUnique1").enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, retrofit2.Response<Results> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(mLayout, "Camera permission was granted.",
                        Snackbar.LENGTH_SHORT)
                        .show();
                new RequestPermission(new CameraPermission(PERMISSION_REQUEST_CAMERA, mLayout, this));
            }
        } else if (requestCode == PERMISSION_REQUEST_READ) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(mLayout, "Read  permission was granted.",
                        Snackbar.LENGTH_SHORT)
                        .show();
                new RequestPermission(new ReadGalleryPermission(PERMISSION_REQUEST_READ, mLayout, this));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri contentURI = imageReturnedIntent.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        imageView.setImageBitmap(bitmap);
                        encoded_stringBase64 = bitMapToString(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case 0:
                if (resultCode == RESULT_OK) {
                    Bitmap thumbnail = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    imageView.setImageBitmap(thumbnail);
                    encoded_stringBase64 = bitMapToString(thumbnail);
                }
                break;
        }
    }

    public String bitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}
