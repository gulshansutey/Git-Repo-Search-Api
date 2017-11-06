package gulshansutey.gitreposearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ContributorDetailsActivity extends AppCompatActivity implements RvItemClickListener.OnItemClickListener {

    private ContributorModel contributorModel;
    private ProgressBar pb_contributor_details;
    private RecyclerView rv_contributor_repos;
    private List<SearchResultModel.Item> searchResultModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contributor_details);
        if (getIntent().getExtras() != null) {
            contributorModel = (ContributorModel) getIntent().getExtras().getSerializable("data");
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(contributorModel.login);
        }

        pb_contributor_details = (ProgressBar) findViewById(R.id.pb_contributor_details);
        rv_contributor_repos = (RecyclerView) findViewById(R.id.rv_contributor_repos);
        rv_contributor_repos.setLayoutManager(new LinearLayoutManager(this));
        ImageView iv_contributor_avatar = (ImageView) findViewById(R.id.iv_contributor_avatar);
        Picasso.with(this)
                .load(contributorModel.avatar_url).placeholder(R.drawable.git_place_holder)
                .into(iv_contributor_avatar);
        rv_contributor_repos.addOnItemTouchListener(new RvItemClickListener(ContributorDetailsActivity.this, this));
        getContributorDetailsApi(contributorModel.url);
    }

    private void setProgressBar(boolean isLoading) {

        if (isLoading) {
            if (pb_contributor_details.getVisibility() == View.GONE) {
                pb_contributor_details.setVisibility(View.VISIBLE);
            }
        } else {
            if (pb_contributor_details.getVisibility() == View.VISIBLE) {
                pb_contributor_details.setVisibility(View.GONE);
            }
        }
    }

    private void getContributorDetailsApi(String url) {
        setProgressBar(true);
        if (url.contains(MyApplication.BASE_URL)){
            url=url.replace(MyApplication.BASE_URL,"");
        }

        MyApplication.getInstance().makeRequest().getContributorsDetails(url).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<SearchResultModel.Item>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<SearchResultModel.Item> searchResultModel1) {
                setProgressBar(false);
                searchResultModel = searchResultModel1;
                rv_contributor_repos.setAdapter(new SearchResultsRvADP(ContributorDetailsActivity.this, searchResultModel1));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(View view, int position) {
        Intent repoActivity = new Intent(ContributorDetailsActivity.this, RepoDetailsActvity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", searchResultModel.get(position));
        repoActivity.putExtras(bundle);
        startActivity(repoActivity);
    }
}
