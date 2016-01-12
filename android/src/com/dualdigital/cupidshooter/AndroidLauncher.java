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
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

public class AndroidLauncher extends AndroidApplication implements AdsController, ActivityMethods{
	LoginButton loginFB;
	Profile profile;
	CallbackManager callbackManager;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new TheGame(), config);
		FacebookSdk.sdkInitialize(getApplicationContext());
		callbackManager = CallbackManager.Factory.create();
		initializeFBButton(callbackManager);
		printFBKeyHash();
	}

	private void initializeFBButton(CallbackManager callbackManager){
		loginFB = new LoginButton(this);
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
		ArrayList list = new ArrayList<String>();
		list.add("publish_actions");
		LoginManager.getInstance().logInWithPublishPermissions(this, list);
		LoginManager.getInstance().registerCallback(callbackManager,
				new FacebookCallback<LoginResult>() {
					@Override
					public void onSuccess(LoginResult loginResult) {
						// App code
						//loginResult.getAccessToken();
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
					"com.dualtech.fallingpresents.android",
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
				"Download from https://play.google.com/store/apps/details?id=com.dualtech.fallingpresents.android";
		share("text/plain", text);
	}

	@Override
	public void shareScore(long score) {
		String text = "#FallingPresents\nI have collected " + score + " presents\nWhat about you?\n" +
				"Download from https://play.google.com/store/apps/details?id=com.dualtech.fallingpresents.android";
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
		loginFB.setVisibility(View.VISIBLE);
	}

	@Override
	public ArrayList<HashMap<String, Integer>> postLeaderboard() {
		return null;
	}

	@Override
	public void showBannerAd() {
		/*runOnUiThread(new Runnable() {
			@Override
			public void run() {
				bannerAd.setVisibility(View.VISIBLE);
				AdRequest.Builder builder = new AdRequest.Builder();
				AdRequest ad = builder.build();
				bannerAd.loadAd(ad);
			}
		});*/
	}

	@Override
	public void hideBannerAd() {
		/*runOnUiThread(new Runnable() {
			@Override
			public void run() {
				bannerAd.setVisibility(View.INVISIBLE);
			}
		});	*/
	}

	@Override
	public boolean isWifiConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		return (ni != null && ni.isConnected());
	}
}
