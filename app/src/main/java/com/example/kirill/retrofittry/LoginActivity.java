package com.example.kirill.retrofittry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.PendingIntent.getActivity;

/**
 * Created by Kirill on 20.04.2017.
 */

public class LoginActivity extends AppCompatActivity {
    public static final String POINT_URL = "http://192.168.1.50:8000";

    private API api;
    private TextView name;
    private TextView password;
    public String authtoken = "Token ";

    private void LoginUser(LoginBody logbody) {
        Call<LoginResponse> call = api.loginUser(logbody);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> logresponse) {
                if (logresponse.isSuccessful()) {
                    Log.e("sucsess", "it's worked");
                    authtoken = authtoken + logresponse.toString();
                } else {
                    Log.e("error response", "error with token");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("falue", "falue!!!", t);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(POINT_URL).
                addConverterFactory(GsonConverterFactory.create()).
                build();

        api = retrofit.create(API.class);
        name = (TextView) findViewById(R.id.Name);
        password = (TextView) findViewById(R.id.Password);

        Button loginbtn = (Button) findViewById(R.id.loginbtn);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginBody body = new LoginBody();
                body.username = name.getText().toString();
                body.password = password.getText().toString();
                LoginUser(body);
            }
        });

    }
}
