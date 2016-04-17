package com.myapp.sg1907.contactgenerator;

import android.content.ContentProviderOperation;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button generate;
    private RadioButton small;
    private RadioButton medium;
    private RadioButton large;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle("CONTACT GENERATOR");

        generate = (Button) findViewById(R.id.generate);
        small = (RadioButton) findViewById(R.id.small);
        medium = (RadioButton) findViewById(R.id.medium);
        large = (RadioButton) findViewById(R.id.large);
    }

    public void generateContact(View view){
        int contactNumber = 0;
        Random r = new Random();
        String x0, x1, x2, x3, x4;
        String[] names = {"Deniz", "Vural", "Yılmaz", "Kemal", "Can", "Su", "Cennet", "Nur", "İpek", "Mert",
                "Bahtiyar", "Mesut", "Salih", "Serdar", "Şevkat", "Doğan", "Şahin", "Kartal", "Cesur", "Toprak"};

        int[] icons = {R.mipmap.ic_1, R.mipmap.ic_2, R.mipmap.ic_3, R.mipmap.ic_4, R.mipmap.ic_5,
                            R.mipmap.ic_6, R.mipmap.ic_7, R.mipmap.ic_8, R.mipmap.ic_9, R.mipmap.ic_10,
                                R.mipmap.ic_11, R.mipmap.ic_12, R.mipmap.ic_13, R.mipmap.ic_14, R.mipmap.ic_15,
                                    R.mipmap.ic_16, R.mipmap.ic_17, R.mipmap.ic_18, R.mipmap.ic_19, R.mipmap.ic_20};

        if(small.isChecked()){
            contactNumber = 50;
        } else if(medium.isChecked()){
            contactNumber = 100;
        } else if(large.isChecked()){
            contactNumber = 150;
        } else{
            contactNumber = 50;
        }

        x0 = "0";
        x1 = "5";

        Toast.makeText(this, "Serdar"/*Integer.toString(contactNumber)*/, Toast.LENGTH_SHORT).show();

        for (int i = 0; i < contactNumber; i++) {
            int ic_id = icons[r.nextInt(20)];

            Resources resources = getBaseContext().getResources();
            Bitmap bmpPhoto = BitmapFactory.decodeResource(resources, ic_id);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmpPhoto.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            byte[] bytePhoto = stream.toByteArray();

            String name = names[r.nextInt(20)];
            String surname = names[r.nextInt(20)];

            x2 = Integer.toString(r.nextInt(3) + 3);

            if (x2 == "3") {
                x3 = Integer.toString(r.nextInt(7) + 2);
            } else {
                x3 = Integer.toString(r.nextInt(4) + 2);
            }

            x4 = Integer.toString(r.nextInt(10000000));

            String displayName = name + " " + surname.toUpperCase();
            String mobileNumber = x0 + x1 + x2 + x3 + x4;

            this.addContact(displayName, mobileNumber, bytePhoto);
        }
    }

    public void addContact(String displayName, String mobileNumber, byte[] bytePhoto){
        ArrayList< ContentProviderOperation > cpo = new ArrayList < ContentProviderOperation > ();

        cpo.add(ContentProviderOperation
                .newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        if (displayName != null) {
            cpo.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, displayName)
                    .build());
        }

        if (mobileNumber != null) {
            cpo.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobileNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }

        cpo.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Photo.DATA15, bytePhoto)
                .build());

        try { getContentResolver().applyBatch(ContactsContract.AUTHORITY, cpo); }
        catch (Exception e) { e.printStackTrace(); }
    }
}
