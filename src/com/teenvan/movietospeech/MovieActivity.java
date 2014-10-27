package com.teenvan.movietospeech;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.inmobi.commons.InMobi;
import com.inmobi.monetization.IMBanner;
import com.squareup.picasso.Picasso;

public class MovieActivity extends Activity implements OnInitListener {
	protected TextToSpeech tts;
	protected String movieNameReceived;
	protected String r1, r2, movieDuration, mDirector, mType, movieTitle,
			movieYear, movieGenre, movieRating, recomendation,
			seriesRecomendation, directorRecomendation, movieSynopsis,
			mSpeechSpoken;
	protected TextView mSpeechTextView;
	Dialog videoTrailer;
	protected float rating;
	protected int runTime;
	protected ImageView backgroundImage;
	String apiURL = "https://www.googleapis.com/youtube/v3/search?part=id&maxResults=1&key=AIzaSyDquxQlvuJIV2_awV1Zx81YE_A-fnB4zPk&q=";
	HttpClient client;
	String ID;
	String URL = "http://www.omdbapi.com/?i=&t=";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		InMobi.initialize(this, "d12af048281f46dcae92a364a69da997");
		IMBanner banner = (IMBanner) findViewById(R.id.banner);
		banner.loadBanner();
		getActionBar().hide();
		backgroundImage = (ImageView) findViewById(R.id.backgroundImage);
		tts = new TextToSpeech(this, this);
		movieNameReceived = getIntent().getStringExtra("Title");
		if (movieNameReceived != null) {
			movieNameReceived = movieNameReceived.replace(' ', '+');
			new GetMoviesTask().execute(URL + movieNameReceived);
		}

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
				ID = getID(movieNameReceived + " trailer");
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
			Toast.makeText(MovieActivity.this, "Redirecting to Youtube....",
					Toast.LENGTH_LONG).show();
			startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.movie, menu);
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
	public void onInit(int status) {
		// TODO Auto-generated method stub
		if (status == TextToSpeech.SUCCESS) {
			tts.setLanguage(Locale.UK);
		} else {

		}
	}

	private class GetMoviesTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... uri) {
			// TODO Auto-generated method stub
			HttpClient client = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				response = client.execute(new HttpGet(uri[0]));
				StatusLine status = response.getStatusLine();
				if (status.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
				} else {
					response.getEntity().getContent().close();
					throw new IOException(status.getReasonPhrase());
				}
			} catch (Exception e) {

			}
			return responseString;
		}

		@Override
		protected void onPostExecute(String response) {

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
					mDirector = jsonResponse.getString("Director");
					mType = jsonResponse.getString("Type");
					movieGenre = jsonResponse.getString("Genre");
					movieTitle = jsonResponse.getString("Title");
					movieYear = jsonResponse.getString("Year");
					movieRating = jsonResponse.getString("imdbRating");
					if (isFloat(movieRating)) {
						rating = Float.parseFloat(movieRating);
						r1 = movieRating.substring(0, 1);
						r2 = movieRating.substring(2);
						movieRating = r1 + "point" + r2;

						if (rating > 6.0 && runTime < 150
								&& mType.equals("movie")) {
							recomendation = "It is a must watch movie for "
									+ movieGenre
									+ " lovers and it is also not too lengthy.";

						} else if (rating > 6.0 && runTime > 150
								&& mType.equals("movie")) {
							recomendation = "It is a must watch movie for "
									+ movieGenre
									+ " lovers , but , it is quite lengthy.";

						} else if (rating < 6.0 && runTime > 150
								&& mType.equals("movie")) {
							recomendation = "This is a long movie , may not be a great watch.";

						} else if (rating < 6.0 && runTime < 150
								&& mType.equals("movie")) {
							recomendation = "This may be a short movie but not a great watch.";

						} else if (rating > 6.0 && mType.equals("series")) {
							seriesRecomendation = "It is a must watch TV series for "
									+ movieGenre + " lovers.";

						} else if (rating < 6.0 && mType.equals("series")) {
							seriesRecomendation = "There are better series out there. Pass this one.";
						}
					} else {
						rating = 0;
						recomendation = "We don't have sufficient information.";

					}
					if (mDirector != null) {
						if (mDirector.equals("David Fincher")) {
							directorRecomendation = "The Director of the movie is also well known for making some excellent movies.";
						} else if (mDirector.equals("Christopher Nolan")) {
							directorRecomendation = "The Director is renowned for making movies with socio-logical and philosophical ideas , such as , Memento , prestige and many more";
						} else if (mDirector.equals("James Cameron")) {
							directorRecomendation = "The Director of the movie is popular for his other movies such as Jurrasic park and others.";
						} else if (mDirector.equals("David Lynch")) {
							directorRecomendation = "The Director of the movie is known for creating movies with weird concepts such as Mullholland drive and more.";
						} else if (mDirector.equals("Steven Spielberg")) {
							directorRecomendation = "The Director of the movie is known for creating classics such as E.T. and many more.";
						} else if (mDirector.equals("Ashutosh Gowariker")) {
							directorRecomendation = "The Director of the movie is known for creating timeless classics and this movie is no exception to that.";
						} else if (mDirector.equals("Rajkumar Hirani")) {
							directorRecomendation = "The Director of the movie has directed amazing movies such as Munnabhai M.B.B.S.";
						}
					} else {
						directorRecomendation = "";
					}

					movieSynopsis = jsonResponse.getString("Plot");
					String imageUrl = jsonResponse.getString("Poster");

					Uri mImageUri = Uri.parse(imageUrl);
					Picasso.with(MovieActivity.this).load(mImageUri)
							.into(backgroundImage);
					if (mType.equals("movie")) {
						mSpeechSpoken = (movieTitle
								+ "was released in the year " + movieYear
								+ "and has an I M D B rating of " + movieRating
								+ ".." + "Synopsis." + movieSynopsis
								+ recomendation + "." + directorRecomendation);
						mSpeechTextView.setText(movieTitle + "\n" + movieYear
								+ "\n" + rating + "\n" + movieSynopsis);
						tts.speak(mSpeechSpoken, TextToSpeech.QUEUE_FLUSH, null);
					} else {
						mSpeechSpoken = (movieTitle + "premiered in the year "
								+ movieYear + "and has an I M D B rating of "
								+ movieRating + ".." + "Synopsis."
								+ movieSynopsis + seriesRecomendation);
						mSpeechTextView.setText(movieTitle + "\n" + movieYear
								+ "\n" + rating + "\n" + movieSynopsis);
						tts.speak(mSpeechSpoken, TextToSpeech.QUEUE_FLUSH, null);
					}
					// Declare the Dialog for viewing the trailer
					videoTrailer = new Dialog(MovieActivity.this);
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
					}, 12000);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Toast.makeText(MovieActivity.this,
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
