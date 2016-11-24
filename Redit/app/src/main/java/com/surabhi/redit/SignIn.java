package com.surabhi.redit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Surabhi Agrawal on 11/18/2016.
 */
public class SignIn extends Activity{
    String LOG_TAG = "SignIN";
    private static final String AUTH_URL =
            "https://www.reddit.com/api/v1/authorize.compact?client_id=%s" +
                    "&response_type=code&state=%s&redirect_uri=%s&" +
                    "duration=permanent&scope=identity";

    private static final String CLIENT_ID = "jL0bUk9sf6HFfg";

    private static final String REDIRECT_URI =
            "https://www.reddit.com/r/AskReddit/.json";

    private static final String STATE = "MY_RANDOM_STRING_1";

    private static final String ACCESS_TOKEN_URL =
            "https://www.reddit.com/api/v1/access_token";

    public static final int SIGN_IN = 1;
    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.sign_in);
        Button button = (Button) findViewById(R.id.signin);

//        View view = (View) findViewById(R.layout.sign_in);
//        startSignIn();

    }

    public void startSignIn(View view) {
        String url = String.format(AUTH_URL, CLIENT_ID, STATE, REDIRECT_URI);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

//    public void startSign(View view)
//    {
//    String url = String.format(AUTH_URL, CLIENT_ID, STATE, REDIRECT_URI);
//    Intent intent = new Intent(this,MyBrowser.class);
//    intent.putExtra("url",url);
//        startActivityForResult(intent,SIGN_IN);
//    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent()!=null && getIntent().getAction().equals(Intent.ACTION_VIEW)) {
            Uri uri = getIntent().getData();
            if(uri.getQueryParameter("error") != null) {
                String error = uri.getQueryParameter("error");
                Log.e("SignIN:", "An error has occurred : " + error);
            } else {
                String state = uri.getQueryParameter("state");
                if(state.equals(STATE)) {
                    String code = uri.getQueryParameter("code");
//                    Intent intent = new Intent();
//                    intent.putExtra("State",state);
//                    setResult(Activity.RESULT_OK,intent);
//                    finish();
                    getAccessToken(code);
                }
            }
        }
    }

    private void getAccessToken(String code) {
        OkHttpClient client = new OkHttpClient();
        String authString = CLIENT_ID + ":";
        String encodedAuthString = Base64.encodeToString(authString.getBytes(),
                Base64.NO_WRAP);
        Request request = new Request.Builder()
                .addHeader("User-Agent", "Sample App")
                .addHeader("Authorization", "Basic " + encodedAuthString)
                .url(ACCESS_TOKEN_URL)
                .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),
                        "grant_type=authorization_code&code=" + code +
                                "&redirect_uri=" + REDIRECT_URI
                ))
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(LOG_TAG, "ERROR: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();

                JSONObject data = null;
                try {
                    data = new JSONObject(json);
                    String accessToken = data.optString("access_token");
                    String refreshToken = data.optString("refresh_token");
                    Log.d(LOG_TAG, "Access Token = " + accessToken);
                    Log.d(LOG_TAG, "Refresh Token = " + refreshToken);
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("accessToken",accessToken);
                    editor.putString("refreshToken", refreshToken);
                    editor.commit();
                    Intent intent= new  Intent(SignIn.this,MyActivity.class);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }



}
