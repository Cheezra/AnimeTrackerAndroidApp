package com.example.animetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AnimeListArrayAdapter extends RecyclerView.Adapter<AnimeListArrayAdapter.ViewHolder> {

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

        public TextView usernameTextView;
        public TextView numAnimeTextView;
        public TextView timeSpentTextView;

        public ViewHolder (final View itemView) {

            super(itemView);

            usernameTextView = (TextView) itemView.findViewById(R.id.username_text);
            numAnimeTextView = (TextView) itemView.findViewById(R.id.num_anime_text);
            timeSpentTextView = (TextView) itemView.findViewById(R.id.time_spent_text);

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
