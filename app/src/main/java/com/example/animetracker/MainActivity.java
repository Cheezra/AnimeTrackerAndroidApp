package com.example.animetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String SENT_LISTS = "com.example.animetracker.MainActivity.SENT";  //AnimeList object to be sent to AnimeListInfoActivity
    public static final String LIST_INDEX = "com.example.animetracker.MainActivity.INDEX";  //Index to determine which AnimeList was selected
    public static final int RETURNED_LISTS_EDITED = 2; //requestCode for the AnimeLists returned by AnimeListInfoActivity

    private static final String FILENAME = "lists.txt";

    ArrayList<AnimeList> myLists;
    RecyclerView rvAnimeLists;
    AnimeListArrayAdapter adapter;

    EditText addUsernameText;
    EditText removeListText;
    Button addListConfirmButton;
    Button removeListConfirmButton;
    Button cancelButton;
    Button addListButton;
    Button removeListButton;
    TextView warningText;

    //TODO: Add progress bars to the AnimeList RecyclerView


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialization for testing purposes
        //create anime list here
        //myList = new AnimeList("PieBroCool");

        /*
        myList.addAnime("3-gatsu no Lion", 25, 45, 45);
        myList.addAnime("5-toubun no Hanayome", 25, 12, 12);
        myList.addAnime("Ano Hi Mita Hana no Namae wo Bokutachi wa Mada Shiranai", 22, 11, 11);
        myList.addAnime("K-On!", 24, 41, 108);
        myList.addAnime("Chuunibyou demo Koi ga Shitai", 24, 12, 24);
        myList.addAnime("Hibike! Euphonium", 25, 27, 81);
        myList.addAnime("Made in Abyss", 24, 13, 26);
        myList.addAnime("Tzurezure Children", 15, 12, 12);
        myList.addAnime("Isekai Quartet", 11, 12, 12);
        myList.addAnime("Gakkougurashi!", 23, 12, 36);*/

        //initialize all button and editText references
        addUsernameText = (EditText) findViewById(R.id.add_username_text);
        removeListText = (EditText) findViewById(R.id.remove_list_text);
        addListConfirmButton = (Button) findViewById(R.id.add_list_confirm_button);
        removeListConfirmButton = (Button) findViewById(R.id.remove_list_confirm_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        addListButton = (Button) findViewById(R.id.add_button);
        removeListButton = (Button) findViewById(R.id.remove_button);
        warningText = (TextView) findViewById(R.id.warning_text);

        //set visibility for necessary components
        addUsernameText.setVisibility(EditText.INVISIBLE);
        removeListText.setVisibility(EditText.INVISIBLE);
        addListConfirmButton.setVisibility(Button.INVISIBLE);
        removeListConfirmButton.setVisibility(Button.INVISIBLE);
        cancelButton.setVisibility(Button.INVISIBLE);
        addListButton.setVisibility(Button.VISIBLE);
        removeListButton.setVisibility(Button.VISIBLE);
        warningText.setVisibility(TextView.INVISIBLE);

        myLists = new ArrayList<AnimeList>();

        //get the saved list
        retrieveFile();

        //set the recyclerView
        rvAnimeLists = (RecyclerView) findViewById(R.id.rv_anime_lists);

        adapter = new AnimeListArrayAdapter(myLists);

        //set the click listener
        adapter.setOnItemClickListener(new AnimeListArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

                Intent intent = new Intent(MainActivity.this, AnimeListInfoActivity.class);

                //add the anime list as an extra for the intent
                intent.putExtra(SENT_LISTS, myLists);
                intent.putExtra(LIST_INDEX, position);

                startActivityForResult(intent, RETURNED_LISTS_EDITED);

            }
        });

        rvAnimeLists.setAdapter(adapter);
        rvAnimeLists.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvAnimeLists.addItemDecoration(itemDecoration);

    }

    /*
    public void onButtonPressed(View view) {

        int index = 0;

        Intent intent = new Intent(this, AnimeListInfoActivity.class);

        //add the anime list as an extra for the intent
        intent.putExtra(SENT_LISTS, myLists);
        intent.putExtra(LIST_INDEX, index);

        startActivityForResult(intent, RETURNED_LISTS);

    }
     */

    public void onClickAddButton(View view) {

        rvAnimeLists.setVisibility(RecyclerView.INVISIBLE);
        addUsernameText.setVisibility(EditText.VISIBLE);
        addListConfirmButton.setVisibility(Button.VISIBLE);
        cancelButton.setVisibility(Button.VISIBLE);
        addListButton.setVisibility(Button.INVISIBLE);
        removeListButton.setVisibility(Button.INVISIBLE);
        removeListText.setVisibility(EditText.INVISIBLE);
        removeListConfirmButton.setVisibility(Button.INVISIBLE);

    }

    public void onClickRemoveButton(View view) {

        rvAnimeLists.setVisibility(RecyclerView.INVISIBLE);
        addListButton.setVisibility(Button.INVISIBLE);
        removeListButton.setVisibility(Button.INVISIBLE);
        cancelButton.setVisibility(Button.VISIBLE);
        removeListText.setVisibility(EditText.VISIBLE);
        removeListConfirmButton.setVisibility(Button.VISIBLE);

    }

    public void onClickAddConfirmButton(View view) {

        //get the username from the text box
        String newUsername = addUsernameText.getText().toString();

        //check if the new username is taken or not
        boolean isTaken = false;
        for (AnimeList list : myLists) {

            if (list.getUserName().equals(newUsername)) {
                isTaken = true;
                break;
            }

        }

        //perform the appropriate action
        if (!isTaken) {
            //create the AnimeList and add it to the ArrayList
            AnimeList newList = new AnimeList(newUsername);
            myLists.add(newList);

            //update the recyclerView
            adapter.notifyItemInserted(myLists.size() - 1);

            //return the activity to the start state
            onClickCancelButton(cancelButton);
        } else {
            //show a warning
            warningText.setVisibility(TextView.VISIBLE);
            warningText.setText("This username is already taken.");
        }

    }

    public void onClickRemoveConfirmButton(View view) {

        //get the username of the AnimeList to remove
        String removeUsername = removeListText.getText().toString();

        //find the index of the AnimeList with the given username
        int index = -1;
        boolean foundList = false;
        for (int i = 0; i < myLists.size(); i++) {

            if (myLists.get(i).getUserName().equals(removeUsername)) {

                index = i;
                foundList = true;
                break;

            }

        }

        if (foundList) {
            //remove the AnimeList from the ArrayList
            myLists.remove(index);

            //update the RecyclerView
            adapter.notifyItemRemoved(index);

            //return the activity to the start state
            onClickCancelButton(cancelButton);
        } else {
            //show a warning
            warningText.setVisibility(TextView.VISIBLE);
            warningText.setText("The AnimeList with this username was not found");
        }

    }

    public void onClickCancelButton(View view) {

        addUsernameText.setText("");
        removeListText.setText("");
        warningText.setText("");
        addUsernameText.setVisibility(EditText.INVISIBLE);
        removeListText.setVisibility(EditText.INVISIBLE);
        addListConfirmButton.setVisibility(Button.INVISIBLE);
        removeListConfirmButton.setVisibility(Button.INVISIBLE);
        cancelButton.setVisibility(Button.INVISIBLE);
        addListButton.setVisibility(Button.VISIBLE);
        removeListButton.setVisibility(Button.VISIBLE);
        rvAnimeLists.setVisibility(RecyclerView.VISIBLE);
        warningText.setVisibility(TextView.INVISIBLE);

    }

    @Override
    public void onPause () {
        super.onPause();

        //save the AnimeList object to a file
        Context context = getBaseContext();

        try {

            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(myLists);

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

    public void retrieveFile () {

        Context context = getBaseContext();

        try {

            FileInputStream fis = context.openFileInput(FILENAME);
            ObjectInputStream is = new ObjectInputStream(fis);
            myLists = (ArrayList<AnimeList>) is.readObject();

            is.close();
            fis.close();

        }
        catch (FileNotFoundException e) {
            System.out.println(e);
        }
        catch (IOException e) {
            System.out.println(e);
        }
        catch (ClassNotFoundException e) {
            System.out.println(e);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case RETURNED_LISTS_EDITED: {
                if (data != null) {
                    myLists = (ArrayList<AnimeList>)data.getSerializableExtra(AnimeListInfoActivity.RETURNED_LISTS);
                    adapter.notifyDataSetChanged();
                }
            }
            break;

            default:            break;
        }

        super.onActivityResult(requestCode, resultCode, data);

    }



    //These are methods to test object serialization in android apps
    //changing some object references will be needed to make them work
    /*
    public void sendText(View view) {

        //get the TextView and extract the current string from it
        TextView input = findViewById(R.id.inputText);
        String text = input.getText().toString();

        //create a TextClass Object
        TextClass textClass = new TextClass(text);

        Context context = getBaseContext();

        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(textClass);

            os.close();
            fos.close();

        }
        catch (FileNotFoundException e) {
            System.out.println(e);
        }
        catch (IOException e) {
            System.out.println(e);
        }
        catch (Exception e) {
            System.out.println(e);
        }


    }

    public void retrieveText(View view) {

        //get output TextView
        TextView output = findViewById(R.id.outputView);

        //get the context
        Context context = getBaseContext();

        //retrieve the TextClass Object
        String text = "";
        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            ObjectInputStream is = new ObjectInputStream(fis);
            TextClass textClass = (TextClass) is.readObject();
            text = textClass.getText();

            is.close();
            fis.close();
        }
        catch (FileNotFoundException e) {
            System.out.print(e);
            text = "Could not find the file.";
        }
        catch (IOException e) {
            System.out.println(e);
            text = "An IO Exception Occurred.";
        }
        catch (Exception e) {
            System.out.println(e);
            text = "An exception occurred.";
        }
        finally {
            output.setText(text);
            System.out.println(text);
        }

    } */
}
