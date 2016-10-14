package mobilecomputing.ShareARide;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by vams1991 on 11/18/2015.
 */
public class ReviewsAdapter extends ArrayAdapter<ParseObject> {
    String TAG = ReviewsAdapter.class.getSimpleName();
    private List<ParseObject> reviewsList;
    Context context;
    TextView tvRating;
    TextView tvReview;

    public ReviewsAdapter(Context context, List<ParseObject> reviewsList) {
        super(context, -1, reviewsList);
        this.reviewsList = reviewsList;
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.list_reviews, parent, false);
        tvRating = (TextView)rowView.findViewById(R.id.tvrating);
        tvReview = (TextView)rowView.findViewById(R.id.tvreview);
        Log.d(TAG, rowView.toString());
        Log.d(TAG, reviewsList.get(position).getString("RATING"));
        Log.d(TAG, reviewsList.get(position).getString("REVIEW"));
        tvRating.setText(reviewsList.get(position).getString("RATING"));
        tvReview.setText(reviewsList.get(position).getString("REVIEW"));

        return rowView;
    }

    @Override
    public int getCount() {
        return reviewsList.size();
    }
}
