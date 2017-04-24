package com.example.kirill.retrofittry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;


import okhttp3.OkHttpClient;
import okhttp3.ConnectionPool;
import okhttp3.ConnectionSpec;
import okhttp3.Connection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class GetCoursesActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://192.168.1.50:8000";
    private TextView coursesTV;
    private GetCourses getCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_courses);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        getCourses = retrofit.create(GetCourses.class);


        Button allbtn = (Button) findViewById(R.id.getallBtn);
        allbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCourses();
            }
        });

        Button onebtn = (Button) findViewById(R.id.oneBtn);
        onebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCourse(2);
            }
        });

        coursesTV = (TextView) findViewById(R.id.coursesTv);

    }

    private void loadCourses() {
        Call<Courses> call = getCourses.all();
        call.enqueue(new Callback<Courses>() {
            @Override
            public void onResponse(Call<Courses> call, Response<Courses> response) {
                Courses courses = response.body();
                displayCourses(courses);
            }

            @Override
            public void onFailure(Call<Courses> call, Throwable t) {
                Log.e("one course", "I got an error with all courses", t);
            }
        });
    }

    private void displayCourses(Courses c) {
        if (c != null) {
            List<Course> courses = c.getCourses();
            String tmp = "";

            for (Course course : courses) {
                tmp += course.getId() + " | " + course.getDescription() + " | ";

                coursesTV.setText(tmp);
            }
        } else {
            coursesTV.setText("Error to get Courses");
        }
    }

    private void loadCourse(int id) {
        Call<Course> call = getCourses.select(id);
        call.enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, Response<Course> response) {
                displayCourse(response.body());
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {
                Log.e("one course", "I got an error with one course", t);
            }
        });
    }

    private void displayCourse(Course course) {
        if (course != null) {
            String tmp = course.getId() + " | " + course.getDescription() + " | ";
            coursesTV.setText(tmp);
        } else {
            coursesTV.setText("Error to get Course");
        }
    }
}
