package gulshansutey.gitreposearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by asus on 11/5/2017.
 */

class SearchResultsRvADP extends RecyclerView.Adapter<SearchResultsRvADP.VH> {
    private Context context;
    private List<SearchResultModel.Item> searchResultModel;
    private List<String> commits=new ArrayList<>();
    public SearchResultsRvADP(Context context, List<SearchResultModel.Item> searchResultModel) {
        this.searchResultModel=searchResultModel;
        this.context=context;
        for (int i=0;i<searchResultModel.size();i++){
            commits.add("Loading...") ;
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View  view=View.inflate(context,R.layout.adapter_search_results,null);

        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {

        Picasso.with(context)
                .load(searchResultModel.get(position).owner.avatar_url).placeholder(R.drawable.git_place_holder)
                .into(holder.iv_avatar);
        holder.tv_name.setText(searchResultModel.get(position).name);
        holder.tv_full_name.setText(searchResultModel.get(position).full_name);

        String watchers="Watchers: "+searchResultModel.get(position).watchers_count;
        holder.tv_watchers.setText(watchers);
        holder.tv_commits.setText(commits.get(position));

        if (commits.get(position).equals("Loading...")){
            getComittsApi(searchResultModel.get(position).commits_url, holder.tv_commits,position,searchResultModel.get(position).created_at,searchResultModel.get(position).updated_at);
        }


    }

    @Override
    public int getItemCount() {

        if (searchResultModel.isEmpty()){
            return 0;
        }
        return searchResultModel.size()<10?searchResultModel.size():10;
    }

    public class VH extends RecyclerView.ViewHolder {
        private ImageView iv_avatar;
        private TextView tv_name,tv_full_name,tv_watchers,tv_commits;
        public VH(View itemView) {
            super(itemView);
            iv_avatar=(ImageView)itemView.findViewById(R.id.iv_avatar);
            tv_name=(TextView)itemView.findViewById(R.id.tv_name);
            tv_commits=(TextView)itemView.findViewById(R.id.tv_commits);
            tv_full_name=(TextView)itemView.findViewById(R.id.tv_full_name);
            tv_watchers=(TextView)itemView.findViewById(R.id.tv_watchers);
        }
    }

    private void getComittsApi(String url, final TextView textView, final int pos,String since,String until) {

      url=url.replace("{/sha}","");
      
        if (url.contains(MyApplication.BASE_URL)){
            url=url.replace(MyApplication.BASE_URL,"");
        }
        MyApplication.getInstance().makeRequest().getComitts(url,"master",since,until).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<JsonArray>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(JsonArray jsonArray) {
                String comit="Commits: "+jsonArray.size();
                textView.setText(comit);
                commits.set(pos,comit);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }
}
