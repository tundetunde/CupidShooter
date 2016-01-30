package com.dualdigital.cupidshooter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.appodeal.ads.Appodeal;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.dualdigital.cupidshooter.TheGame;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

public class AndroidLauncher extends AndroidApplication implements AdsController, ActivityMethods{
	LoginButton loginFB;
	Profile profile;
	CallbackManager callbackManager;
	private static final String FB_APP_ID = "650286768447115";
	private static final String BANNER_AD_UNIT_ID = "ca-app-pub-6044705985167929/2121637293";
	private static final String BANNER_TEST = "ca-app-pub-3940256099942544/6300978111";
	AccessToken accessToken;
	AdView adView;
	AdView bannerAd;
	RelativeLayout L1;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//initialize(new TheGame(this, this), config);
		//setupAds();
		FacebookSdk.sdkInitialize(getApplicationContext());
		setContentView(R.layout.menu_view);
		callbackManager = CallbackManager.Factory.create();
		printFBKeyHash();
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		View gameView = initializeForView(new TheGame(this, this), config);
		newLayout(gameView);
		initializeFBButton(callbackManager);

	}

	public void initAd(){
		adView = (AdView) findViewById(R.id.adView4);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
	}

	public void newLayout(View gameView){
		L1 = (RelativeLayout)findViewById(R.id.L1);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);

		L1.addView(gameView, params);
		initAd();
		initializeAppodeal();
		gameView.bringToFront();
		adView.bringToFront();
		//loginFB.bringToFront();
		//adView.bringToFront();
	}

	private void initializeFBButton(CallbackManager callbackManager){
		loginFB = (LoginButton) findViewById(R.id.login_button);
		//loginFB = new LoginButton(this);
		loginFB.bringToFront();
		//loginFB.setVisibility(View.INVISIBLE);
		loginFB.setReadPermissions("user_friends");
		// If using in a fragment
		//loginFB.setFragment(this);
		// Other app specific specialization

		// Callback registration
		loginFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				// App code

				profile = Profile.getCurrentProfile();
			}

			@Override
			public void onCancel() {
				// App code
			}

			@Override
			public void onError(FacebookException error) {

			}
		});
		ArrayList list = new ArrayList<>();
		list.add("publish_actions");
		LoginManager.getInstance().logInWithPublishPermissions(this, list);
		LoginManager.getInstance().registerCallback(callbackManager,
				new FacebookCallback<LoginResult>() {
					@Override
					public void onSuccess(LoginResult loginResult) {
						// App code
						loginResult.getAccessToken();
					}

					@Override
					public void onCancel() {
						// App code
					}

					@Override
					public void onError(FacebookException exception) {
						// App code
					}
				});
	}

	private void printFBKeyHash(){
		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"com.dualdigital.cupidshooter",
					PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (PackageManager.NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}
	}

	@Override
	public void shareScore() {
		String text = "#FallingPresents\nI'm collecting presents from Santa\n What about you?\n " +
				"Download from https://play.google.com/store/apps/details?id=com.dualdigital.cupidshooter";
		share("text/plain", text);
	}

	@Override
	public void shareScore(long score) {
		String text = "#FallingPresents\nI have collected " + score + " presents\nWhat about you?\n" +
				"Download from https://play.google.com/store/apps/details?id=com.dualdigital.cupidshooter";
		share("text/plain", text);
	}

	public void share(String type, String caption){

		// Create the new Intent using the 'Send' action.
		Intent share = new Intent(Intent.ACTION_SEND);

		// Set the MIME type
		share.setType(type);
		// Add the URI and the caption to the Intent.
		share.putExtra(Intent.EXTRA_TEXT, caption);

		// Broadcast the Intent.
		startActivity(Intent.createChooser(share, "Share to"));
	}

	@Override
	public void postFacebookScore(long score) {
		Bundle params = new Bundle();
		params.putString("score", Long.toString(score));
/* make the API call */
		profile = Profile.getCurrentProfile();
		new GraphRequest(
				AccessToken.getCurrentAccessToken(),
				"/" + profile.getId() + "/scores",
				params,
				HttpMethod.POST,
				new GraphRequest.Callback() {
					public void onCompleted(GraphResponse response) {
            /* handle the result */
						Log.d("Post Score", response.toString());
					}
				}
		).executeAsync();
	}

	@Override
	public boolean isLoggedInFB() {
		AccessToken accessToken = AccessToken.getCurrentAccessToken();
		return accessToken != null;
	}

	@Override
	 public void hideFbButton() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				loginFB.setVisibility(View.INVISIBLE);
			}
		});
	}

	@Override
	public void showFbButton() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				loginFB.setVisibility(View.VISIBLE);
			}
		});
	}

	@Override
	public ArrayList<HashMap<String, Integer>> postLeaderboard() {
		Leaderboard.leaderboardIDs = new ArrayList<>();
		final ArrayList<HashMap<String, Integer>> list = new ArrayList<>();
		new GraphRequest(
				AccessToken.getCurrentAccessToken(),
				"/" + FB_APP_ID + "/scores",
				null,
				HttpMethod.GET,
				new GraphRequest.Callback() {
					public void onCompleted(GraphResponse response) {
            /* handle the result */
						Log.d("LeaderBoard ting", response.toString());
						JSONObject j = response.getJSONObject();
						JSONArray jsonArray = null;
						try {
							jsonArray = j.getJSONArray("data");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						for (int i=0; i< jsonArray.length(); i++) {
							try {
								String name;
								JSONObject jsonobject = (JSONObject) jsonArray.get(i);
								JSONObject user = jsonobject.getJSONObject("user");
								final int score = jsonobject.optInt("score");
								String id = user.optString("id");
								name = user.optString("name");
								final String theName = name;
								list.add(
										new HashMap<String, Integer>(){{
											put(theName, score);
										}}
								);
								Leaderboard.leaderboardIDs.add(id);
								//is
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				}
		).executeAndWait();

		return list;
	}

	private void initializeAppodeal(){
		String appKey = "1ef9cc021fc4e5ee4feeb803aa6c87a1e658fadb1c9f3f75";
		Appodeal.initialize(this, appKey, Appodeal.BANNER);
		Appodeal.setTesting(true);
		Appodeal.setLogging(true);
		//Appodeal.show(AndroidLauncher.this, Appodeal.BANNER);
	}

	@Override
	public void startLeaderboardActivity() {
		Leaderboard.leaderboardArray = postLeaderboard();
		Intent i = new Intent(this, Leaderboard.class);
		startActivity(i);
	}

	@Override
	public void showBannerAd() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adView.setVisibility(View.VISIBLE);
				adView.bringToFront();
				Appodeal.show(AndroidLauncher.this, Appodeal.BANNER);

			}
		});
	}

	@Override
	public void hideBannerAd() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adView.setVisibility(View.INVISIBLE);
				Appodeal.hide(AndroidLauncher.this, Appodeal.BANNER);
			}
		});
	}

	@Override
	public boolean isWifiConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		return (ni != null && ni.isConnected());
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Logs 'install' and 'app activate' App Events.
		AppEventsLogger.activateApp(this);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Logs 'app deactivate' App Event.
		AppEventsLogger.deactivateApp(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}
}
