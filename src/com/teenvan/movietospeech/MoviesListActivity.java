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
	String movieData;
	String movieName;
	protected String rottenUpcomingURL = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/upcoming.json?page_limit=10&page=1&country=us&apikey=42vvmbm9nkj7u6gchdmrsunr";
	protected String rottenInURL = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json?page_limit=10&page=1&country=us&apikey=42vvmbm9nkj7u6gchdmrsunr";
	protected String rottenBoxURL = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/box_office.json?limit=10&country=us&apikey=42vvmbm9nkj7u6gchdmrsunr";
	protected String query;
	protected String APIkey = "24dd57acd091d95f62e3a6bc67b23f54";
	protected String discoverURL = "";
	protected int mFlag = 0;

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
		} else if (query.equals("Brad")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?with_people=287,819&sort_by=vote_average.desc&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("Tom")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?with_genres=878&with_cast=500&sort_by=vote_average.des&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("Will")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?with_genres=35&with_cast=23659&sort_by=revenue.desc&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("popular movies in 2010")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?primary_release_year=2010&sort_by=popularity.desc&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("popular movies in 2011")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?primary_release_year=2011&sort_by=popularity.desc&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("popular movies in 2012")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?primary_release_year=2012&sort_by=popularity.desc&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("popular movies in 2013")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?primary_release_year=2013&sort_by=popularity.desc&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("popular movies in 2014")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?primary_release_year=2014&sort_by=popularity.desc&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("david")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?with_people=7467&sort_by=popularity.desc&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("brad pitt popular movies")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?with_people=287&sort_by=popularity.desc&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("drama")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?with_genres=18&primary_release_year=2014&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("action")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?with_genres=28&primary_release_year=2014&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("adventure")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?with_genres=12&primary_release_year=2014&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("animation")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?with_genres=16&primary_release_year=2014&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("comedy")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?with_genres=35&primary_release_year=2014&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("crime")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?with_genres=80&primary_release_year=2014&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("disaster")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?with_genres=105&primary_release_year=2014&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("documentary")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?with_genres=99&primary_release_year=2014&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("fantasy")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?with_genres=14&primary_release_year=2014&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("horror")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?with_genres=27&primary_release_year=2014&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("romance")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?with_genres=10749&primary_release_year=2014&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("science")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?with_genres=878&primary_release_year=2014&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("thriller")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?with_genres=53&primary_release_year=2014&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("suspense")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?with_genres=10748&primary_release_year=2014&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("kids")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?certification_country=US&certification.lte=G&sort_by=popularity.desc&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("edward")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?with_people=819&sort_by=popularity.desc&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("best drama")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?with_genres=18&sort_by=vote_average.desc&vote_count.gte=10&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("best thriller")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?with_genres=53&sort_by=vote_average.desc&vote_count.gte=10&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("best suspense")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?with_genres=10748&sort_by=vote_average.desc&vote_count.gte=10&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("best horror")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?with_genres=27&sort_by=vote_average.desc&vote_count.gte=10&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("liam")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/movie?sort_by=revenue.desc&with_cast=3896&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("intheatres")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/movie/now_playing?api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("popular tv")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/tv?sort_by=popularity.des&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("best drama series")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/tv?with_genres=18&primary_release_year=2014&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("best action series")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/tv?with_genres=10759&primary_release_year=2014&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("best animation series")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/tv?with_genres=16&primary_release_year=2014&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("best mystery series")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/tv?with_genres=9648&primary_release_year=2014&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("best documentary series")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/tv?with_genres=99&primary_release_year=2014&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("best comedy series")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/discover/tv?with_genres=35&primary_release_year=2014&api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("upcoming movies")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/movie/upcoming?api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("tv airing today")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/tv/airing_today?api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("on air tv")) {
			new GetMoviesTaskDiscover()
					.execute("http://api.themoviedb.org/3/tv/on_the_air?api_key=24dd57acd091d95f62e3a6bc67b23f54");
		} else if (query.equals("tom")) {
			new GetMoviesTaskDiscover().execute("");
		}

		else {
			new GetMoviesTaskRotten().execute(rottenBoxURL);
		}

		setContentView(R.layout.activity_movies_list);

	}

	private class GetMoviesTaskDiscover extends
			AsyncTask<String, String, String> {
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

			super.onPostExecute(result);
			if (result != null) {
				movieData = result;
				mFlag = 1;
				try {
					JSONObject jsonResponse = new JSONObject(result);
					JSONArray movies = jsonResponse.getJSONArray("results");
					String[] movieNames = new String[movies.length()];
					for (int i = 0; i < movies.length(); i++) {
						JSONObject movie = movies.getJSONObject(i);
						movieNames[i] = movie.getString("title");
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							MoviesListActivity.this, R.layout.custom_textview,
							movieNames);
					setListAdapter(adapter);
				} catch (JSONException e) {

					Log.d("Test", "Failed to parse the JSON response!");
				}
			}

		}
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
				mFlag = 2;
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
			if (mFlag == 2) {
				JSONObject jsonresponse = new JSONObject(blogData);
				JSONArray movies = jsonresponse.getJSONArray("movies");
				JSONObject movie = movies.getJSONObject(position);
				movieName = movie.getString("title");
			} else if (mFlag == 1) {
				JSONObject jsonResponse = new JSONObject(movieData);
				JSONArray movies = jsonResponse.getJSONArray("results");
				JSONObject movie = movies.getJSONObject(position);
				movieName = movie.getString("title");
			}
			Intent intent = new Intent(MoviesListActivity.this,
					MovieActivity.class);
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
