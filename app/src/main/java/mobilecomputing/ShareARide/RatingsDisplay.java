package mobilecomputing.ShareARide;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Arrays;
import java.util.List;

/**
 * Created by vams1991 on 11/18/2015.
 */
public class RatingsDisplay extends Activity {
    String TAG = RatingsDisplay.class.getSimpleName();
    ReviewsAdapter reviewsAdapter;
    ListView lvRatings;
    String username;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratingsdisplay);
        username = getIntent().getStringExtra("username");
        lvRatings = (ListView)findViewById(R.id.lvratings);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("RATINGSANDREVIEWS");
        query.whereEqualTo("USERNAME", username);
        query.selectKeys(Arrays.asList("RATING", "REVIEW"));
        List<ParseObject> reviewsList = null;
        try {
            reviewsList = query.find();
        } catch(Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, reviewsList.toString());
        if(reviewsAdapter == null) reviewsAdapter = new ReviewsAdapter(getApplicationContext(), reviewsList);
        lvRatings.setAdapter(reviewsAdapter);
    }
}
