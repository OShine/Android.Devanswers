package com.example.devanswers;

import java.io.IOException;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	ImageView DevMainImage;
	WebView Webview;
	MyTask DevMain;
	String note = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	
		final TextView NodeText = (TextView)findViewById(R.id.NodeText);
		DevMainImage = (ImageView) findViewById(R.id.DevMainImage);
	

		OnClickListener onClick = new OnClickListener() {
			public void onClick(View v) {
 
				DevMain = new MyTask();
				DevMain.execute();
				NodeText.setText(note);
				
			}
		};

		DevMainImage.setOnClickListener(onClick);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	class MyTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		//	DevMainImage.setEnabled(false);
		}

		@Override
		protected Void doInBackground(Void... params) {
								
			try {				
				
				char[] alphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','1','2','3','4','5','6','7','8','9','0'};
		        String result = "";
		        Random rn = new Random();
		        
		        for(int i = 0; i < 2; i++) {        
		            int pos = rn.nextInt(alphabet.length);
		            result += alphabet[pos];
		        }
				
	            Document doc = null;
	          //  doc = Jsoup.connect("http://devanswers.ru/a/Ee").get();
	            doc = Jsoup.connect("http://devanswers.ru/a/" + result).get();
	            Elements element = doc.select("h1 > a > span");
	            note = element.tagName("span").text();
	            
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			
			return null;
				
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		//	DevMainImage.setEnabled(true);
		}
	}

}
