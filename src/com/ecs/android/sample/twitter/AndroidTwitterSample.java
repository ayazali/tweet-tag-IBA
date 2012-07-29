package com.ecs.android.sample.twitter;

import java.util.Date;

import oauth.signpost.OAuth;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidTwitterSample extends Activity {

	private SharedPreferences prefs;
	private final Handler mTwitterHandler = new Handler();
	private TextView loginStatus;
	private final int MENU_ADD_EVENT = 0;
	private final int MENU_LOAD_TWEETS = 1;
	private final int MENU_HISTORY = 2;

	final Runnable mUpdateTwitterNotification = new Runnable() {
		public void run() {
			Toast.makeText(getBaseContext(), "Tweet sent !", Toast.LENGTH_LONG)
					.show();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);

		loginStatus = (TextView) findViewById(R.id.login_status);
		ImageButton login = (ImageButton) findViewById(R.id.btn_login);
		Button clearCredentials = (Button) findViewById(R.id.btn_clear_credentials);

		login.setOnClickListener(new View.OnClickListener() {
			/**
			 * Send a tweet. If the user hasn't authenticated to Tweeter yet,
			 * he'll be redirected via a browser to the twitter login page. Once
			 * the user authenticated, he'll authorize the Android application
			 * to send tweets on the users behalf.
			 */
			public void onClick(View v) {
				if (TwitterUtils.isAuthenticated(prefs)) {
					Intent i = new Intent(getApplicationContext(),
							NewEventActivity.class);
					startActivity(i);

				} else {
					Intent i = new Intent(getApplicationContext(),
							PrepareRequestTokenActivity.class);
					i.putExtra("tweet_msg", getTweetMsg());
					startActivity(i);
				}
			}
		});

		clearCredentials.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				clearCredentials();
				updateLoginStatus();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateLoginStatus();
	}

	public void updateLoginStatus() {
		loginStatus.setText("Logged into Twitter : "
				+ TwitterUtils.isAuthenticated(prefs));
	}

	private String getTweetMsg() {
		return "Tweeting from Android App at " + new Date().toLocaleString();
	}

	public void sendTweet() {
		Thread t = new Thread() {
			public void run() {

				try {
					TwitterUtils.sendTweet(prefs, getTweetMsg());
					mTwitterHandler.post(mUpdateTwitterNotification);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		};
		t.start();
	}

	private void clearCredentials() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		final Editor edit = prefs.edit();
		edit.remove(OAuth.OAUTH_TOKEN);
		edit.remove(OAuth.OAUTH_TOKEN_SECRET);
		edit.commit();
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