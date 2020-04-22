package com.sevenlearn.musicplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.w3c.dom.Text;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {
    private List<Music> musicList = Music.getList();


    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MusicViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_music, parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        holder.bindMusic(musicList.get(position));
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView simpleDraweeView;
        private TextView artistTv;
        private TextView musicNameTv;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            simpleDraweeView = itemView.findViewById(R.id.iv_music);
            artistTv = itemView.findViewById(R.id.tv_music_artist);
            musicNameTv = itemView.findViewById(R.id.tv_music_name);
        }

        public void bindMusic(Music music) {
            simpleDraweeView.setActualImageResource(music.getCoverResId());
            artistTv.setText(music.getArtist());
            musicNameTv.setText(music.getName());
        }
    }
}
