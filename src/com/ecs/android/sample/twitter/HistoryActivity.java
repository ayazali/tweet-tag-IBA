package com.ecs.android.sample.twitter;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class HistoryActivity extends Activity {

	ListView historyList;
	ArrayList<String> historystrlist;
	ArrayAdapter<String> history_arrayAdapter;
	Cursor cursor_history;
	private final Handler historyHandler = new Handler();
	String[][] historyRecords;
	SharedPreferences prefs;

	private final int MENU_ADD_EVENT = 0;
	private final int MENU_LOAD_TWEETS = 1;
	private final int MENU_HISTORY = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		historyList = (ListView) findViewById(R.id.listViewHistory);
		historystrlist = new ArrayList<String>();
		history_arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, historystrlist);
		historyList.setAdapter(history_arrayAdapter);

		loadHistory();

		historyList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(getBaseContext(),
						"Loading Tweets for this hasgtag", Toast.LENGTH_SHORT)
						.show();
				setCurrent(historyRecords[position][0]);
				Intent launch = new Intent(getBaseContext(),
						ChatterActivity.class);
				startActivity(launch);

			}

		});

	}

	final Runnable ondatabaseHistory = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			history_arrayAdapter.notifyDataSetChanged();
		}
	};

	public void loadHistory() {
		Thread t = new Thread() {
			public void run() {

				DbAdapter db = new DbAdapter(getApplicationContext());
				db.open();
				cursor_history = db.getHistory();
				historyRecords = new String[cursor_history.getCount()][3];
				cursor_history.moveToFirst();
				int count = 0;
				if (cursor_history.moveToFirst()) {
					do {

						historyRecords[count][0] = cursor_history.getString(0);
						historyRecords[count][0] = cursor_history.getString(1);
						historyRecords[count][0] = cursor_history.getString(2);
						count++;

					} while (cursor_history.moveToNext());
				}

				for (int i = 0; i < historyRecords.length; i++) {

					historystrlist.add(historyRecords[i][0]);
				}
				historyHandler.post(ondatabaseHistory);

			}

		};
		t.start();
	}

	public void setCurrent(String val) {

		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("Current_Hashtag", val);
		editor.commit();

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_ADD_EVENT, 0, "Add Event");
		menu.add(0, MENU_LOAD_TWEETS, 1, "Load Chatter");
		menu.add(0, MENU_HISTORY, 2, "History");

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ADD_EVENT:
			Intent i = new Intent(this, NewEventActivity.class);
			startActivity(i);
			return true;
		case MENU_LOAD_TWEETS:
			Intent ii = new Intent(this, ChatterActivity.class);
			startActivity(ii);
			return true;
		case MENU_HISTORY:
			Intent hist = new Intent(this, HistoryActivity.class);
			startActivity(hist);
			return true;
		}
		return false;
	}
}
