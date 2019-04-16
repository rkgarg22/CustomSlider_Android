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


public class WeightSliderAdapter extends RecyclerView.Adapter<WeightSliderAdapter.ViewHolder> {
    Context context;
    int MIN_VALUE = 1;
    int MAX_VALUE = 250;

    public WeightSliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public WeightSliderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.weight_slider_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(WeightSliderAdapter.ViewHolder holder, int position) {
        if ((position + 1) % 10 == 0 && position != 0) {
            holder.nohighLightView.setVisibility(View.VISIBLE);
            int value = MIN_VALUE + position;
            holder.valueTextView.setText("" + value);
            holder.cyanColorView.setVisibility(View.GONE);
            holder.greyColorView.setVisibility(View.GONE);
        } else if ((position + 1) % 5 == 0 && position != 0) {
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
        int count = (MAX_VALUE - MIN_VALUE);
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
