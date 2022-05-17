package hr.fer.caloriecounter.api;

import java.util.List;

import hr.fer.caloriecounter.model.Meal;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MealApi {
    @POST("api/meal")
    Call<Meal> saveMeal(@Body Meal meal);

    @GET("api/meal/{userId}/{date}")
    Call<List<Meal>> getMeals(@Path("userId") Long userId, @Path("date") String date);
}
