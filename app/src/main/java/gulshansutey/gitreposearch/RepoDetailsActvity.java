package gulshansutey.gitreposearch;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RepoDetailsActvity extends AppCompatActivity implements View.OnClickListener, RvItemClickListener.OnItemClickListener {
    private SearchResultModel.Item repoData;
    private RecyclerView rv_contributors;
    private ProgressBar pb_contributors;
    private List<ContributorModel> contributorsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_details_actvity);
        if (getIntent().getExtras() != null) {
            repoData = (SearchResultModel.Item) getIntent().getExtras().getSerializable("data");
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(repoData.full_name);
        }

        rv_contributors = (RecyclerView) findViewById(R.id.rv_contributors);
        rv_contributors.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false));
        ImageView iv_repo_avatar = (ImageView) findViewById(R.id.iv_repo_avatar);
        pb_contributors = (ProgressBar) findViewById(R.id.pb_contributors);
        TextView tv_repo_name = (TextView) findViewById(R.id.tv_repo_name);
        TextView tv_click_here = (TextView) findViewById(R.id.tv_click_here);
        TextView tv_repo_description = (TextView) findViewById(R.id.tv_repo_description);
        if (repoData != null) {
            Picasso.with(this)
                    .load(repoData.owner.avatar_url).placeholder(R.drawable.git_place_holder)
                    .into(iv_repo_avatar);
            tv_repo_name.setText(repoData.name);
            String description = getString(R.string.description) + repoData.description;
            tv_repo_description.setText(description);
            tv_click_here.setOnClickListener(this);
            getContributorApi(repoData.contributors_url);
        }
        rv_contributors.setNestedScrollingEnabled(false);
        rv_contributors.addOnItemTouchListener(new RvItemClickListener(RepoDetailsActvity.this, this));

    }

    private void setProgressBar(boolean isLoading) {

        if (isLoading) {
            if (pb_contributors.getVisibility() == View.GONE) {
                pb_contributors.setVisibility(View.VISIBLE);
            }
        } else {
            if (pb_contributors.getVisibility() == View.VISIBLE) {
                pb_contributors.setVisibility(View.GONE);
            }
        }
    }

    private void getContributorApi(String url) {
        setProgressBar(true);
        if (url.contains(MyApplication.BASE_URL)) {
            url = url.replace(MyApplication.BASE_URL, "");
        }
        MyApplication.getInstance().makeRequest().getContributors(url).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<ContributorModel>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<ContributorModel> contributorModels) {
                setProgressBar(false);
                contributorsList = contributorModels;
                rv_contributors.setAdapter(new ItemInfoGridADP(RepoDetailsActvity.this, contributorModels));

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_click_here:
                Intent webViewActivity = new Intent(RepoDetailsActvity.this, WebViewActivity.class);
                webViewActivity.putExtra("URL", repoData.html_url);
                startActivity(webViewActivity);
        }
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
        Intent intentContributorActivity = new Intent(this, ContributorDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", contributorsList.get(position));
        intentContributorActivity.putExtras(bundle);
        startActivity(intentContributorActivity);
    }
}
