package com.sevenlearn.musicplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.slider.Slider;
import com.sevenlearn.musicplayer.databinding.ActivityMainBinding;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private List<Music> musicList = Music.getList();
    private MediaPlayer mediaPlayer;
    private MusicState musicState = MusicState.STOPPED;
    private Timer timer;

    enum MusicState {
        PLAYING, PAUSED, STOPPED
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RecyclerView recyclerView = findViewById(R.id.rv_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(new MusicAdapter(musicList));

        onMusicChange(musicList.get(0));
        binding.playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (musicState) {
                    case PLAYING:
                        mediaPlayer.pause();
                        musicState = MusicState.PAUSED;
                        binding.playBtn.setImageResource(R.drawable.ic_play_32dp);
                        break;
                    case PAUSED:
                    case STOPPED:
                        mediaPlayer.start();
                        musicState = MusicState.PLAYING;
                        binding.playBtn.setImageResource(R.drawable.ic_pause_24dp);
                        break;
                }
            }
        });
    }

    private void onMusicChange(final Music music) {
        mediaPlayer = MediaPlayer.create(this, music.getMusicFileResId());
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.positionTv.setText(Music.convertMillisToString(mediaPlayer.getCurrentPosition()));
                                binding.musicSlider.setValue(mediaPlayer.getCurrentPosition());
                            }
                        });

                    }
                }, 1000, 1000);
                binding.durationTv.setText(Music.convertMillisToString(mediaPlayer.getDuration()));
                binding.musicSlider.setValueTo(mediaPlayer.getDuration());
                musicState = MusicState.PLAYING;
                binding.playBtn.setImageResource(R.drawable.ic_pause_24dp);
            }
        });

        binding.coverIv.setActualImageResource(music.getCoverResId());
        binding.artistIv.setActualImageResource(music.getArtistResId());
        binding.artistTv.setText(music.getArtist());
        binding.musicNameTv.setText(music.getName());

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}
