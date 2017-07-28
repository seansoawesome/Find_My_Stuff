package seanchen.find_my_stuff;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class pictureTime extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_time);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = "TO-DO: do the snappy snappy and save the snappy snappy";

        // Capture the layout's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.targetView);
        textView.setText(message);
    }
}
