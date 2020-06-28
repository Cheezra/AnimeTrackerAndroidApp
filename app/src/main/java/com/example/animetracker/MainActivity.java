package com.example.animetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity {

    public static final String CHOSEN_LIST = "com.example.animetracker.MainActivity.SENT";  //AnimeList objrct to be sent to AnimeListInfoActivity
    public static final int RETURNED_LIST = 2; //requestCode for the AnimeList returned by AnimeListInfoActivity

    private static final String FILENAME = "lists.txt";

    AnimeList myList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create anime list here
        //myList = new AnimeList("PieBroCool");

        /*
        //Initialization for testing purposes
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

        //get the saved list
        onClickRetrieveButton(findViewById(R.id.retrieve_button));
    }

    public void onButtonPressed(View view) {

        Intent intent = new Intent(this, AnimeListInfoActivity.class);

        //add the anime list as an extra for the intent
        intent.putExtra(CHOSEN_LIST, myList);

        startActivityForResult(intent, RETURNED_LIST);

    }

    public void onClickSaveButton (View view) {

        //save the AnimeList object to a file
        Context context = getBaseContext();

        try {

            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(myList);

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

    public void onClickRetrieveButton (View view) {

        Context context = getBaseContext();

        try {

            FileInputStream fis = context.openFileInput(FILENAME);
            ObjectInputStream is = new ObjectInputStream(fis);
            myList = (AnimeList) is.readObject();

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

            case RETURNED_LIST: if (data != null) myList = (AnimeList)data.getSerializableExtra(AnimeListInfoActivity.RETURNED_LIST);
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
