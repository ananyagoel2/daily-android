package in.dailyatfive.socialify.network;

import in.dailyatfive.socialify.network.models.Register;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface API {

    public static String BASEURL = "http://35.154.27.204:3000";

    @FormUrlEncoded
    @POST("/register")
    Call<Register> registerUser(@Field("facebook_id") String fb_id,
                                @Field("access_token") String access_token);


}
