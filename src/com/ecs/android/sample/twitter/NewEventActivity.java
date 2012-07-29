package com.ecs.android.sample.twitter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NewEventActivity extends Activity implements OnClickListener {

	TextView name, htag;
	Button addNew;
	DbAdapter db;
	SharedPreferences pref;

	private final int MENU_ADD_EVENT = 0;
	private final int MENU_LOAD_TWEETS = 1;
	private final int MENU_HISTORY = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.joinevent);

		name = (TextView) findViewById(R.id.editTextNewEvent);
		htag = (TextView) findViewById(R.id.editTextHashTag);
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		db = new DbAdapter(this);
		db.open();

		addNew = (Button) findViewById(R.id.buttonAddnew);
		addNew.setOnClickListener(this);

		// Log.d("New Activity", "Started Activity New Event");
		// Toast.makeText(getApplicationContext(), "Activity Launched",
		// Toast.LENGTH_LONG).show();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.buttonAddnew:

			if (name.getText().toString().length() == 0
					|| htag.getText().toString().length() == 0) {
				Toast.makeText(getApplicationContext(),
						"Please fill all fields", Toast.LENGTH_LONG).show();

			} else {
				db.insertEntry(name.getText().toString(), htag.getText()
						.toString());
				setCurrent(htag.getText().toString());

				Toast.makeText(
						getApplicationContext(),
						"All Done Current Htag is "
								+ pref.getString("Current_Hashtag",
										"Error pref doesnt exist"),
						Toast.LENGTH_LONG).show();

				Intent i = new Intent(getApplicationContext(),
						ChatterActivity.class);
				startActivity(i);
			}
			break;
		}

	}

	public void setCurrent(String val) {

		SharedPreferences.Editor editor = pref.edit();
		editor.putString("Current_Hashtag", htag.getText().toString());
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
