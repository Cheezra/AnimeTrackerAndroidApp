package com.example.animetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AnimeListAdapter extends RecyclerView.Adapter<AnimeListAdapter.ViewHolder> {

    //click handler interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    //Define listener member variable
    private OnItemClickListener listener;

    //Define the method that allows the parent activity to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView numEpsTextView;
        public TextView epsWatchedTextView;
        public TextView totalTimeTextView;

        public ViewHolder(final View itemView) {

            super(itemView);

            nameTextView = itemView.findViewById(R.id.anime_name);
            numEpsTextView = itemView.findViewById(R.id.num_eps);
            epsWatchedTextView = itemView.findViewById(R.id.eps_watched);
            totalTimeTextView = itemView.findViewById(R.id.total_time);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }

            });

        }



    }

    private List<Anime> mAnimeList;

    public AnimeListAdapter(List<Anime> animeList) {
        mAnimeList = animeList;
    }

    //inflates the layout from XML and returns the holder
    @Override
    public AnimeListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View animeView = inflater.inflate(R.layout.item_anime, parent, false);

        ViewHolder viewHolder = new ViewHolder(animeView);
        return viewHolder;

    }

    //populates data into the item through holder
    @Override
    public void onBindViewHolder(AnimeListAdapter.ViewHolder viewHolder, int position) {

        Anime anime = mAnimeList.get(position);

        TextView nameView = viewHolder.nameTextView;
        nameView.setText(anime.getEntryNum() + ") " + anime.getName());
        TextView numEpsView = viewHolder.numEpsTextView;
        numEpsView.setText("Number of Episodes: " + anime.getNumEpisodes());
        TextView epsWatchedView = viewHolder.epsWatchedTextView;
        epsWatchedView.setText("Episodes Watched: " + anime.getEpisodesWatched());
        TextView totalTimeView = viewHolder.totalTimeTextView;
        totalTimeView.setText("Total Time: " + anime.getTimeSpent());

    }

    //returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mAnimeList.size();
    }

}
