package com.zflabs.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private String[] trailerData;

    private final TrailerAdapter.TrailerAdapterClickHandler mClickHandler;

    public interface TrailerAdapterClickHandler {
        void onClick(String trailerId);
    }

    public TrailerAdapter(TrailerAdapter.TrailerAdapterClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView textView;
        public TrailerViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.trailer_text);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String actualTrailerId = trailerData[adapterPosition];
            mClickHandler.onClick(actualTrailerId);
        }
    }

    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.detail_trailer_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TrailerAdapter.TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.TrailerViewHolder trailerAdapterViewHolder, int position) {

    }

    @Override
    public int getItemCount() {
        if (null == trailerData) return 0;
        return trailerData.length;
    }

    public void setTrailerData(String[] trailerData) {
        this.trailerData = trailerData;
        notifyDataSetChanged();
    }

}
