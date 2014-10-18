package com.example.movietospeech;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class MainActivity extends Activity implements OnClickListener,
		TextToSpeech.OnInitListener {
	protected Button mSpeechButton;
	protected TextView mMovieNameText;
	protected TextView mMovieRatingText;
	protected TextView mMovieSynopsis;
	protected TextView mSpeechTextView;
	protected ArrayList<String> result;
	protected String movieName = "fight%20club";
	protected JSONObject movieData;
	protected String movieTitle;
	protected String movieYear;
	protected String movieRating;
	protected String movieSynopsis;
	String speech;
	String URL = "http://www.omdbapi.com/?i=&t=";
	String rottenURL = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?&page_limit=1&page=1&apikey=42vvmbm9nkj7u6gchdmrsunr&q=";
	private TextToSpeech tts;
	boolean mTrue;
	HttpClient client;
	String apiURL = "https://www.googleapis.com/youtube/v3/search?part=id&maxResults=1&key=AIzaSyDquxQlvuJIV2_awV1Zx81YE_A-fnB4zPk&q=";
	String youtubeAPIKey = "AIzaSyDquxQlvuJIV2_awV1Zx81YE_A-fnB4zPk";
	String ID, vid;
	String speech2;
	String movieDuration;
	protected TextView mHint;
	protected String mSpeechSpoken;
	int runTime;
	protected String movieGenre;
	protected float rating;
	protected String recomendation;
	protected String r1, r2;
	protected ImageView backgroundImage;
	Dialog videoTrailer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSpeechButton = (Button) findViewById(R.id.speechButton);
		client = new DefaultHttpClient();
		mSpeechTextView = (TextView) findViewById(R.id.speechText);
		backgroundImage = (ImageView) findViewById(R.id.backgroundImage);

		// Hide the ActionBar
		getActionBar().hide();
		tts = new TextToSpeech(this, this);
		mSpeechButton.setOnClickListener(this);

	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			tts.setLanguage(Locale.US);
			// Toast.makeText(MainActivity.this,
			//
			// "Text-To-Speech engine is initialized",
			// Toast.LENGTH_LONG).show();

		}

		else if (status == TextToSpeech.ERROR) {

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something!");
		try {

			startActivityForResult(intent, 100);
		} catch (ActivityNotFoundException a) {
			Toast.makeText(getApplicationContext(), "Speech is not supported",
					Toast.LENGTH_SHORT).show();
		}
		if (videoTrailer != null && videoTrailer.isShowing()) {
			videoTrailer.dismiss();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		tts.shutdown();
	}

	public String getID(String name) throws ClientProtocolException,
			JSONException, IOException {

		StringBuilder url = new StringBuilder(apiURL);
		url.append(name);
		HttpGet get = new HttpGet(url.toString());
		HttpResponse r = client.execute(get);
		int status = r.getStatusLine().getStatusCode();
		if (status == 200) // if status is ok
		{
			HttpEntity e = r.getEntity();
			String data = EntityUtils.toString(e);
			JSONObject res = new JSONObject(data);
			JSONArray item = res.getJSONArray("items");
			JSONObject first = item.getJSONObject(0);
			JSONObject Id = first.getJSONObject("id");
			String videoid = Id.getString("videoId");
			Log.d("Show", videoid);
			return videoid;
		} else {
			Toast.makeText(this, "Oops error! Please try again.",
					Toast.LENGTH_LONG).show();
			return null;
		}
	}

	public class read extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			// TODO Auto-generated method stub
			try {
				ID = getID(speech2);
				return null;
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("vnd.youtube:" + ID));
			Toast.makeText(MainActivity.this, "Redirecting to Youtube....",
					Toast.LENGTH_LONG).show();
			startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 100 && resultCode == RESULT_OK) {

			result = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			speech = result.get(0).toString();
			speech2 = speech + " " + "trailer";
			speech = speech.replace(' ', '+');
			speech2 = speech2.replace(' ', '+');
			new GetMoviesTask().execute(URL + speech);

			// mSpeechTextView.setText(result.get(0).substring(0, 1)
			// .toUpperCase()
			// + result.get(0).substring(1));
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private class GetMoviesTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... uri) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				// make a HTTP request
				response = httpclient.execute(new HttpGet(uri[0]));
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					// request successful - read the response and close the
					// connection
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
				} else {
					// request failed - close the connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {
				Log.d("Test", "Couldn't make a successful request!");
			}
			return responseString;

		}

		@Override
		protected void onPostExecute(String response) {
			// TODO Auto-generated method stub
			super.onPostExecute(response);
			if (response != null) {
				try {
					JSONObject jsonResponse = new JSONObject(response);
					// JSONObject movie = jsonResponse.getJSONObject("Title");
					runTime = 0;
					movieDuration = jsonResponse.getString("Runtime").trim();
					if (isInt(movieDuration.substring(0, 3))) {
						runTime = Integer.parseInt(movieDuration
								.substring(0, 3));

					}
					movieGenre = jsonResponse.getString("Genre");
					movieTitle = jsonResponse.getString("Title");
					movieYear = jsonResponse.getString("Year");
					movieRating = jsonResponse.getString("imdbRating");
					if (isFloat(movieRating)) {
						rating = Float.parseFloat(movieRating);
						r1 = movieRating.substring(0, 1);
						r2 = movieRating.substring(2);
						movieRating = r1 + "point" + r2;

						if (rating > 6.0 && runTime < 150) {
							recomendation = "It is a must watch movie for "
									+ movieGenre
									+ " lovers and it is also not too lengthy.";
						} else if (rating > 6.0 && runTime > 150) {
							recomendation = "It is a must watch movie for "
									+ movieGenre
									+ " lovers , but , it is quite lengthy.";
						} else if (rating < 6.0 && runTime > 150) {
							recomendation = "This is long movie , may not be a great watch.";
						} else if (rating < 6.0 && runTime < 150) {
							recomendation = "This may be a short movie but not a great watch.";
						}
					} else {
						rating = 0;
						recomendation = "We don't have sufficient information.";

					}
					movieSynopsis = jsonResponse.getString("Plot");
					String imageUrl = jsonResponse.getString("Poster");

					Uri mImageUri = Uri.parse(imageUrl);
					Picasso.with(MainActivity.this).load(mImageUri)
							.into(backgroundImage);
					mSpeechSpoken = (movieTitle + "was released in the year "
							+ movieYear + "and has an I M D B rating of "
							+ movieRating + ".." + "Synopsis." + movieSynopsis
							+ recomendation + movieDuration);
					mSpeechTextView.setText(movieTitle + "\n" + movieYear
							+ "\n" + rating + "\n" + movieSynopsis);
					tts.speak(mSpeechSpoken, TextToSpeech.QUEUE_FLUSH, null);

					// Declare the Dialog for viewing the trailer
					videoTrailer = new Dialog(MainActivity.this);
					videoTrailer.requestWindowFeature(Window.FEATURE_NO_TITLE);
					videoTrailer.setContentView(R.layout.dialoglayout);
					Button watchVideoButton = (Button) videoTrailer
							.findViewById(R.id.watchVideoButton);
					watchVideoButton
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									new read().execute();
								}
							});
					Button dismissButton = (Button) videoTrailer
							.findViewById(R.id.dismissDialogButton);
					dismissButton
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									videoTrailer.dismiss();
								}
							});
					// Delay the displaying of the Dialog by 2000 milliseconds
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							videoTrailer.show();
						}
					}, 13000);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Toast.makeText(MainActivity.this,
						"Oops error! Please try again.", Toast.LENGTH_LONG)
						.show();
			}

		}
	}

	boolean isFloat(String str) {
		try {
			Float.parseFloat(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	boolean isInt(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
