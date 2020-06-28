package com.example.animetracker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class AnimeList implements Serializable {

    private ArrayList<Anime> mAnimeList;
    private int mNumAnime;
    private int mTimeSpentMinutes;
    private String mTotalTimeSpent;

    private String mUsername;

    public AnimeList(String username) {
        mAnimeList = new ArrayList<Anime>();
        mNumAnime = 0;
        mTimeSpentMinutes = 0;
        mTotalTimeSpent = "0 minutes";
        mUsername = username;
    }

    public AnimeList(String username, ArrayList<Anime> animeList) {

        mUsername = username;
        mAnimeList = new ArrayList<Anime>();

        for (Anime anime : animeList) {
            addAnime(anime.getName(), anime.getEpisodeLength(), anime.getNumEpisodes(), anime.getEpisodesWatched());
        }

    }

    public int getNumAnime() {
        return mNumAnime;
    }

    public int getTimeSpentMinutes() {
        return mTimeSpentMinutes;
    }

    public String getUserName() {
        return mUsername;
    }

    public Anime getAnime(int entryNum) {
        return mAnimeList.get(entryNum - 1);
    }

    public ArrayList<Anime> getAnimeList() {
        return mAnimeList;
    }

    public void setUsername(String newUsername) {
        mUsername = newUsername;
    }

    public void setAnime(int entryNum, Anime newAnime) {

        mAnimeList.set(entryNum - 1, newAnime);
        Collections.sort(mAnimeList);
        recalculateEntryNums();
        updateTimeSpent();

    }

    public void addAnime(String name, int episodeLength, int numEpisodes, int episodesWatched) {

        Anime newAnime = new Anime(name, episodeLength, numEpisodes, episodesWatched);
        newAnime.setEntryNum(++mNumAnime);
        mAnimeList.add(newAnime);
        Collections.sort(mAnimeList);
        recalculateEntryNums();

        updateTimeSpent();

    }

    public boolean removeAnime(String name) {

        boolean wasRemoved = false;

        //check of the anime exists
        for (Anime anime : mAnimeList) {
            if (anime.getName().equals(name)) {
                mAnimeList.remove(anime);
                recalculateEntryNums();
                wasRemoved = true;
                break;
            }
        }

        updateTimeSpent();

        return wasRemoved;

    }

    public boolean removeAnime(int entryNum) {

        boolean wasRemoved;

        if (entryNum > 0 && entryNum <= mNumAnime) {
            mAnimeList.remove(entryNum - 1);
            mNumAnime--;
            recalculateEntryNums();
            wasRemoved = true;
        } else {
            wasRemoved = false;
        }

        updateTimeSpent();

        return wasRemoved;
    }

    public void updateTimeSpent() {

        int timeSpent = 0;

        for (Anime anime : mAnimeList) {
            timeSpent += anime.getEpisodeLength() * anime.getEpisodesWatched();
        }

        mTimeSpentMinutes = timeSpent;

    }

    public String findTimeSpent() {

        String timeSpent = "";

        int minutes = getTimeSpentMinutes() % 60;
        int hours = (int)Math.floor(getTimeSpentMinutes() / 60.0);

        timeSpent = hours + " hours, " + minutes + " minutes";
        return timeSpent;

    }

    public void recalculateEntryNums() {

        for (int i = 1; i <= mAnimeList.size(); i++) {
            mAnimeList.get(i - 1).setEntryNum(i);
        }

    }

    public String toString() {
        return mAnimeList.toString();
    }

}
