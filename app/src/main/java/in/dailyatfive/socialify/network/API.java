package in.dailyatfive.socialify.network;

import java.util.Map;

import in.dailyatfive.socialify.network.models.Register;
import in.dailyatfive.socialify.network.models.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface API {

    public static String BASEURL = "http://35.154.27.204:3000";

    @GET("/users/{user_id}")
    Call<User> getUser(@Path("user_id") String user_id ,
                       @Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("/register")
    Call<Register> registerUser(@Field("facebook_id") String fb_id,
                                @Field("access_token") String access_token);


    @FormUrlEncoded
    @PUT("/users/{user_id}")
    Call<Void> updateUser(@FieldMap Map<String,String> parameters,
                          @Path("user_id") String user_id ,
                          @Header("Authorization") String authorization);

}
