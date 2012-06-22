package org.cnx.flashcards;

import static org.cnx.flashcards.Constants.*;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;

public class ModeSelectActivity extends SherlockActivity {

    Button quizModeButton;
    Button studyModeButton;
    Button selfTestModeButton;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modeselect);

        final String id = getIntent().getStringExtra(DECK_ID);

        quizModeButton = (Button) findViewById(R.id.quizModeButton);
        studyModeButton = (Button) findViewById(R.id.studyModeButton);
        selfTestModeButton = (Button) findViewById(R.id.selfTestModeButton);

        studyModeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cardIntent = new Intent(getApplicationContext(),
                        StudyCardActivity.class);
                cardIntent.putExtra(DECK_ID, id);
                startActivity(cardIntent);
            }
        });

        selfTestModeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cardIntent = new Intent(getApplicationContext(),
                        SelfTestCardActivity.class);
                cardIntent.putExtra(DECK_ID, id);
                startActivity(cardIntent);
            }
        });

        quizModeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cardIntent = new Intent(getApplicationContext(),
                        QuizCardActivity.class);
                cardIntent.putExtra(DECK_ID, id);
                startActivityForResult(cardIntent, QUIZ_LAUNCH);
            }
        });
    }

    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Only dealing with invalid decks right now
        if (resultCode != RESULT_INVALID_DECK)
            return;
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        
        switch(requestCode) {
        case QUIZ_LAUNCH:
            builder.setMessage("This deck has too few cards. You need at least 3 for a quiz.");
            break;
            
        case SELF_TEST_LAUNCH:
            builder.setMessage("This deck doesn't have any cards!");
            break;
            
        case STUDY_LAUNCH:
            builder.setMessage("This deck doesn't have any cards!");
            break;
        }
        
        AlertDialog quizAlert = builder.create();
        quizAlert.show();
        
        super.onActivityResult(requestCode, resultCode, data);
    }
}
