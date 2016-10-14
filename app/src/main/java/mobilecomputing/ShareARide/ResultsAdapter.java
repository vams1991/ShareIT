package mobilecomputing.ShareARide;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by vams1991 on 11/13/2015.
 */
public class ResultsAdapter extends ArrayAdapter<ParseObject> {
    String TAG = ResultsAdapter.class.getSimpleName();
    private List<ParseObject> userList;
    TextView tvUName;
    TextView tvPhNo;
    TextView tvUAddress;
    Context context;
    Button btnRate;
    Button btnViewRate;
    Button btnReserve;
    String username;
    ParseUser currentUser = ParseUser.getCurrentUser();
    public ResultsAdapter(Context context, List<ParseObject> userList) {
        super(context, -1, userList);
        this.userList = userList;
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.list_results, parent, false);
        tvUName = (TextView) rowView.findViewById(R.id.tv_uname);
        tvPhNo = (TextView) rowView.findViewById(R.id.tv_phno);
        tvUAddress = (TextView) rowView.findViewById(R.id.tv_uaddress);
        btnRate = (Button) rowView.findViewById(R.id.btnrate);
        btnViewRate = (Button)rowView.findViewById(R.id.btnviewrate);
        username = userList.get(position).getString("username");
        btnReserve = (Button) rowView.findViewById(R.id.btn_reserve);
        rowView.setTag(username);
        tvUName.setText(userList.get(position).get("username").toString());
        tvPhNo.setText(userList.get(position).get("Phone").toString());
        tvUAddress.setText(userList.get(position).get("Address").toString());

        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Rate.class);
                Log.d(TAG, username);
                intent.putExtra("username", rowView.getTag().toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                rowView.getContext().startActivity(intent);
            }
        });

        btnViewRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RatingsDisplay.class);
                intent.putExtra("username", rowView.getTag().toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                rowView.getContext().startActivity(intent);
            }
        });

        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnReserve.getText().toString().equals("RESERVE")) {
                    btnReserve.setText("CANCEL");
                    notifyDataSetChanged();
                } else {
                    btnReserve.setText("RESERVE");
                }
            }
        });

        return rowView;
    }

    @Override
    public int getCount() {
        return userList.size();
    }
}
