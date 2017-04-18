package com.example.kirill.retrofittry;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Sergey on 18.04.2017.
 */

public interface GetThemes {
    @GET("themes")
    Call<Themes> all();

    @GET("courses/{id}")
    Call<Theme> select(@Path("id") int id);
}
