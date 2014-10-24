package com.navneet.movietospeech;

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
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.inmobi.commons.InMobi;
import com.inmobi.monetization.IMBanner;
import com.squareup.picasso.Picasso;
import com.teenvan.movietospeech.MoviesListActivity;
import com.teenvan.movietospeech.R;

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
	protected final JSONObject movieJsonData = null;
	protected String mType;
	protected String seriesRecomendation;
	protected String mDirector;
	protected String directorRecomendation = "";
	protected String movieNameReceived;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		InMobi.initialize(this, "d12af048281f46dcae92a364a69da997");
		IMBanner banner = (IMBanner) findViewById(R.id.banner);
		banner.loadBanner();

		mSpeechButton = (Button) findViewById(R.id.speechButton);
		client = new DefaultHttpClient();
		mSpeechTextView = (TextView) findViewById(R.id.speechText);
		backgroundImage = (ImageView) findViewById(R.id.backgroundImage);

		// Hide the ActionBar
		getActionBar().hide();
		tts = new TextToSpeech(this, this);
		mSpeechButton.setOnClickListener(this);

		movieNameReceived = getIntent().getStringExtra("Title");
		if (movieNameReceived != null) {
			movieNameReceived = movieNameReceived.replace(' ', '+');
			new GetMoviesTask().execute(URL + movieNameReceived);
		}

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

	private boolean isNetworkAvailable() {
		ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = manager.getActiveNetworkInfo();
		boolean isAvailable = false;
		if (network != null && network.isConnected()) {
			isAvailable = true;
		}
		return isAvailable;
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

			// Speech Permutations for different Search options

			if (speech.equals("upcoming movies")
					|| speech.equals("Upcoming movies")
					|| speech.equals("Upcoming Movies")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", speech);
				startActivity(intent);
			} else if (speech.equals("In theatres")
					|| speech.equals("in theatres")
					|| speech.equals("In Theatres")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", speech);
				startActivity(intent);
			} else if (speech.equals("Box Office")
					|| speech.equals("box office")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", speech);
				startActivity(intent);
			} else if (speech
					.equals("have brad pitt and edward norton acted in a movie together?")
					|| speech.equals("brad pitt and edward norton")
					|| speech
							.equals("have brad pitt and edward norton acted in a movie together")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", "Brad");
				startActivity(intent);
			} else if (speech.equals("Tom Cruise Science Fiction Movies")
					|| speech.equals("tom cruise science fiction movies")
					|| speech.equals("tom Cruise")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", "Tom");
				startActivity(intent);

			} else if (speech
					.equals("what are will ferrell's highest grossing comedies?")
					|| speech.equals("will ferrell highest grossing comedies")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", "Will");
				startActivity(intent);
			} else if (speech.equals("popular movies in 2010")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", speech);
				startActivity(intent);
			} else if (speech.equals("popular movies in 2011")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", speech);
				startActivity(intent);
			} else if (speech.equals("popular movies in 2012")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", speech);
				startActivity(intent);
			} else if (speech.equals("popular movies in 2013")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", speech);
				startActivity(intent);
			} else if (speech.equals("popular movies in 2014")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", speech);
				startActivity(intent);
			} else if (speech.equals("brad pitt popular movies")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", speech);
				startActivity(intent);
			} else if (speech.equals("david fincher popular movies")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", "david");
				startActivity(intent);
			} else if (speech.equals("edward norton popular movies")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", "Edward");
				startActivity(intent);
			} else if (speech.equals("what are the most popular kids movies")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", "kids");
				startActivity(intent);
			} else if (speech.equals("best drama movies this year")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", "drama");
				startActivity(intent);
			} else if (speech.equals("best action movies this year")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", "action");
				startActivity(intent);
			} else if (speech.equals("best adventure movies this year")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", "adventure");
				startActivity(intent);
			} else if (speech.equals("best animation movies this year")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", "animation");
				startActivity(intent);
			} else if (speech.equals("best comedy movies this year")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", "comedy");
				startActivity(intent);
			} else if (speech.equals("best crime movies this year")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", "crime");
				startActivity(intent);
			} else if (speech.equals("best disaster movies this year")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", "disaster");
				startActivity(intent);
			} else if (speech.equals("best documentary movies this year")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", "documentary");
				startActivity(intent);
			} else if (speech.equals("best fantasy movies this year")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", "fantasy");
				startActivity(intent);
			} else if (speech.equals("best horror movies this year")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", "horror");
				startActivity(intent);
			} else if (speech.equals("best romance movies this year")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", "romance");
				startActivity(intent);
			} else if (speech.equals("best science movies this year")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", "science");
				startActivity(intent);
			} else if (speech.equals("best thriller movies this year")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", "thriller");
				startActivity(intent);
			} else if (speech.equals("best suspense movies this year")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", "suspense");
				startActivity(intent);
			} else if (speech.equals("What are the best drama movies")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", "best drama");
				startActivity(intent);
			} else if (speech.equals("What are the best thriller movies")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", "best thriller");
				startActivity(intent);
			} else if (speech.equals("What are the best suspense movies")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", "best suspense");
				startActivity(intent);
			} else if (speech.equals("What are the best horror movies")) {
				Intent intent = new Intent(MainActivity.this,
						MoviesListActivity.class);
				intent.putExtra("Query", "best horror");
				startActivity(intent);
			} else {
				if (isNetworkAvailable()) {
					speech2 = speech + " " + "trailer";
					speech = speech.replace(' ', '+');
					speech2 = speech2.replace(' ', '+');

					new GetMoviesTask().execute(URL + speech);
				} else {
					Toast.makeText(
							MainActivity.this,
							"Living in stone age  ,  are you ? Connect to internet!",
							Toast.LENGTH_LONG).show();
				}
			}
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
					Picasso.with(MainActivity.this).load(mImageUri)
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
					}, 12000);

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
