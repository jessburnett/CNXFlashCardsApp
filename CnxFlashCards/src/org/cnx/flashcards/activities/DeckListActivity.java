package org.cnx.flashcards.activities;

import static org.cnx.flashcards.Constants.*;
import static org.cnx.flashcards.Constants.TITLE;
import static org.cnx.flashcards.Constants.RESULT_DECK_DELETED;

import org.cnx.flashcards.R;
import org.cnx.flashcards.database.DeckProvider;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;

public class DeckListActivity extends SherlockActivity {
    
    ListView deckListView;
    Cursor titlesCursor;
    Button newDeckButton;
    
    static int DECK_INFO_REQUEST = 0;
    static int NEW_DECK_REQUEST = 1;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decklist);
        
        // Get UI elements
        deckListView = (ListView)findViewById(R.id.deckListView);
        newDeckButton = (Button)findViewById(R.id.newDeckButton);
        
        // Retrieve decks from the database, show in list
        getDecks();
        
        deckListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long resource_id) {
                // Get the id of the selected deck
                titlesCursor.moveToPosition(position);
                String id = titlesCursor.getString(titlesCursor.getColumnIndex(BaseColumns._ID));
                
                // Launch the deck
                Intent cardIntent = new Intent(getApplicationContext(), DownloadedDeckInfoActivity.class);
                cardIntent.putExtra(DECK_ID, id);
                startActivityForResult(cardIntent, DECK_INFO_REQUEST);
            }
        });
        
        newDeckButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent editIntent = new Intent(DeckListActivity.this, DeckEditorActivity.class);
				editIntent.putExtra(NEW_DECK, true);
				startActivityForResult(editIntent, NEW_DECK_REQUEST);
			}
		});
    }

    
    /**Extracts decks from the database, shows them in the ListView**/
    private void getDecks() {
        String[] projection = {BaseColumns._ID, MODULE_ID, TITLE };
        titlesCursor = getContentResolver().query(
                DeckProvider.CONTENT_URI, projection, null, null, null);
        titlesCursor.moveToFirst();
        
        int[] to = {R.id.url, R.id.title};
        String[] from = {MODULE_ID, TITLE };
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.search_row, titlesCursor, from, to, CursorAdapter.NO_SELECTION);

        deckListView.setAdapter(cursorAdapter);
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == DECK_INFO_REQUEST || requestCode == NEW_DECK_REQUEST) {
    		getDecks();
    	}

    	super.onActivityResult(requestCode, resultCode, data);
    }
    
    
    @Override
    public void finish() {
        titlesCursor.close();
        super.finish();
    }
}
