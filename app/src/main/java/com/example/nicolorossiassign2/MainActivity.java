package com.example.nicolorossiassign2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.MailTo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static String emailTo;
    private static String emailContent;
    private static String emailSubject;
    ActivityResultLauncher<Intent> getMessage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    emailTo = result.getData().getStringExtra(EmailComposerActivity.MESSAGE_TO);
                    emailContent = result.getData().getStringExtra(EmailComposerActivity.MESSAGE_CONTENT);
                    emailSubject = result.getData().getStringExtra(EmailComposerActivity.MESSAGE_SUBJECT);
                    String emailComplete= String.format("To: %s\nSubject: %s\nContent: %s\n", emailTo, emailSubject, emailContent);
                    if(result != null
                            && emailTo != null
                            &&  emailContent != null
                            && emailSubject != null){
                        TextView Email = (TextView) findViewById(R.id.textView4);
                        Email.setText(emailComplete);
                        Button button = (Button) findViewById(R.id.buttonSend);
                        button.setEnabled(true);

                    }
                }
            }
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void implicitIntentCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivity(intent);
    }

    public void implicitIntentGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivity(intent);
    }

    public void explicitIntentComposer(View view) {
        Intent intent = new Intent(this, EmailComposerActivity.class);

        getMessage.launch(intent);
    }


    public void sendEmail(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:" + emailTo + "?subject=" + emailSubject + "&body=" + emailContent);

        intent.setData(data);
        startActivity(intent);

    }
}