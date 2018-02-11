package com.example.kirill.retrofittry.Interfaces;

import com.example.kirill.retrofittry.Parsers.Question;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Kirill on 08.05.2017.
 */

public interface GetQuestions {
    @GET("questions")
    Call<GetThemes.Questions> all();

    @GET("questions/{id}")
    Call<Question> select(@Path("id") int id);
}
