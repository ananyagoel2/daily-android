
package in.dailyatfive.socialify.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Register {

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("min_version_code")
    @Expose
    private String minVersionCode;
    @SerializedName("current_version_code")
    @Expose
    private String currentVersionCode;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getMinVersionCode() {
        return minVersionCode;
    }

    public void setMinVersionCode(String minVersionCode) {
        this.minVersionCode = minVersionCode;
    }

    public String getCurrentVersionCode() {
        return currentVersionCode;
    }

    public void setCurrentVersionCode(String currentVersionCode) {
        this.currentVersionCode = currentVersionCode;
    }

}

