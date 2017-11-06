package gulshansutey.gitreposearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 11/5/2017.
 */

public class SearchResultModel implements Serializable{



    @SerializedName("items")
    public List<Item> Items=new ArrayList<>();

    public class Item implements Serializable{

        @SerializedName("id")
        @Expose
        public String id;

        @SerializedName("name")
        @Expose
        public String name;

        @SerializedName("full_name")
        @Expose
        public String full_name;

        @SerializedName("watchers_count")
        @Expose
        public String watchers_count;

        @SerializedName("html_url")
        @Expose
        public String html_url;

        @SerializedName("description")
        @Expose
        public String description;

        @SerializedName("contributors_url")
        @Expose
        public String contributors_url;

        @SerializedName("commits_url")
        @Expose
        public String commits_url;
        
        @SerializedName("updated_at")
        @Expose
        public String updated_at;

        @SerializedName("created_at")
        @Expose
        public String created_at;

        @SerializedName("owner")
        public Owner owner;

    }

   public class Owner implements Serializable{

        @SerializedName("avatar_url")
        @Expose
        public String avatar_url;

        @SerializedName("name")
        @Expose
        public String name;

        @SerializedName("full_name")
        @Expose
        public String full_name;

    }


}
