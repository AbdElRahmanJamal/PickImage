package example.com.saveimage.Retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IApiService {

    @FormUrlEncoded
    @POST("saveImageRetrofitToDB.php")
    Call<Results> createTask(@Field("encoded_string") String encoded_string,@Field("image_name")String image_name);

    @POST("testObject.php")
    Call<Results> createTask(@Body ImageData imageData);




}
