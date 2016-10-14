package mobilecomputing.ShareARide;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

/**
 * Created by vams1991 on 11/14/2015.
 */
public class DisplayResults extends Activity {

    ListView resultsList;
    ResultsAdapter resultsAdapter;
    //ArrayList<ParseObject> relatedUsers;
    private static final String TAG = DisplayResults.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(EnterItinerary.relatedUsers.size() == 0){
            setContentView(R.layout.noresults);
        } else {
            setContentView(R.layout.activity_displayresults);
            resultsList = (ListView)findViewById(R.id.lv_results);
            if(resultsAdapter == null) resultsAdapter = new ResultsAdapter(getApplicationContext(), EnterItinerary.relatedUsers);
            resultsList.setAdapter(resultsAdapter);
        }
        //Bundle bundle = getIntent().getExtras();
        //relatedUsers = (ArrayList<ParseObject>)bundle.getSerializable("Related_Users");
        //Log.d(TAG, relatedUsers.toString());
    }
}
