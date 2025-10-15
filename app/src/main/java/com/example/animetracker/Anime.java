package com.example.animetracker;

import java.io.Serializable;

public class Anime implements Serializable, Comparable<Anime> {

    private int mEntryNum;
    private String mName;
    private int mStatus;
    private int mEpisodeLength;
    private int mNumEpisodes;
    private int mEpisodesWatched;
    private String mTimeSpent;

    private final String[] STATUSES = {"Watching", "Completed", "On Hold", "Dropped", "Plan to Watch"};

    public Anime(String name, int episodeLength, int numEpisodes, int episodesWatched, int status) {

        mEntryNum = -1;
        mName = name;
        mEpisodeLength = episodeLength;
        mNumEpisodes = numEpisodes;
        mEpisodesWatched = episodesWatched;
        mTimeSpent = updateTimeSpent();
        mStatus = status;

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

    public int getStatus() {
        return mStatus;
    }

    public String getStatusString() {
        return STATUSES[getStatus()];
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

    public void setStatus(int newStatus) {

        mStatus = newStatus;
        if (newStatus < 0 || newStatus > 4) {
            mStatus = 1;
        }

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

        String sortIdThis = getStatus() + " " + getName();
        String sortIdOther = other.getStatus() + " " + other.getName();

        return sortIdThis.compareTo(sortIdOther);

    }

    public String toString() {
        return getName();
    }

}
