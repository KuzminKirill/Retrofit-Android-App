package com.example.kirill.retrofittry;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextClock;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kirill on 08.05.2017.
 */

public class TableCourseActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://192.168.1.44:8000";
    private TextView coursename;
    private TextView coursedescription;
    private TextView coursetime;
    private TextView hyperlink;
    private GetCourses getCourses;
    private static final int NOTIFY_ID = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course);

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
        coursename = (TextView) findViewById(R.id.coursename);
        coursedescription = (TextView) findViewById(R.id.coursedescription);
        coursetime = (TextView) findViewById(R.id.coursetime);
        hyperlink = (TextView) findViewById(R.id.hyperlink);
        loadCourse(2);

        TextView hyperlink = (TextView) findViewById(R.id.hyperlink);
        hyperlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TableCourseActivity.this, CourseDetailActivity.class);
                startActivity(i);
            }
        });
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
            coursename.setText("Курс №1");
            coursedescription .setText(course.getDescription());
            Date d = new Date(course.getStart());
            SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
            String formatted = formatDate.format(d);
            //formatted = "Дата публикации: " + formatted;

            Context context = getApplicationContext();

            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context,
                    0, notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            Resources res = context.getResources();
            Notification.Builder builder = new Notification.Builder(context);

            builder.setContentIntent(contentIntent)
                    //.setTicker(res.getString(R.string.warning)) // текст в строке состояния
                    .setSmallIcon(R.drawable.nots)
                    .setTicker("Уведомление")
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    //.setContentTitle(res.getString(R.string.notifytitle)) // Заголовок уведомления
                    .setContentTitle("Появились новые учебные материалы")
                    //.setContentText(res.getString(R.string.notifytext))
                    .setContentText("Опубликован Курс №1"); // Текст уведомления

            Notification notification = builder.getNotification();

            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFY_ID, notification);
            coursetime.setText(formatted);
        } else {
            coursename.setText("Ошибка загрузки курса");
        }
    }
}
