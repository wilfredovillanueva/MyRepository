package com.nasadailyimage;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NasaImageDisplay extends Activity {
	
	private IotdHandler iotdHandler;
	ProgressDialog dialog;
	Handler handler;
	Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        handler = new Handler();
        refreshFromFeed();
    }
    
    private void refreshFromFeed(){
    	
    	dialog = ProgressDialog.show(this, "Loading", 
    			"Loading the image of the Day");
    	
    	Thread th = new Thread(){
    		
    		public void run(){
    			if (iotdHandler == null){
    				iotdHandler = new IotdHandler();
    			}
    			
    			iotdHandler.processFeed();
    			
    			image = iotdHandler.getImage();
    			
    			handler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						resetDisplay(iotdHandler.getTitle(), 
								iotdHandler.getDate(), 
								iotdHandler.getImage(), 
		    	        		iotdHandler.getDescription());
						dialog.dismiss();
					}
				});
    		}
    	};
  
        th.start();
        
    }
    
    public void onSetWallpaper(View view){
    	Thread th = new Thread(){
    		public void run(){
    			WallpaperManager wallpaperManager = 
    					WallpaperManager.getInstance(NasaImageDisplay.this);
    			
    			try{
    				wallpaperManager.setBitmap(image);
    				handler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(NasaImageDisplay.this, 
									"Wallpaper set", Toast.LENGTH_SHORT).show();
						}
					});
    			}
    			
    			catch(Exception e){
    				e.printStackTrace();
    				handler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(NasaImageDisplay.this, 
									"Error setting wallpaper", Toast.LENGTH_SHORT).show();
						}
					});
    			}
    		}
    	};
    	th.start();
    }
    
    public void onRefresh(View view){
    	refreshFromFeed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nasa_image_display, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.main, container, false);
            return rootView;
        }
    }
    
    private void resetDisplay(String title, String date, Bitmap image, StringBuffer description){
    	TextView titleView = (TextView) findViewById(R.id.imageTitle);
    	TextView dateView = (TextView) findViewById(R.id.imageDate);
    	ImageView imageView = (ImageView) findViewById(R.id.imageDisplay);
    	TextView descriptionView = (TextView) findViewById(R.id.imageDescription);
    	titleView.setText(title);
    	dateView.setText(date);
    	imageView.setImageBitmap(image);
    	descriptionView.setText(description);
    }

}
