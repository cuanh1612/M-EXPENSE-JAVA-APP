package vn.edu.greenwich.expensemanagementjavaapp.Adapters;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.edu.greenwich.expensemanagementjavaapp.Database.Trip;
import vn.edu.greenwich.expensemanagementjavaapp.R;

public class Adapter_Trip extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Trip> tripList;

    public Adapter_Trip(Context context, int layout, ArrayList<Trip> tripList){
        this.context = context;
        this.layout = layout;
        this.tripList = tripList;
    }

    @Override
    public int getCount() {
        return tripList.size();
    }

    @Override
    public Object getItem(int position) {
        return tripList.get(position);
    }

    @Override
    public long getItemId(int position) {
        Trip tripItem = tripList.get(position);
        return tripItem.getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflate.inflate(layout, null);

        TextView dtTripNameItem = view.findViewById(R.id.dtTripNameItem);
        TextView dtTripDestinationItem = view.findViewById(R.id.dtTripDestinationItem);
        TextView dtTripTimeItem = view.findViewById(R.id.dtTripTimeItem);
        TextView dtTripRiskAssessmentItem = view.findViewById(R.id.dtTripRiskAssessmentItem);
        ImageView dtTripImageView = view.findViewById(R.id.dtTripImageTrip);

        //Set data
        Trip tripItem = tripList.get(position);
        dtTripNameItem.setText(tripItem.getName());
        dtTripDestinationItem.setText(tripItem.getDestination());
        dtTripTimeItem.setText(tripItem.getDate());
        if(tripItem.getRequiredRiskAssessment() == 1){
            dtTripRiskAssessmentItem.setText("Yes");
        } else {
            dtTripRiskAssessmentItem.setText("No");
        }
        Log.d("hoang", tripItem.getImage());
        if(!TextUtils.isEmpty(tripItem.getImage())){
            dtTripImageView.setImageURI(Uri.parse(tripItem.getImage()));
        }

        return view;
    }
}
