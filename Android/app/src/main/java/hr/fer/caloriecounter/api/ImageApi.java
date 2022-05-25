package hr.fer.caloriecounter.api;

import hr.fer.caloriecounter.model.Image;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ImageApi {
    @Multipart
    @POST("api/image")
    Call<Image> saveImage(@Part MultipartBody.Part part);
}
