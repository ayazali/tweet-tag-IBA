package com.ecs.android.sample.twitter;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Tweet;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChatterActivity extends Activity {

	TextView name, htag;
	EditText tweet_editText;
	Button tweet_htag;
	DbAdapter db;
	private SharedPreferences pref;
	private final Handler mTwitterHandler = new Handler();
	private final Handler mTweetHandler = new Handler();
	ListView myListView;
	ArrayList<String> arraylstTweets;
	List<Tweet> tweets;
	ArrayAdapter<String> aa;

	private final int MENU_ADD_EVENT = 0;
	private final int MENU_LOAD_TWEETS = 1;
	private final int MENU_HISTORY = 2;

	final Runnable mUpdateTwitterNotification = new Runnable() {
		public void run() {

			if (tweets.size() > 0) {
				for (Tweet tweet : tweets) {
					// System.out.println(tweet.getFromUser() + ":" +
					// tweet.getText());
					arraylstTweets.add(0,
							tweet.getFromUser() + ":" + tweet.getText());
				}
				aa.notifyDataSetChanged();
				Toast.makeText(getBaseContext(), "Searching Complete",
						Toast.LENGTH_LONG).show();
			} else {

				htag.setText("No Tweets Loaded");
			}

		}
	};

	final Runnable mTweetDone = new Runnable() {
		public void run() {
			Toast.makeText(getBaseContext(), "Tweeted !", Toast.LENGTH_LONG)
					.show();

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chatter);

		name = (TextView) findViewById(R.id.textViewChatter);
		htag = (TextView) findViewById(R.id.textViewTweets);
		myListView = (ListView) findViewById(R.id.listViewChatter);
		arraylstTweets = new ArrayList<String>();
		aa = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, arraylstTweets);
		myListView.setAdapter(aa);
		tweet_htag = (Button) findViewById(R.id.btn_tweet_htag);
		tweet_editText = (EditText) findViewById(R.id.editTextTweet_htag);

		this.pref = PreferenceManager.getDefaultSharedPreferences(this);

		searchTweet();

		tweet_htag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendTweet();
			}
		});

	}

	public void searchTweet() {
		Thread t = new Thread() {
			public void run() {

				try {
					tweets = TwitterUtils.search(pref, null);
					mTwitterHandler.post(mUpdateTwitterNotification);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		};
		t.start();
	}

	private String getTweetMsg() {

		return tweet_editText.getText().toString() + " # "
				+ pref.getString("Current_Hashtag", "");
	}

	public void sendTweet() {
		Thread t = new Thread() {
			public void run() {

				try {

					if (getTweetMsg().length() > 130)
						Toast.makeText(getBaseContext(),
								"Tweet Length must not exceed 130 chars",
								Toast.LENGTH_LONG);
					else {
						if (!getTweetMsg().equals(
								pref.getString("Current_tweet", ""))) {
							TwitterUtils.sendTweet(pref, getTweetMsg());
							setCurrentTweet(getTweetMsg());
							mTweetHandler.post(mTweetDone);
						} else {
							Toast.makeText(getBaseContext(),
									"You already Tweeted that !",
									Toast.LENGTH_LONG);
						}

					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		};
		t.start();
	}

	public void setCurrentTweet(String val) {

		SharedPreferences.Editor editor = pref.edit();
		editor.putString("Current_tweet", val);
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
