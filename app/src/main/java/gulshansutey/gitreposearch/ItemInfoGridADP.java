package gulshansutey.gitreposearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by asus on 11/4/2017.
 */

public class ItemInfoGridADP extends RecyclerView.Adapter<ItemInfoGridADP.VH> {

    private Context context;


    private List<ContributorModel>  contributorsList;
    public ItemInfoGridADP(Context context, List<ContributorModel> contributorsList) {
        this.contributorsList=contributorsList;
        this.context=context;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(View.inflate(context, R.layout.adapter_item_info_grid,null));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
              holder.tv_start.setText(contributorsList.get(position).login);
        Picasso.with(context)
                .load(contributorsList.get(position).avatar_url).placeholder(R.drawable.git_place_holder)
                .into(holder.iv_contributor_avatar);
    }

    @Override
    public int getItemCount() {
        return contributorsList.size();
    }

      class VH extends RecyclerView.ViewHolder {
        private TextView tv_start,tv_end,tv_start_save,tv_end_save;
        private CircleImageView iv_contributor_avatar;
        private VH(View itemView) {
            super(itemView);
            tv_start=(TextView) itemView.findViewById(R.id.tv_start);
            iv_contributor_avatar=(CircleImageView) itemView.findViewById(R.id.iv_contributor_avatar);
        }
    }
}
