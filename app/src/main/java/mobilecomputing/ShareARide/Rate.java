package mobilecomputing.ShareARide;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseObject;

/**
 * Created by vams1991 on 11/18/2015.
 */
public class Rate extends Activity {
    EditText etRate;
    EditText etReview;
    Button btnSubmit;
    String username;
    public String TAG = Rate.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        username = getIntent().getStringExtra("username");
        etRate = (EditText)findViewById(R.id.etrate);
        etReview = (EditText)findViewById(R.id.etreview);
        btnSubmit = (Button)findViewById(R.id.btnsubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etRate.getText())||TextUtils.isEmpty(etReview.getText())){
                    new AlertDialog.Builder(Rate.this)
                            .setTitle(" MISSING DETAILS")
                            .setMessage("Required Fields Missing")
                            .setNeutralButton(android.R.string.ok, null)
                            .setCancelable(false)
                            .show();
                } else {
                    ParseObject ratings = new ParseObject("RATINGSANDREVIEWS");
                    Log.d(TAG, username);
                    ratings.put("USERNAME", username);
                    ratings.put("RATING", etRate.getText().toString());
                    ratings.put("REVIEW", etReview.getText().toString());
                    ratings.saveInBackground();
                }
                onBackPressed();
            }
        });
    }
}
