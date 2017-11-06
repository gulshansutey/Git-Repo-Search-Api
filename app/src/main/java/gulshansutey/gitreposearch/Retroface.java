package gulshansutey.gitreposearch;


import com.google.gson.JsonArray;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by asus on 11/5/2017.
 */

public interface Retroface {

    @GET("search/repositories")
    Observable<SearchResultModel> getRepos(@Query("per_page") String per_page, @Query("q") String keywords, @Query("sort") String sort, @Query("order") String order);

    @GET
    Observable<List<ContributorModel>> getContributors(@Url String url);


    @GET
    Observable<List<SearchResultModel.Item>> getContributorsDetails(@Url String url);

    @GET
    Observable<JsonArray> getComitts(@Url String url, @Query("sha") String s
            , @Query("since") String since, @Query("until") String until);
}
