package example.com.saveimage.Retrofit;

public class IApiServiceInterfaceCreation {
    public static final String BASE_URL = "https://psychoapp.000webhostapp.com/";

    public static IApiService apiService() {
        return RetrofitObjectCreation.getClient(BASE_URL).create(IApiService.class);
    }
}
