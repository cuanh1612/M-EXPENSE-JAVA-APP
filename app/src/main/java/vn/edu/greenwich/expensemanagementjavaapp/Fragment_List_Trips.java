package vn.edu.greenwich.expensemanagementjavaapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Calendar;

import vn.edu.greenwich.expensemanagementjavaapp.Adapters.Adapter_Trip;
import vn.edu.greenwich.expensemanagementjavaapp.Database.Trip;
import vn.edu.greenwich.expensemanagementjavaapp.Database.Trip_CRUD;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_List_Trips#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_List_Trips extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_List_Trips() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_List_Trips.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_List_Trips newInstance(String param1, String param2) {
        Fragment_List_Trips fragment = new Fragment_List_Trips();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //cho phep su dụng option menu
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    SearchView svTrip;
    ListView lvTrips;
    Adapter_Trip tripAdapter;
    ArrayList<Trip> listTrips = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_list_trips, container, false);

        svTrip = rootView.findViewById(R.id.svTrip);
        lvTrips = rootView.findViewById(R.id.lvTrips);
        listTrips = Trip_CRUD.getAll(rootView.getContext());
        tripAdapter = new Adapter_Trip(getContext(), R.layout.adapter_item_trip, listTrips);

        //Set adapter
        lvTrips.setAdapter(tripAdapter);

        //Handle search
        svTrip.clearFocus();
        svTrip.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String search) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String search) {
                listTrips.clear();

                if(TextUtils.isEmpty(search)){
                    listTrips.addAll(Trip_CRUD.getAll(getContext()));
                } else {
                    listTrips.addAll(Trip_CRUD.filterList(getContext(), search, "", ""));
                }

                tripAdapter.notifyDataSetChanged();
                return true;
            }
        });

        //Handle Click item list
        lvTrips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //get data trip clicked
                Trip tripClicked = listTrips.get(i);

                //set data bundle to pass to detail fragment
                Bundle bundle = new Bundle();
                bundle.putInt("Id", tripClicked.getId());

                //Redirect To DetailTrip
                Navigation.findNavController(view).navigate(R.id.action_fragment_List_Trips_to_fragment_Detail_Trip,bundle);
            }
        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //inflate menu
        inflater.inflate(R.menu.filter_trip_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //handle menu item clicks
        int id = item.getItemId();

        switch (id){
            case  R.id.FilterTrip:
                showDialogAddFilter();
                break;
            case R.id.ClearFilterTrip:
                listTrips.clear();

                listTrips.addAll(Trip_CRUD.getAll(getContext()));

                tripAdapter.notifyDataSetChanged();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private  void showDialogAddFilter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_filter_trip, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        EditText edtFilterTripName = view.findViewById(R.id.edtFilterTripName);
        EditText edtFilterTripDate = view.findViewById(R.id.edtFilterTripDate);
        EditText edtFilterTripDestination = view.findViewById(R.id.edtFilterTripDestination);
        Button btnAddFilter = view.findViewById(R.id.btnAddFilter);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        edtFilterTripDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            month = month+1;
                            String date = day + "/" + month + "/" + year;
                            edtFilterTripDate.setText(date);
                        }
                    }, year, month, day);
                    datePickerDialog.show();
                }
            }
        });

        btnAddFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textFilterName = edtFilterTripName.getText().toString();
                String textFilterDestination = edtFilterTripDestination.getText().toString();
                String textFilterDate= edtFilterTripDate.getText().toString();

                listTrips.clear();

                listTrips.addAll(Trip_CRUD.filterList(getContext(), textFilterName,textFilterDestination, textFilterDate));

                tripAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }
        });
    }
}