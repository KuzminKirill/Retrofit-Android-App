package com.example.kirill.retrofittry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
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
    public static final String POINT_URL = "http://192.168.1.44:8000";

    private API api;
    private TextView name;
    private TextView password;
    public String authtoken = "Token ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(POINT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        api = retrofit.create(API.class);

        name = (TextView) findViewById(R.id.Name);
        password = (TextView) findViewById(R.id.Password);

        TextView registertext = (TextView) findViewById(R.id.registerurl);
        registertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

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

    private void LoginUser(LoginBody loginbody) {
        Call<LoginResponse> call = api.loginUser(loginbody);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    Log.e("sucsess", "it's worked");
                    //authtoken = authtoken + response.toString();
                    Intent i = new Intent(LoginActivity.this, TableCourseActivity.class);
                    startActivity(i);
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


}
