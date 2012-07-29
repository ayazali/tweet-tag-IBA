package com.ecs.android.sample.twitter;

import java.util.List;

import oauth.signpost.OAuth;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import android.content.SharedPreferences;

public class TwitterUtils {

	public static boolean isAuthenticated(SharedPreferences prefs) {

		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");

		AccessToken a = new AccessToken(token, secret);
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(Constants.CONSUMER_KEY,
				Constants.CONSUMER_SECRET);
		twitter.setOAuthAccessToken(a);

		try {
			twitter.getAccountSettings();
			return true;
		} catch (TwitterException e) {
			return false;
		}
	}

	public static void sendTweet(SharedPreferences prefs, String msg)
			throws Exception {
		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");

		AccessToken a = new AccessToken(token, secret);
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(Constants.CONSUMER_KEY,
				Constants.CONSUMER_SECRET);
		twitter.setOAuthAccessToken(a);
		twitter.updateStatus(msg);
	}

	public static List<Tweet> search(SharedPreferences prefs, String msg)
			throws Exception {

		// String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		// String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		//
		// AccessToken a = new AccessToken(token, secret);
		// twitter.setOAuthConsumer(Constants.CONSUMER_KEY,
		// Constants.CONSUMER_SECRET);
		// twitter.setOAuthAccessToken(a);
		Twitter twitter = new TwitterFactory().getInstance();
		String tmp = prefs.getString("Current_Hashtag",
				"Error pref doesnt exist");
		Query query = new Query("#" + tmp);
		QueryResult result = twitter.search(query);
		List<Tweet> tweets = result.getTweets();

		return tweets;
	}
}
