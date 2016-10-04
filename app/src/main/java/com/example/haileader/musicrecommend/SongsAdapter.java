package com.example.haileader.musicrecommend;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by haileader on 16/09/16.
 */
public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder>{

    private List<Song> items;
    private int itemLayout;

    public SongsAdapter(List<Song> items, int itemLayout){
        this.items = items;
        this.itemLayout = itemLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song item = items.get(position);
        holder.title.setText(item.title);
        holder.album.setText(item.album);
        holder.artist.setText(item.artist);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public TextView album;
        public TextView artist;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            album = (TextView) itemView.findViewById(R.id.album);
            artist = (TextView) itemView.findViewById(R.id.artist);
        }
    }
}
