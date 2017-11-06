package gulshansutey.gitreposearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by asus on 11/6/2017.
 */

public class ContributorModel implements Serializable{

    @SerializedName("avatar_url")
    @Expose
    public String avatar_url;

    @SerializedName("login")
    @Expose
    public String login;

    @SerializedName("repos_url")
    @Expose
    public String url;

}
