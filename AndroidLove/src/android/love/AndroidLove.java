package android.love;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AndroidLove extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void onLoveButtonClicked(View view){
    	TextView textView = (TextView) findViewById(R.id.haikuTextView);
    	textView.setVisibility(View.VISIBLE);
    }

}
