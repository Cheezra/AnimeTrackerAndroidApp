package com.example.animetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class ListAnimeActivity extends AppCompatActivity {

    public static final String RETURNED_LIST = "com.example.animetracker.ListAnime.RETURNED";   //AnimeList to be returned to AnimeListInfoActivity
    public static final String SENT_LIST = "com.example.animetracker.ListAnime.SENT";  //Anime object to send to AnimeInfoActivity
    public static final int RETURN_LIST = 5;   //requestCode for the Anime object returned from AnimeInfoActivity
    public static final String ANIME_INDEX = "com.example.animetracker.ListAnime.INDEX";

    private static final String FILENAME = "lists.txt";

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
        AnimeList toSend = animeList;

        Intent intent = new Intent(this, AnimeInfoActivity.class);
        intent.putExtra(SENT_LIST, toSend);
        intent.putExtra(ANIME_INDEX, index);
        startActivityForResult(intent, RETURN_LIST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case RETURN_LIST:
                if (data != null) {
                    AnimeList newList = (AnimeList)data.getSerializableExtra(AnimeInfoActivity.RETURNED_LIST);
                    list.set(index, newList.getAnime(index + 1));
                    Collections.sort(list);
                    animeList = new AnimeList(username, list);
                    adapter.notifyDataSetChanged();
                }
                break;

            default:
                break;

        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    public void onPause () {
        super.onPause();

        //saves the list to the file when the app loses focus

        //save the AnimeList object to a file
        Context context = getBaseContext();

        try {

            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(animeList);

            os.close();
            fos.close();

        }
        catch (FileNotFoundException e) {
            System.out.println(e);
        }
        catch (IOException e) {
            System.out.println(e);
        }

    }
}
