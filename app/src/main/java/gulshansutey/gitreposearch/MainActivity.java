package gulshansutey.gitreposearch;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, android.support.v7.widget.SearchView.OnQueryTextListener, RvItemClickListener.OnItemClickListener {

    private SearchResultsRvADP searchResultsRvADP;
    private RecyclerView rv_search_results;
    private android.support.v7.widget.SearchView search_view;
    private List<SearchResultModel.Item> searchResultModel;
    private ProgressBar progressBar;
    private ImageView iv_empty_screen;
    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        search_view = (android.support.v7.widget.SearchView) findViewById(R.id.search_view);
        iv_empty_screen = (ImageView) findViewById(R.id.iv_empty_screen);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            search_view.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        search_view.setOnQueryTextListener(this);
        rv_search_results = (RecyclerView) findViewById(R.id.rv_search_results);
        rv_search_results.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        rv_search_results.setNestedScrollingEnabled(false);
        rv_search_results.addOnItemTouchListener(new RvItemClickListener(MainActivity.this, this));

    }

    public void retroSearchApi(String keyword, String sort, String order) {
        setProgressBar(true);
        MyApplication.getInstance().makeRequest().getRepos("10",keyword, sort, order).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<SearchResultModel>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(SearchResultModel resultModel) {
                setProgressBar(false);
                if (iv_empty_screen.getVisibility() == View.VISIBLE && resultModel.Items.isEmpty()) {
                    iv_empty_screen.setVisibility(View.GONE);
                }
                searchResultModel = resultModel.Items;
                searchResultsRvADP = new SearchResultsRvADP(MainActivity.this, searchResultModel);
                rv_search_results.setAdapter(searchResultsRvADP);
            }

            @Override
            public void onError(Throwable e) {
                setProgressBar(false);
            }

            @Override
            public void onComplete() {

            }
        });

    }

    private void setProgressBar(boolean isLoading) {

        if (isLoading) {
            if (progressBar.getVisibility() == View.GONE) {
                progressBar.setVisibility(View.VISIBLE);
            }
        } else {
            if (progressBar.getVisibility() == View.VISIBLE) {
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        retroSearchApi(query, "watchers", "desc");
        search_view.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void onItemClick(View view, int position) {
        Intent repoActivity = new Intent(MainActivity.this, RepoDetailsActvity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", searchResultModel.get(position));
        repoActivity.putExtras(bundle);
        startActivity(repoActivity);

    }
}
