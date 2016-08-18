package com.dk.boosted;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class IGN extends AppCompatActivity {
    ProgressDialog pd;
    public enum States {
        findingSummoner, findingPatch, findingPlayers, findingFull,
        gettingFull
    }
    States state;
    String patch;
    String[] championIDs = new String[10];
    String[] champFulls = new String[10];
    JSONArray participantsInfo;
    int champCounter = 0;

    //Finds JSON data from URL
    //Credit: http://stackoverflow.com/questions/33229869/get-json-data-from-url-using-android
    private class JsonTask extends AsyncTask<String, String, String> {

//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            pd = new ProgressDialog(IGN.this);
//            pd.setMessage("Please wait");
//            pd.setCancelable(false);
//            pd.show();
//        }

        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                }
                //might not need
//                if (state == States.findingFull) {
//                    JSONObject response= new JSONObject(buffer.toString());
//                    JSONObject image = response.getJSONObject("image");
//                    String full  = image.getString("full");
//                    Log.d("FULL", full);
////                    champCounter++;
//                }
                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            catch (JSONException e) {
//                e.printStackTrace();
//            }
            finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            if (pd.isShowing()){
//                pd.dismiss();
//            }
            try {
                Intent intent = new Intent(IGN.this, DisplaySummoners.class);
                String id;
                //find the current patch
                if (state == States.findingPatch) {
                    JSONArray versions = new JSONArray(result);
                    patch = versions.get(0).toString();
                    Log.d("PATCH", patch);
                //find the data for the summoner entered
                } else if (state == States.findingSummoner) {
                    JSONObject response = new JSONObject(result);
                    EditText summonerSearchEdit = (EditText) findViewById(R.id.summonerSearchEdit);
                    //get summoner id to perform next JSON task
                    id = response.getJSONObject(summonerSearchEdit.getText().toString()).getString("id");
                    Log.d("TEST", result);
                    Log.d("ID", id);
                    state = States.findingPlayers;
                    //find summoner current game data
                    new JsonTask().execute("https://na.api.pvp.net/observer-mode/rest/consumer/getSpectatorGameInfo/NA1/" +
                            id + "?api_key=RGAPI-0F72C736-8550-4761-923E-4D68D08508D8");
                //finds current game data to get each players data
                } else if (state == States.findingPlayers) {
                    JSONObject response = new JSONObject(result);
                    participantsInfo = response.getJSONArray("participants");
                    for (int i = 0; i < participantsInfo.length(); i++) {
                        JSONObject participantInfo = participantsInfo.getJSONObject(i);
                        championIDs[i] = participantInfo.getString("championId");
                        Log.d("CHAMPION ID", championIDs[i]);
                    }
                    state = States.findingFull;
//                    find first champion image
                    new JsonTask().execute("https://global.api.pvp.net/api/lol/static-data/na/v1.2/champion/" +
                            championIDs[champCounter] + "?champData=image&api_key=RGAPI-0F72C736-8550-4761-923E-4D68D08508D8");
//                    champCounter++;
                } else if (state == States.findingFull) {
                    JSONObject response= new JSONObject(result);
                    JSONObject image = response.getJSONObject("image");
                    String full  = image.getString("full");
                    Log.d("FULL1", full + ": " + champCounter);
                    champFulls[champCounter] = full;
                    champCounter++;
                    if (champCounter < 10) {
                        //find images for rest of the champions
                        new JsonTask().execute("https://global.api.pvp.net/api/lol/static-data/na/v1.2/champion/" +
                                championIDs[champCounter] + "?champData=image&api_key=RGAPI-0F72C736-8550-4761-923E-4D68D08508D8");
//                        champCounter++;
                    } else {
                        champCounter = 0;
                        intent.putExtra("champFulls", champFulls);
                        pd.dismiss();
                        startActivity(intent);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
                //Alert Dialog for when invalid summoner name is entered
                if (state == States.findingSummoner) {
                    pd.dismiss();
                    new AlertDialog.Builder(IGN.this)
                            .setTitle("Summoner Name Not Found")
                            .setMessage("Please enter a valid summoner who is currently in a game.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Do nothing
                                }
                            })
                            .show();
                //Alert Dialog for when summoner is not in a game
                } else if (state == States.findingPlayers) {
                    pd.dismiss();
                    state = States.findingSummoner;
                    new AlertDialog.Builder(IGN.this)
                            .setTitle("Summoner Game Not Found")
                            .setMessage("Please enter a valid summoner who is currently in a game.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Do nothing
                                }
                            })
                            .show();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ign);
        state = States.findingPatch;
        //search box for finding a summoner
        final EditText summonerSearchEdit = (EditText) findViewById(R.id.summonerSearchEdit);
        //find current patch
        new JsonTask().execute("https://global.api.pvp.net/api/lol/static-data/na/v1.2/versions?api_key=RGAPI-0F72C736-8550-4761-923E-4D68D08508D8");
        //Action for when summoner name is entered
        summonerSearchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    state = States.findingSummoner;
                    pd = new ProgressDialog(IGN.this);
                    pd.setMessage("Please wait");
                    pd.setCancelable(false);
                    pd.show();
                    new JsonTask().execute("https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/" +
                        summonerSearchEdit.getText() + "?api_key=RGAPI-0F72C736-8550-4761-923E-4D68D08508D8");
//                    intent.putExtra("SummonerName", summonerSearchEdit.getText());
                    return true;
                }
                return false;
            }
        });
    }
}
