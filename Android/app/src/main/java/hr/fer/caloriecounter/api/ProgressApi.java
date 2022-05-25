package hr.fer.caloriecounter.api;

import java.util.List;

import hr.fer.caloriecounter.model.Progress;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProgressApi {
    @POST("api/progress")
    Call<Progress> saveProgress(@Body Progress progress);

    @GET("api/progress/{userId}")
    Call<List<Progress>> getProgress(@Path("userId") Long userId);
}
