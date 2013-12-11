package com.example.devanswers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	ImageView DevMainImage;
	ImageView DevMainImage2;
	// TextView LinkID;
	MyTask DevMain;
	String note;
	List<String> colors = new ArrayList<String>();
	ArrayList<String> links = new ArrayList<String>();
	int number = 0;
	Elements element;
	MenuItem shareItem;
	ProgressDialog ProgressDialog;
	private boolean parsingSuccessful = true;
	TextView NodeText;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final Animation anim = AnimationUtils.loadAnimation(this, R.anim.shake);
		NodeText = (TextView) findViewById(R.id.NodeText);

		DevMainImage = (ImageView) findViewById(R.id.DevMainImage);
		DevMainImage2 = (ImageView) findViewById(R.id.DevMainImage2);

		OnClickListener onClick = new OnClickListener() {
			public void onClick(View v) {

				if (isOnline() == false) {

					Toast toast = Toast.makeText(getApplicationContext(),
							R.string.connection, Toast.LENGTH_SHORT);
					toast.show();
				}

				else {

					DevMainImage.startAnimation(anim);
					DevMain = new MyTask();
					DevMain.execute();

				}

			}
		};

		DevMainImage2.setOnClickListener(onClick);
	}

	public void colors() {

		int i = 0;
		if (i == 0) {
			colors = new ArrayList<String>(Arrays.asList(getResources()
					.getStringArray(R.array.colors_list)));
			i++;

		}
		
		// final TextView LinkID = (TextView) findViewById(R.id.Linkid);
		RelativeLayout RelLay = (RelativeLayout) findViewById(R.id.RelativeLayout);
		Random randomcolors = new Random();
		int generatedRandomNum = randomcolors.nextInt(691);
		// LinkID.setText(Integer.toString(generatedRandomNum));
		RelLay.setBackgroundColor(Color.parseColor(colors
				.get(generatedRandomNum)));
		colors.clear();
	}

	public int links() {
		
		int i = 0;
		if (i == 0) {
		links = new ArrayList<String>(Arrays.asList(getResources()
				.getStringArray(R.array.links_list)));
			i++;
		}
		
		Random rand = new Random();
		number = rand.nextInt(450);
		return (number);
	}

	public boolean isOnline() {
		String cs = Context.CONNECTIVITY_SERVICE;
		ConnectivityManager cm = (ConnectivityManager) getSystemService(cs);
		if (cm.getActiveNetworkInfo() == null) {

			return false;
		}
		return cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.i_about:
			AlertDialog();
			return true;
		case R.id.i_share: {

			ShareActionProvider mShareActionProvider = (ShareActionProvider) shareItem
					.getActionProvider();

			String nestedUrl = getResources().getString(R.string.main_url)
					+ links.get(number);

			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setAction(Intent.ACTION_SEND);
			shareIntent.setType("text/plain");

			if (note == null)
				shareIntent
						.putExtra(
								Intent.EXTRA_TEXT,
								"Девелопер ответил."
										+ " Оригинальные цитаты доступны здесь: http://devanswers.ru/");
			else
				shareIntent.putExtra(Intent.EXTRA_TEXT, "Девелопер ответил: "
						+ note + "." + " Оригинальная цитата доступна здесь: "
						+ nestedUrl);

			mShareActionProvider.setShareIntent(shareIntent);

			return true;
		}

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		shareItem = (MenuItem) menu.findItem(R.id.i_share);

		return true;
	}

	public boolean AlertDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("О приложении DevAnswers")
				.setMessage(
						"Автор приложения - Taurusha."
								+ " Оригинальные цитаты достуны на сайте devanswers.ru."
								+ " Все права на используемый материал принадлежат его владельцу. ")
				.setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// do something if OK
					}
				});
		builder.create().show();
		return false;
	}

	class MyTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			ProgressDialog = new ProgressDialog(MainActivity.this);
			ProgressDialog.setMessage("Загрузка...");
			ProgressDialog.setIndeterminate(true);
			ProgressDialog.setCancelable(false);
			ProgressDialog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

				links();
				Document doc = null;
				doc = Jsoup.connect(
						(String) "http://devanswers.ru/a/" + links.get(number))
						.get();
				element = doc.select("h1 > a > span");
				note = element.tagName("span").text();

			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			if (parsingSuccessful == true) {
				if (ProgressDialog.isShowing()) {
					ProgressDialog.dismiss();
				}
			}

			NodeText.setText(note);
			colors();

		}
	}

}
