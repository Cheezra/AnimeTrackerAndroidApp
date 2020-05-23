package com.example.animetracker;

import java.io.Serializable;

public class Anime implements Serializable, Comparable<Anime> {

    private int mEntryNum;
    private String mName;
    private int mEpisodeLength;
    private int mNumEpisodes;
    private int mEpisodesWatched;
    private String mTimeSpent;

    public Anime(String name, int episodeLength, int numEpisodes, int episodesWatched) {

        mEntryNum = -1;
        mName = name;
        mEpisodeLength = episodeLength;
        mNumEpisodes = numEpisodes;
        mEpisodesWatched = episodesWatched;
        mTimeSpent = updateTimeSpent();

    }

    public int getEntryNum() {
        return mEntryNum;
    }

    public String getName() {
        return mName;
    }

    public int getEpisodeLength() {
        return mEpisodeLength;
    }

    public int getNumEpisodes() {
        return mNumEpisodes;
    }

    public int getEpisodesWatched() {
        return mEpisodesWatched;
    }

    public String getTimeSpent() {
        return mTimeSpent;
    }

    public void setEntryNum(int newNum) {
        mEntryNum = newNum;
    }

    public void setName(String newName) {
        mName = newName;
    }

    public void setEpisodeLength(int newNum) {

        mEpisodeLength = newNum;
        setTimeSpent();
    }

    public void setNumEpisodes(int newNum) {
        mNumEpisodes = newNum;
    }

    public void setEpisodesWatched(int newNum) {

        mEpisodesWatched = newNum;
        setTimeSpent();
    }

    public void setTimeSpent() {
        mTimeSpent = updateTimeSpent();
    }

    public String updateTimeSpent() {

        String timeSpent = "";
        int timeSpentMinutes = mEpisodeLength * mEpisodesWatched;

        int minutes = timeSpentMinutes % 60;
        int hours = (int)Math.floor(timeSpentMinutes / 60.0);

        timeSpent = hours + " hours, " + minutes + " minutes";

        return timeSpent;

    }

    public int compareTo(Anime other) {

        return this.getName().compareTo(other.getName());

    }

    public String toString() {
        return getName();
    }

}
