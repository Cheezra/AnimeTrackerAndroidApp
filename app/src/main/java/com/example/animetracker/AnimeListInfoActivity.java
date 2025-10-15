package com.example.animetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class AnimeListInfoActivity extends AppCompatActivity {

    public static final String RETURNED_LISTS = "com.example.animetracker.ListInfo.RETURNED";     //list to return to the main activity
    public static final int RETURN_LISTS = 1;     //return code for the list returned by the list activity
    public static final String SENT_LISTS = "com.example.animetracker.ListInfo.SENT";     //list that is sent to the list anime activity

    public static final String LIST_INDEX = "com.example.animetracker.ListInfo.LIST_INDEX"; //index to determine which AnimeList is being listed
    public static final String STATUS_CHOICE = "com.example.animetracker.ListInfo.STATUS_CHOICE";

    private static final String FILENAME = "lists.txt";

    ArrayList<AnimeList> lists;
    int listIndex;
    AnimeList list;

    static int statusChoice = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_list_info);

        //get the intent that started the activity and extract the chosen anime list
        Intent intent = getIntent();
        lists = (ArrayList<AnimeList>) intent.getSerializableExtra(MainActivity.SENT_LISTS);
        listIndex = (int) intent.getSerializableExtra(MainActivity.LIST_INDEX);
        list = lists.get(listIndex);

        populateViews(list);
    }

    public void populateViews(AnimeList animeList) {

        //get all textView objects
        TextView userNameView = findViewById(R.id.user_text);
        TextView timeSpentView = findViewById(R.id.time_spent_text);

        //fill text objects with appropriate information
        userNameView.setText(animeList.getUserName() + "'s Anime List");
        timeSpentView.setText("Total Time Spent Watching Anime: \n" + animeList.findTimeSpent());

        //-----------------------------------------------------

        //add anime items

        //get the linear layout, edit text, and button objects
        LinearLayout addAnimePanel = findViewById(R.id.add_anime_layout);
        EditText addNameView = findViewById(R.id.add_name_text);
        EditText addEpLengthView = findViewById(R.id.add_length_text);
        EditText addNumEpsView = findViewById(R.id.add_num_episodes_text);
        EditText addEpsWatchedView = findViewById(R.id.add_episodes_watched_text);
        Button submitAnimeButton = findViewById(R.id.submit_anime_button);
        TextView warningText = findViewById(R.id.add_anime_warning);

        //hide all information
        addAnimePanel.setVisibility(LinearLayout.INVISIBLE);
        submitAnimeButton.setVisibility(Button.INVISIBLE);
        warningText.setText("");
        addNameView.setText("");
        addEpLengthView.setText("");
        addNumEpsView.setText("");
        addEpsWatchedView.setText("");

        //----------------------------------------

        //remove anime items

        //get the layouts, textViews, and buttons
        LinearLayout removeAnimePanel = findViewById(R.id.remove_anime_layout);
        EditText entryNumInputView = findViewById(R.id.remove_input);
        Button removeCancelButton = findViewById(R.id.remove_cancel);
        Button removeConfirmButton = findViewById(R.id.remove_confirm);
        Button removeQueryButton = findViewById(R.id.remove_query);
        TextView removeWarningView = findViewById(R.id.remove_warning);

        //hide all information
        removeQueryButton.setVisibility(Button.VISIBLE);
        removeAnimePanel.setVisibility(LinearLayout.INVISIBLE);
        removeCancelButton.setVisibility(Button.INVISIBLE);
        removeConfirmButton.setVisibility(Button.INVISIBLE);
        removeWarningView.setText("");
        entryNumInputView.setText("");


        //fill out pie chart
        PieChart mPieChart = findViewById(R.id.pie_chart);
        ArrayList<PieEntry> dataList = new ArrayList<PieEntry>();
        dataList.add(new PieEntry(list.getAnimeListWithStatus(0).size(),"Watching"));
        dataList.add(new PieEntry(list.getAnimeListWithStatus(1).size(), "Completed"));
        dataList.add(new PieEntry(list.getAnimeListWithStatus(2).size(), "On-Hold"));
        dataList.add(new PieEntry(list.getAnimeListWithStatus(3).size(), "Dropped"));
        dataList.add(new PieEntry(list.getAnimeListWithStatus(4).size(), "Plan to Watch"));

        piechart(mPieChart, dataList);

    }

    //pie chart click listener
    static OnChartValueSelectedListener selectedListener = new OnChartValueSelectedListener() {
        @Override
        public void onValueSelected(Entry e, Highlight h) {
            PieEntry entry = (PieEntry) e;
            String status = entry.getLabel();
            switch(status) {
                case "Watching":        statusChoice = 0;
                                        break;
                case "Completed":       statusChoice = 1;
                                        break;
                case "On-Hold":         statusChoice = 2;
                                        break;
                case "Dropped":         statusChoice = 3;
                                        break;
                case "Plan to Watch":   statusChoice = 4;
                                        break;
                default:                statusChoice = -1;
                                        break;
            }
        }

        @Override
        public void onNothingSelected() {
            statusChoice = -1;
        }
    };

    //pie chart value formatter
    static IValueFormatter formatter = new IValueFormatter() {

        DecimalFormat format = new DecimalFormat("###,##0");

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return format.format(value);
        }
    };

    //pie chart inflation method
    public static void piechart(PieChart pieChart, ArrayList<PieEntry> arrayList) {

        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(2, 5, 2, 2);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setRotationEnabled(false);
        pieChart.setLongClickable(true);
        pieChart.setHighlightPerTapEnabled(true);
        PieDataSet pieDataSet = new PieDataSet(arrayList, "");
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(15f);


        //Custom colors in the pie chart
        int[] colors = {
                Color.rgb(13, 166, 10),
                Color.rgb(20, 70, 184),
                Color.rgb(255, 210, 28),
                Color.rgb(194, 44, 17),
                Color.rgb(186, 186, 186)
        };

        ArrayList<Integer> arrayList1 = new ArrayList<>();
        for (int c : colors) {
            arrayList1.add(c);
        }

        pieDataSet.setColors(arrayList1);
        pieDataSet.setColors(ColorTemplate.createColors(colors));
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(18f);
        pieData.setValueTextColor(Color.WHITE);
        pieChart.setData(pieData);
        pieChart.setCenterTextSize(30f);
        pieChart.setDrawEntryLabels(false);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutExpo);

        Legend l = pieChart.getLegend();
        l.setFormSize(10);
        l.setTextSize(10);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setWordWrapEnabled(false);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);

        //listeners
        pieChart.setOnChartValueSelectedListener(selectedListener);

        //value formatter
        pieData.setValueFormatter(formatter);

    }

    public void onClickAddAnimeButton(View view) {

        //get the linear layout, edit text, and button objects
        LinearLayout addAnimePanel = findViewById(R.id.add_anime_layout);
        Button submitAnimeButton = findViewById(R.id.submit_anime_button);

        //make necessary information visible
        addAnimePanel.setVisibility(LinearLayout.VISIBLE);
        submitAnimeButton.setVisibility(Button.VISIBLE);

        //make other panels invisible
        LinearLayout removeAnimePanel = findViewById(R.id.remove_anime_layout);
        removeAnimePanel.setVisibility(LinearLayout.INVISIBLE);

    }

    public void onClickSubmitAnimeButton(View view) {

        //get the edit text objects
        EditText addNameView = findViewById(R.id.add_name_text);
        EditText addEpLengthView = findViewById(R.id.add_length_text);
        EditText addNumEpsView = findViewById(R.id.add_num_episodes_text);
        EditText addEpsWatchedView = findViewById(R.id.add_episodes_watched_text);
        TextView warningText = findViewById(R.id.add_anime_warning);

        String name, lengthString, numEpsString, epsWatchedString, warning = "";
        int length = 0, numEps = 0, epsWatched = 0;

        //assign each field to its variable and make sure all inputs are valid
        name = addNameView.getText().toString();
        lengthString = addEpLengthView.getText().toString();
        numEpsString = addNumEpsView.getText().toString();
        epsWatchedString = addEpsWatchedView.getText().toString();

        if (isInteger(epsWatchedString)) epsWatched = Integer.parseInt(epsWatchedString); else warning = "Please enter an integer for episodes watched.";
        if (isInteger(numEpsString)) numEps = Integer.parseInt(numEpsString); else warning = "Please enter an integer for number of episodes.";
        if (isInteger(lengthString)) length = Integer.parseInt(lengthString); else warning = "Please enter an integer for episode length.";

        //if no warnings
        if (warning.equals("")) {

            //add the anime to the list
            list.addAnime(name, length, numEps, epsWatched, 0);

            //update the information on the screen
            populateViews(list);

        } else {

            //display warning
            warningText.setText(warning);

        }


    }

    public boolean isInteger(String input) {

        try {
            Integer.parseInt(input);
            return true;
        }catch (NumberFormatException e) {
            return false;
        }

    }

    public void onClickRemoveAnimeButton(View view) {

        //get the layouts, textViews, and buttons
        LinearLayout removeAnimePanel = findViewById(R.id.remove_anime_layout);
        EditText entryNumInputView = findViewById(R.id.remove_input);
        Button removeQueryButton = findViewById(R.id.remove_query);
        Button removeCancelButton = findViewById(R.id.remove_cancel);
        Button removeConfirmButton = findViewById(R.id.remove_confirm);
        TextView removeWarningView = findViewById(R.id.remove_warning);

        //make necessary information visible
        removeAnimePanel.setVisibility(LinearLayout.VISIBLE);

        //hide the other panel
        LinearLayout addAnimePanel = findViewById(R.id.add_anime_layout);
        Button submitAnimeButton = findViewById(R.id.submit_anime_button);
        addAnimePanel.setVisibility(LinearLayout.INVISIBLE);
        submitAnimeButton.setVisibility(Button.INVISIBLE);
    }

    public void onClickRemoveQueryButton(View view) {

        //get the textView object and extract the string
        EditText entryNumView = findViewById(R.id.remove_input);
        String entryNumString = entryNumView.getText().toString();

        //get the warning view
        TextView warningView = findViewById(R.id.remove_warning);

        //convert the entryNum to an int, or display the proper warning
        int entryNum = 0;
        if (isInteger(entryNumString)) {
            entryNum = Integer.parseInt(entryNumString);

            //check if the entryNum is in bounds
            if (entryNum < 1 || entryNum > list.getNumAnime())
                warningView.setText("Please choose a valid integer (1 - " + list.getNumAnime() + ")");
            else {
                //ask for confirmation about the anime they want to delete
                warningView.setText("Are you sure you want to delete " + list.getAnime(entryNum).getName() + "?");

                //display the confirm/cancel buttons and hide the query button
                Button confirm = findViewById(R.id.remove_confirm);
                Button cancel = findViewById(R.id.remove_cancel);
                confirm.setVisibility(Button.VISIBLE);
                cancel.setVisibility(Button.VISIBLE);
                view.setVisibility(Button.INVISIBLE);
            }
        }
        else
            warningView.setText("Please enter an integer.");


    }

    //clears text and hides information
    public void onClickRemoveCancelButton(View view) {

        EditText inputView = findViewById(R.id.remove_input);
        TextView warningView = findViewById(R.id.remove_warning);
        LinearLayout removeLayout = findViewById(R.id.remove_anime_layout);
        Button removeCancelButton = findViewById(R.id.remove_cancel);
        Button removeConfirmButton = findViewById(R.id.remove_confirm);
        Button removeQueryButton = findViewById(R.id.remove_query);

        //reset button visibility
        removeCancelButton.setVisibility(Button.INVISIBLE);
        removeConfirmButton.setVisibility(Button.INVISIBLE);
        removeQueryButton.setVisibility(Button.VISIBLE);

        //clear text
        inputView.setText("");
        warningView.setText("");

        //hide everything
        removeLayout.setVisibility(LinearLayout.INVISIBLE);

    }

    public void onClickRemoveConfirmButton(View view) {

        //we already made sure the number entered was valid, so no need to do it again
        EditText inputView = findViewById(R.id.remove_input);
        int entryNum = Integer.parseInt(inputView.getText().toString());

        list.removeAnime(entryNum);

        //update information on screen while hiding remove information
        populateViews(list);

    }

    public void onClickListAnimeButton(View view) {

        Intent intent = new Intent(this, ListAnimeActivity.class);
        intent.putExtra(SENT_LISTS, lists);
        intent.putExtra(LIST_INDEX, listIndex);
        intent.putExtra(STATUS_CHOICE, statusChoice);
        startActivityForResult(intent, RETURN_LISTS);

    }

    public void onClickSaveButton(View view) {

        ArrayList<AnimeList> resultLists = lists;
        Intent result = new Intent();
        result.putExtra(RETURNED_LISTS, resultLists);
        setResult(Activity.RESULT_OK, result);
        finish();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode) {
            case RETURN_LISTS:       ArrayList<AnimeList> newAnimeLists = (ArrayList<AnimeList>)data.getSerializableExtra(ListAnimeActivity.RETURNED_LISTS);
                                    lists = newAnimeLists;
                                    list = lists.get(listIndex);
                                    populateViews(list);
                                    break;

            default:                break;
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onPause () {
        super.onPause();

        //saves the list to the file when the app loses focus

        //save the AnimeList object to a file
        Context context = getBaseContext();

        try {

            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(lists);

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

    /*
    public void OnClickViewButton(View view) {

        //get the textbox and extract the string
        EditText entryNumBox = findViewById(R.id.anime_select_text);
        String entryNumString = entryNumBox.getText().toString();

        //get the warning textbox in case it's needed
        TextView warningView = findViewById(R.id.warning_text);

        //convert to an integer while checking for dangerous inputs
        try {
            int entryNum = Integer.parseInt(entryNumString);

            if (entryNum > 0 && entryNum <= list.getNumAnime()) {
                //remove the current warning message
                warningView.setText("");

                //start an activity and create and extra with the desired anime
                Intent intent = new Intent(this, AnimeInfoActivity.class);
                intent.putExtra(CHOSEN_ANIME, list.getAnime(entryNum));
                startActivityForResult(intent, RETURNED_ANIME);

            } else {
                //display the proper warning
                warningView.setText("Please choose a valid entry number (1 - " + list.getNumAnime() + ")");
            }

        }
        catch (NumberFormatException e) {
            //display the proper warning
            warningView.setText("Please enter an integer.");
        }

    }
    */
}
