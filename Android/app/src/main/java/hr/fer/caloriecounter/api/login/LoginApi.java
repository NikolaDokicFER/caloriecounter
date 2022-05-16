package hr.fer.caloriecounter.api.login;

import hr.fer.caloriecounter.model.UserDetail;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface LoginApi {
    @GET("api/user/{username}/{password}")
    Call<UserDetail> getUser(@Path("username") String username, @Path("password") String password);
}
