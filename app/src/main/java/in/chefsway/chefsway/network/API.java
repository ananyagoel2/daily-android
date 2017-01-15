package in.chefsway.chefsway.network;

import java.util.Map;

import in.chefsway.chefsway.network.models.LoginRegister;
import in.chefsway.chefsway.network.models.User;
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
    Call<LoginRegister> registerUser(@Field("email") String email,
                                     @Field("password") String password);

    @FormUrlEncoded
    @POST("/login")
    Call<LoginRegister> loginUser(@Field("email") String email,
                                  @Field("password") String password);

    @FormUrlEncoded
    @PUT("/users/{user_id}")
    Call<Void> updateUser(@FieldMap Map<String,String> parameters,
                          @Path("user_id") String user_id ,
                          @Header("Authorization") String authorization);

    @GET("/register/email/{email}")
    Call<Void> checkEmail(@Path("email") String email);

}
