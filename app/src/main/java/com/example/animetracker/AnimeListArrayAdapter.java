package com.example.animetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class AnimeListArrayAdapter extends RecyclerView.Adapter<AnimeListArrayAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView usernameTextView;
        public TextView numAnimeTextView;
        public TextView timeSpentTextView;

        public ViewHolder (View itemView) {

            super(itemView);

            usernameTextView = (TextView) itemView.findViewById(R.id.username_text);
            numAnimeTextView = (TextView) itemView.findViewById(R.id.num_anime_text);
            timeSpentTextView = (TextView) itemView.findViewById(R.id.time_spent_text);

        }

    }

    private List<AnimeList> mAnimeLists;

    public AnimeListArrayAdapter (List<AnimeList> animeLists) {

        mAnimeLists = animeLists;

    }

    @Override
    public AnimeListArrayAdapter.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View animeListView = inflater.inflate(R.layout.item_anime_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(animeListView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(AnimeListArrayAdapter.ViewHolder holder, int position) {

        AnimeList animeList = mAnimeLists.get(position);

        TextView usernameView = holder.usernameTextView;
        TextView numAnimeView = holder.numAnimeTextView;
        TextView timeSpentView = holder.timeSpentTextView;

        usernameView.setText(animeList.getUserName());
        numAnimeView.setText(animeList.getNumAnime() + " series watched");
        timeSpentView.setText(animeList.findTimeSpent());

    }

    @Override
    public int getItemCount() {
        return mAnimeLists.size();
    }

}
