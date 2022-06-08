package hr.fer.caloriecounter;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {
    public final static String BASE_URL = "https://calorie-counter-zav.herokuapp.com/";
    private static Retrofit retrofit;

    public static Retrofit retrofit(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
