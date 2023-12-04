package com.example.nicolorossiassign2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static String emailTo;
    private static String emailContent;
    private static String emailSubject;

    // Creating activity result launcher. It will call a secondary activity to return some data from there
    ActivityResultLauncher<Intent> getMessage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result == null) {
                        Log.e("onActivityResult", "The result returned from activity was null");
                        return;
                    }

                    // Extracting data from result of activity and assign it to static properties
                    setAllEmailProperties(result);

                    //Check if all values are valid and log errors
                    if(areAllPropertiesValid(emailTo, emailContent, emailSubject)){

                        // Writing email in TextView and enabling the button
                        TextView Email = (TextView) findViewById(R.id.textView4);
                        Email.setText(composeEmail(emailTo, emailSubject, emailContent));
                        Button button = (Button) findViewById(R.id.buttonSend);
                        button.setEnabled(true);

                    }
                    else {
                        //Display message if data is not valid
                        TextView Email = (TextView) findViewById(R.id.textView4);
                        Email.setText(getResources().getString(R.string.error_data_invalid));
                        Log.e("onActivityResult", "Some values are missing from email");
                    }
                }
            }
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // This method will call the camera of the device
    public void implicitIntentCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);
    }

    // This method will call the image gallery and accept any format of image
    public void implicitIntentGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivity(intent);
    }

    // this method will call a secondary activity
    public void explicitIntentComposer(View view) {
        Intent intent = new Intent(this, EmailComposerActivity.class);

        getMessage.launch(intent);
    }

    // this method will open the default email app and compose the message
    public void implicitIntentDefaultEmail(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:" + emailTo + "?subject=" + emailSubject + "&body=" + emailContent);

        intent.setData(data);
        startActivity(intent);

    }

    // unifying all setters
    private static void setAllEmailProperties(ActivityResult result) {
        setEmailTo(result);
        setEmailContent(result);
        setEmailSubject(result);
    }
    private static void setEmailTo(ActivityResult result){
        emailTo = result.getData().getStringExtra(EmailComposerActivity.MESSAGE_TO);
    }
    private static void setEmailContent(ActivityResult result){
        emailContent = result.getData().getStringExtra(EmailComposerActivity.MESSAGE_CONTENT);
    }
    private static void setEmailSubject(ActivityResult result){
        emailSubject = result.getData().getStringExtra(EmailComposerActivity.MESSAGE_SUBJECT);
    }

    // Creating email as single string
    public static String composeEmail(String emailTo, String emailSubject, String emailContent) {
       return String.format("To: %s\nSubject: %s\nContent: %s\n", emailTo, emailSubject, emailContent);
    }

    // check if all fields are valid and log debug messages if necessary
    private static boolean areAllPropertiesValid(String emailTo, String emailContent, String emailSubject){
        boolean isValid = true;
        if(emailTo.isEmpty()) {
          Log.d("onActivityResult", "Email TO is empty.");
          isValid = false;
        }
        if(emailContent.isEmpty()) {
            Log.d("onActivityResult", "Email CONTENT is empty.");
            isValid = false;
        }
        if(emailSubject.isEmpty()) {
            Log.d("onActivityResult", "Email SUBJECT is empty.");
            isValid = false;
        }

        return isValid;
    }
}