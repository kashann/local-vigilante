package com.project.kashan.localvigilante;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPetitionActivity extends AppCompatActivity {
    private final int MAPS_REQUEST_CODE = 9;
    private static final int PLACE_PICKER_REQUEST = 1000;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private GoogleApiClient mClient;
    Button b, s;
    EditText etName, etBody, etAddress;
    String tempLat, tempLong;
    private Bitmap mImageBitmap, img;
    private String mCurrentPhotoPath;
    private ImageButton ib;
    DatabaseHelper myDb;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_petition);
        myDb = new DatabaseHelper(this);

        mClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        b = findViewById(R.id.btnPlacePicker);
        s = findViewById(R.id.btnSubmit);
        etName = findViewById(R.id.etPetTitle);
        etBody = findViewById(R.id.etPetDescription);
        etAddress = findViewById(R.id.etAddress);
        ib = findViewById(R.id.imageButton);
        userId = getIntent().getIntExtra("userId", -1);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(AddPetitionActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = myDb.insertPetitionData(etName.getText().toString(),
                        etBody.getText().toString(), 1, etAddress.getText().toString(),
                        Float.parseFloat(tempLat), Float.parseFloat(tempLong), userId, getBytes(img));
                if(success)
                    Toast.makeText(getApplicationContext(), "Your petition has been sent!",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Petition couldn't be send!",Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    if (photoFile != null) {
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });
    }

    public void PlacePicker(View v){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(AddPetitionActivity.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void NavigateToMaps(View view){
        Intent intent = new Intent(AddPetitionActivity.this, MapsActivity.class);
        intent.putExtra("lat", tempLat);
        intent.putExtra("long", tempLong);
        intent.putExtra("address", etAddress.getText().toString());
        startActivityForResult(intent, MAPS_REQUEST_CODE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mClient.connect();
    }
    @Override
    protected void onStop() {
        mClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                String address = String.format("%s", place.getAddress());
                stBuilder.append("Name: ");
                stBuilder.append(placename);
                stBuilder.append("\n");
                stBuilder.append("Latitude: ");
                stBuilder.append(latitude);
                stBuilder.append("\n");
                stBuilder.append("Logitude: ");
                stBuilder.append(longitude);
                stBuilder.append("\n");
                stBuilder.append("Address: ");
                stBuilder.append(address);
                etAddress.setText(address);
                tempLat = latitude;
                tempLong = longitude;
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                Bitmap rotatedBitmap = Bitmap.createBitmap(mImageBitmap , 0, 0, mImageBitmap .getWidth(), mImageBitmap .getHeight(), matrix, true);
                ib.setImageBitmap(rotatedBitmap);
                img = rotatedBitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg", storageDir);

        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
        return stream.toByteArray();
    }
}
