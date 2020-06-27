package com.example.animetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AnimeInfoActivity extends AppCompatActivity {

    public static final String RETURNED_ANIME = "com.example.animetracker.AnimeInfo.RETURNED";  //Anime object to return to ListAnimeActivity

    Anime thisPage;
    int changeButtonPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_info);

        changeButtonPressed = 0;

        //get the intent that started the activity
        Intent intent = getIntent();

        //find the anime entry that this is for
        //Anime thisPage = new Anime("Ano Hi Mita Hana no Namae wo Bokutachi wa Mada Shiranai", 22, 11, 11);
        thisPage = (Anime)intent.getSerializableExtra(ListAnimeActivity.SENT_ANIME);

        //fill the page with the appropriate information
        fillPage(thisPage);
    }

    //gathers all of the information from the entry and populates the page
    public void fillPage(Anime thisAnime) {

        //create variables for all of the TextBoxes
        TextView entryNumView = findViewById(R.id.entry_num_text);
        TextView nameView = findViewById(R.id.name_text);
        TextView episodeLengthView = findViewById(R.id.episode_length_text);
        TextView numEpisodesView = findViewById(R.id.num_episodes_text);
        TextView episodesWatchedView = findViewById(R.id.episodes_watched_text);
        TextView timeSpentView = findViewById(R.id.time_spent_text);

        //fill the boxes with the appropriate information
        entryNumView.setText("Anime #" + thisAnime.getEntryNum() + ")");
        nameView.setText(thisAnime.getName());
        episodeLengthView.setText("Episode Length: " + thisAnime.getEpisodeLength() + " minutes");
        numEpisodesView.setText("Number of Episodes: " + thisAnime.getNumEpisodes() + " episodes");
        episodesWatchedView.setText("Episodes Watched: " + thisAnime.getEpisodesWatched() + " episodes");
        timeSpentView.setText("Time Spent: " + thisAnime.getTimeSpent());

        //create save button functionality
        Button saveButton = findViewById(R.id.save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Anime resultAnime = thisPage;
                Intent result = new Intent();
                result.putExtra(RETURNED_ANIME, resultAnime);
                setResult(Activity.RESULT_OK, result);
                finish();

            }
        });

    }

    public void onClickChangeButton(View view) {

        //get object references for the changeText, submitButton, and warningText
        EditText changeText = findViewById(R.id.change_text);
        Button submitButton = findViewById(R.id.submit_change_button);

        //set the changeText and submitButton to visible and enabled
        changeText.setVisibility(EditText.VISIBLE);
        submitButton.setVisibility(Button.VISIBLE);
        changeText.setEnabled(true);
        submitButton.setEnabled(true);

        //check which button was pressed and perform the appropriate actions
        switch (view.getId()) {

            case R.id.change_name_button:
                changeText.setHint("Type a new name for the series");
                changeButtonPressed = R.id.change_name_button;
                break;
            case R.id.change_episode_length_button:
                changeText.setHint("How long is each episode (in minutes)?");
                changeButtonPressed = R.id.change_episode_length_button;
                break;
            case R.id.change_num_episodes_button:
                changeText.setHint("How many episodes are in the series?");
                changeButtonPressed = R.id.change_num_episodes_button;
                break;
            case  R.id.change_episodes_watched_button:
                changeText.setHint("How many episodes have you watched?");
                changeButtonPressed = R.id.change_episodes_watched_button;
                break;
            default:
                break;
        }

    }

    //click listener for submitButton
    public void onClickSubmitButton(View view) {

        boolean successfullyChanged = false;

        //get object references for the changeText, submitButton, and warningText
        EditText changeText = findViewById(R.id.change_text);
        Button submitButton = findViewById(R.id.submit_change_button);
        TextView warningText = findViewById(R.id.warning_text2);

        //get the string from the changeText
        String input = changeText.getText().toString();

        //determine which button was pressed and perform the appropriate operations
        switch (changeButtonPressed) {

            case R.id.change_name_button:

                //set the anime name
                thisPage.setName(input);
                successfullyChanged = true;

                break;
            case R.id.change_episode_length_button:

                //make sure the input is an integer
                try {
                    int length = Integer.parseInt(input);

                    //set the episode length
                    thisPage.setEpisodeLength(length);
                    successfullyChanged = true;
                }
                catch (NumberFormatException e) {
                    System.out.println(e);
                    warningText.setText("Please enter the episode length as an integer.");
                }

                break;
            case R.id.change_num_episodes_button:

                //make sure the input is an integer
                try {
                    int newNum = Integer.parseInt(input);

                    //set the number of episodes
                    thisPage.setNumEpisodes(newNum);
                    successfullyChanged = true;
                }
                catch (NumberFormatException e) {
                    System.out.println(e);
                    warningText.setText("Please enter the number of episodes as an integer.");
                }

                break;
            case  R.id.change_episodes_watched_button:

                //make sure the input is an integer
                try {
                    int newNum = Integer.parseInt(input);

                    //set the number of episodes
                    thisPage.setEpisodesWatched(newNum);
                    successfullyChanged = true;
                }
                catch (NumberFormatException e) {
                    System.out.println(e);
                    warningText.setText("Please enter the episodes watched as an integer.");
                }

                break;
            default:
                break;
        }

        //only clear everything if something changed successfully
        if (successfullyChanged) {

            //disable the changeText and submitButton views
            changeText.setEnabled(false);
            submitButton.setEnabled(false);
            changeText.setVisibility(EditText.INVISIBLE);
            submitButton.setVisibility(Button.INVISIBLE);

            changeButtonPressed = 0;

            //clear text from the changeText and warning
            changeText.setText("");
            warningText.setText("");

            //update the information on the screen
            fillPage(thisPage);
        }

    }
}