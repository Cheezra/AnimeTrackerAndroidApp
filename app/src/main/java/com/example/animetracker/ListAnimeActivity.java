package com.example.animetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;

public class ListAnimeActivity extends AppCompatActivity {

    public static final String RETURNED_LIST = "com.example.animetracker.ListAnime.RETURNED";   //AnimeList to be returned to AnimeListInfoActivity
    public static final String SENT_ANIME = "com.example.animetracker.ListAnime.SENT";  //Anime object to send to AnimeInfoActivity
    public static final int RETURN_ANIME = 5;   //requestCode for the Anime object returned from AnimeInfoActivity

    RecyclerView rvAnimeList;
    AnimeListAdapter adapter;
    AnimeList animeList;

    int index;

    //variables to recreate the animelist in order to return it
    String username;
    ArrayList<Anime> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_anime);

        Intent intent = getIntent();

        rvAnimeList = (RecyclerView) findViewById(R.id.rv_anime_list);

        animeList = (AnimeList)intent.getSerializableExtra(AnimeListInfoActivity.SENT_LIST);

        username = animeList.getUserName();
        list = animeList.getAnimeList();

        adapter = new AnimeListAdapter(list);

        rvAnimeList.setAdapter(adapter);

        rvAnimeList.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvAnimeList.addItemDecoration(itemDecoration);

    }

    public void onClickSaveButton(View view) {

        AnimeList listResult = new AnimeList(username, list);
        Intent result = new Intent();
        result.putExtra(RETURNED_LIST, listResult);
        setResult(Activity.RESULT_OK, result);
        finish();

    }

    public void onClickViewButton(View view) {

        View group = (View)view.getParent().getParent();
        index = rvAnimeList.getChildAdapterPosition(group);
        Anime toSend = list.get(index);

        Intent intent = new Intent(this, AnimeInfoActivity.class);
        intent.putExtra(SENT_ANIME, toSend);
        startActivityForResult(intent, RETURN_ANIME);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case RETURN_ANIME:
                if (data != null) {
                    Anime newAnime = (Anime)data.getSerializableExtra(AnimeInfoActivity.RETURNED_ANIME);
                    list.set(index, newAnime);
                    Collections.sort(list);
                    adapter.notifyDataSetChanged();
                }
                break;

            default:
                break;

        }

        super.onActivityResult(requestCode, resultCode, data);

    }
}
