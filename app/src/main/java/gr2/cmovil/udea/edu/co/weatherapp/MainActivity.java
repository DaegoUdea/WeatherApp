package gr2.cmovil.udea.edu.co.weatherapp;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    public final String TAG = getClass().getSimpleName();
    Button request;
    EditText location;
    TextView country;
    TextView city;
    TextView temp;
    TextView description;
    ImageView condIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        request = (Button)findViewById(R.id.Request);
        location = (EditText)findViewById(R.id.Location);
        country = (TextView)findViewById(R.id.Country);
        city = (TextView)findViewById(R.id.City);
        temp = (TextView)findViewById(R.id.Temp);
        description = (TextView)findViewById(R.id.Description);
        condIcon = (ImageView)findViewById(R.id.CondIcon);
        request.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        new WeatherTask().execute(location.getText().toString());
    }

    private class WeatherTask extends AsyncTask<String, Void, Void> {

        private static final String TAG = "WeatherTask";
        String data = "";
        byte[] icon;
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
        JSONObject jsonResponse;

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            // StartProgressDialog(Message)
            Dialog.setMessage("Pleasewait..");
            Dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                data = ((new WeatherHttpClient()).getWeatherData(params[0]));
                jsonResponse = new JSONObject(data);
                icon = new WeatherHttpClient().getImage(
                        jsonResponse.getJSONArray("weather").getJSONObject(0).optString("icon").toString());
            } catch (Exception ex) {
                Log.d(TAG, "Triggered Exception");
                Error = ex.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Dialog.dismiss();
            if (Error != null) {
                Log.d(TAG,"Error == null");

            } else {
                String OutputData = "";

                try {
                    OutputData = jsonResponse.getJSONObject("sys").optString("country").toString();
                    country.setText(OutputData);
                    OutputData = jsonResponse.optString("name").toString();
                    city.setText(OutputData);
                    OutputData = jsonResponse.getJSONObject("main").optString("temp").toString();
                    temp.setText(OutputData + "°C");
                    OutputData = jsonResponse.getJSONArray("weather").getJSONObject(0).optString("main").toString();
                    OutputData += "\n" + jsonResponse.getJSONArray("weather").getJSONObject(0).optString("description").toString();;
                    description.setText(OutputData);
                    if(icon != null && icon.length>0){
                        Bitmap img = BitmapFactory.decodeByteArray(icon, 0, icon.length);
                        condIcon.setImageBitmap(img);
                    }
                } catch (JSONException e) {
                    Log.d(TAG,"JSonException error");
                }
                catch (NullPointerException e){
                    Log.d(TAG,"Nullpointer error");
                }
            }
        }
    }
}
