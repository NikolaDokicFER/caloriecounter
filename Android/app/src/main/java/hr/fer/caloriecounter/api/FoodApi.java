package hr.fer.caloriecounter.api;

import java.util.List;

import hr.fer.caloriecounter.model.Food;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FoodApi {
    @POST("api/food")
    Call<Food> saveFood(@Body Food food);

    @GET("api/food")
    Call<List<Food>> getAllFood();

    @GET("api/food/{name}")
    Call<Food> getFood(@Path("name") String name);
}
