package com.basha.karim.baking.retrofit;

import com.basha.karim.baking.models.Recipe;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by karim on 4/7/2018.
 */

public class RetrofitBuilder {

    static RecipesInterface interfaceRecipes;
    static OkHttpClient httpClient = new OkHttpClient.Builder().build();

    public static RecipesInterface fetchRecipes() {
        interfaceRecipes = new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/")
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build().create(RecipesInterface.class);
        return interfaceRecipes;
    }

    public interface RecipesInterface {
        @GET("baking.json")
        Call<ArrayList<Recipe>> getRecipesListTask();
    }
}
