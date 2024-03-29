package hr.fer.caloriecounter.api;

import hr.fer.caloriecounter.model.UserDetail;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegisterApi {
    @POST("api/user")
    Call<UserDetail> saveUser(@Body UserDetail userDetail);

    @POST("api/user/update")
    Call<UserDetail> updateUser(@Body UserDetail userDetail);
}

