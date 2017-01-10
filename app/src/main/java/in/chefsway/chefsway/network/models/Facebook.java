
package in.chefsway.chefsway.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Facebook {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("facebook_data")
    @Expose
    private String facebookData;
    @SerializedName("long_access_token")
    @Expose
    private String longAccessToken;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFacebookData() {
        return facebookData;
    }

    public void setFacebookData(String facebookData) {
        this.facebookData = facebookData;
    }

    public String getLongAccessToken() {
        return longAccessToken;
    }

    public void setLongAccessToken(String longAccessToken) {
        this.longAccessToken = longAccessToken;
    }


}
