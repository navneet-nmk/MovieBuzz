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

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.navneet.movietospeech.MainActivity;

public class MoviesListActivity extends ListActivity {
	String blogData;

	protected String rottenUpcomingURL = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/upcoming.json?page_limit=10&page=1&country=us&apikey=42vvmbm9nkj7u6gchdmrsunr";
	protected String rottenInURL = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json?page_limit=10&page=1&country=us&apikey=42vvmbm9nkj7u6gchdmrsunr";
	protected String rottenBoxURL = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/box_office.json?limit=10&country=us&apikey=42vvmbm9nkj7u6gchdmrsunr";
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
				blogData = result;
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
							MoviesListActivity.this, R.layout.custom_textview,
							movieNames);
					setListAdapter(adapter);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.d("Test", "Failed to parse the JSON response!");
				}
			}

		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		try {
			JSONObject jsonresponse = new JSONObject(blogData);
			JSONArray movies = jsonresponse.getJSONArray("movies");
			JSONObject movie = movies.getJSONObject(position);
			String movieName = movie.getString("title");
			Intent intent = new Intent(MoviesListActivity.this,
					MainActivity.class);
			intent.putExtra("Title", movieName);
			startActivity(intent);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
