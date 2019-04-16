package Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.weightdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HeightSliderAdapter extends RecyclerView.Adapter<HeightSliderAdapter.ViewHolder> {
    Context context;
   // int MIN_VALUE = 1;
    int MAX_VALUE = 121;

    public HeightSliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public HeightSliderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.height_slider_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HeightSliderAdapter.ViewHolder holder, int position) {
        if ((position) % 12 == 0 ) {
            holder.nohighLightView.setVisibility(View.VISIBLE);
            int value = (position/12);
            holder.valueTextView.setText("" + value);
            holder.cyanColorView.setVisibility(View.GONE);
            holder.greyColorView.setVisibility(View.GONE);
        } else if ((position) % 6 == 0 && position != 0) {
            holder.nohighLightView.setVisibility(View.GONE);
            holder.cyanColorView.setVisibility(View.GONE);
            holder.greyColorView.setVisibility(View.VISIBLE);
        } else {
            holder.nohighLightView.setVisibility(View.GONE);
            holder.cyanColorView.setVisibility(View.VISIBLE);
            holder.greyColorView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        int count = (MAX_VALUE);
        return count;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cyanColorView)
        TextView cyanColorView;

        @BindView(R.id.greyColorView)
        TextView greyColorView;

        @BindView(R.id.valueTextView)
        TextView valueTextView;

        @BindView(R.id.nohighLightView)
        LinearLayout nohighLightView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
