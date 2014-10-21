package com.teenvan.movietospeech;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MoviesListActivity extends Activity {
	private ListView moviesList;
	protected String rottenUpcomingURL = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/upcoming.json?page_limit=5&page=1&country=us&apikey=42vvmbm9nkj7u6gchdmrsunr";
	protected String rottenInURL = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json?page_limit=5&page=1&country=us&apikey=42vvmbm9nkj7u6gchdmrsunr";
	protected String rottenBoxURL = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/box_office.json?limit=5&country=us&apikey=42vvmbm9nkj7u6gchdmrsunr";
	protected String query;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		query = getIntent().getStringExtra("Query");
		if (query.equals("upcoming movies") || query.equals("Upcoming movies")
				|| query.equals("Upcoming Movies")) {
			new GetMoviesTaskRotten().execute(rottenUpcomingURL);
		} else if (query.equals("In Theatres") || query.equals("in theatres")
				|| query.equals("In theatres")) {
			new GetMoviesTaskRotten().execute(rottenInURL);
		} else {
			new GetMoviesTaskRotten().execute(rottenBoxURL);
		}

		setContentView(R.layout.activity_movies_list);

		moviesList = (ListView) findViewById(R.id.moviesListView);

	}

	private class GetMoviesTaskRotten extends AsyncTask<String, String, String> {
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
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null) {
				try {
					JSONObject jsonResponse = new JSONObject(result);

					JSONArray movies = jsonResponse.getJSONArray("movies");
					String[] movieNames = new String[movies.length()];
					String[] ratings = new String[movies.length()];
					for (int i = 0; i < movies.length(); i++) {
						JSONObject movie = movies.getJSONObject(i);
						movieNames[i] = movie.getString("title");
						JSONObject rating = movie.getJSONObject("ratings");
						ratings[i] = rating.getString("audience_score");
						// movieNames[i] = posts.getString("title");
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							MoviesListActivity.this,
							android.R.layout.simple_list_item_1, movieNames);
					moviesList.setAdapter(adapter);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.d("Test", "Failed to parse the JSON response!");
				}
			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.movies_list, menu);
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
}
