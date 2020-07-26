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

    public static final String RETURNED_LISTS = "com.example.animetracker.ListAnime.RETURNED";   //AnimeList to be returned to AnimeListInfoActivity
    public static final String SENT_LISTS = "com.example.animetracker.ListAnime.SENT";  //Anime object to send to AnimeInfoActivity
    public static final int RETURN_LISTS = 5;   //requestCode for the Anime object returned from AnimeInfoActivity
    public static final String ANIME_INDEX = "com.example.animetracker.ListAnime.ANIME_INDEX";    //index to determine which anime was chosen to edit
    public static final String LIST_INDEX = "com.example.animetracker.ListAnime.LIST_INDEX";    //index to determine which AnimeList was chosen to edit

    private static final String FILENAME = "lists.txt";

    RecyclerView rvAnimeList;
    AnimeListAdapter adapter;
    ArrayList<AnimeList> animeLists;
    int listIndex;
    AnimeList animeList;

    int animeIndex;

    //variables to recreate the animelist in order to return it
    String username;
    ArrayList<Anime> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_anime);

        Intent intent = getIntent();

        rvAnimeList = (RecyclerView) findViewById(R.id.rv_anime_list);

        animeLists = (ArrayList<AnimeList>) intent.getSerializableExtra(AnimeListInfoActivity.SENT_LISTS);
        listIndex = (int) intent.getSerializableExtra(AnimeListInfoActivity.LIST_INDEX);
        animeList = animeLists.get(listIndex);

        username = animeList.getUserName();
        list = animeList.getAnimeList();

        adapter = new AnimeListAdapter(list);

        adapter.setOnItemClickListener(new AnimeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

                ArrayList<AnimeList> toSend = animeLists;
                animeIndex = position;

                Intent intent = new Intent(ListAnimeActivity.this, AnimeInfoActivity.class);
                intent.putExtra(SENT_LISTS, toSend);
                intent.putExtra(LIST_INDEX, listIndex);
                intent.putExtra(ANIME_INDEX, animeIndex);
                startActivityForResult(intent, RETURN_LISTS);

            }
        });

        rvAnimeList.setAdapter(adapter);

        rvAnimeList.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvAnimeList.addItemDecoration(itemDecoration);

    }

    public void onClickSaveButton(View view) {

        AnimeList listResult = new AnimeList(username, list);   //TODO: this may not be necessary, due to the fact that ArrayLists are reference classes,
        animeLists.set(listIndex, listResult);                  //TODO: but I'm too scared to remove it
        Intent result = new Intent();
        result.putExtra(RETURNED_LISTS, animeLists);
        setResult(Activity.RESULT_OK, result);
        finish();

    }

    /*
    public void onClickViewButton(View view) {

        View group = (View)view.getParent().getParent();
        animeIndex = rvAnimeList.getChildAdapterPosition(group);
        ArrayList<AnimeList> toSend = animeLists;

        Intent intent = new Intent(this, AnimeInfoActivity.class);
        intent.putExtra(SENT_LISTS, toSend);
        intent.putExtra(LIST_INDEX, listIndex);
        intent.putExtra(ANIME_INDEX, animeIndex);
        startActivityForResult(intent, RETURN_LISTS);

    }
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case RETURN_LISTS:
                if (data != null) {
                    ArrayList<AnimeList> newLists = (ArrayList<AnimeList>)data.getSerializableExtra(AnimeInfoActivity.RETURNED_LISTS);
                    list.set(animeIndex, newLists.get(listIndex).getAnime((int)data.getSerializableExtra(AnimeInfoActivity.RETURNED_ENTRY_NUM)));
                    Collections.sort(list);
                    animeList = new AnimeList(username, list);
                    animeLists.set(listIndex, animeList);
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
            os.writeObject(animeLists);

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
