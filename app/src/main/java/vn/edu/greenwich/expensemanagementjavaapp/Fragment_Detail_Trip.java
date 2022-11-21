package vn.edu.greenwich.expensemanagementjavaapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import vn.edu.greenwich.expensemanagementjavaapp.Adapters.Adapter_Expense;
import vn.edu.greenwich.expensemanagementjavaapp.Database.Expense;
import vn.edu.greenwich.expensemanagementjavaapp.Database.Expense_CRUD;
import vn.edu.greenwich.expensemanagementjavaapp.Database.Trip;
import vn.edu.greenwich.expensemanagementjavaapp.Database.Trip_CRUD;
import vn.edu.greenwich.expensemanagementjavaapp.Helpers.Date_Time_Validator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Detail_Trip#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Detail_Trip extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Detail_Trip() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Detail_Trip.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Detail_Trip newInstance(String param1, String param2) {
        Fragment_Detail_Trip fragment = new Fragment_Detail_Trip();
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

    TextView dtName;
    TextView dtDestination;
    TextView dtDate;
    TextView dtDescription;
    TextView dtNote;
    TextView dtTopic;
    TextView dtRiskAssessment;
    ListView lvExpenses;
    TextView tvShowExpenses;
    ImageView dtImageTrip;


    int IdTrip;
    Trip dataTrip;
    String selectedTypeExpense = "Travel";
    ArrayList<Expense> listExpenses = new ArrayList<>();
    Adapter_Expense expenseAdapter;
    boolean isShowExpenses = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Get view
        View rootView = inflater.inflate(R.layout.fragment_detail_trip, container, false);

        dtName = rootView.findViewById(R.id.dtName);
        dtDestination = rootView.findViewById(R.id.dtDestination);
        dtDate = rootView.findViewById(R.id.dtDate);
        dtDescription = rootView.findViewById(R.id.dtDescription);
        dtNote = rootView.findViewById(R.id.dtNote);
        dtTopic = rootView.findViewById(R.id.dtTopic);
        dtRiskAssessment = rootView.findViewById(R.id.dtRiskAssessment);
        dtImageTrip = rootView.findViewById(R.id.dtImage);
        lvExpenses = rootView.findViewById(R.id.lvExpenses);
        tvShowExpenses = rootView.findViewById(R.id.tvShowExpenses);

        //Get data Trip from bundle
        Bundle bundle = this.getArguments();
        IdTrip = bundle.getInt("Id");

        //Get detail Trip
        dataTrip = Trip_CRUD.getDetail(getContext(), IdTrip);

        //Set show data
        dtName.setText(dataTrip.getName());
        dtDestination.setText(dataTrip.getDestination());
        dtDate.setText(dataTrip.getDate());
        dtDescription.setText(dataTrip.getDescription());
        dtNote.setText(dataTrip.getNote());
        dtTopic.setText(dataTrip.getTopic());
        if(!TextUtils.isEmpty(dataTrip.getImage())){
            dtImageTrip.setImageURI(Uri.parse(dataTrip.getImage()));
        };
        if(dataTrip.getRequiredRiskAssessment() > 0){
            dtRiskAssessment.setText("Yes");
        } else  {
            dtRiskAssessment.setText("No");
        }

        //Set adapter list expenses
        if(isShowExpenses){
            listExpenses = Expense_CRUD.getExpensesByTrip(getContext(), IdTrip);
        }

        expenseAdapter = new Adapter_Expense(getContext(), R.layout.adapter_item_expense, listExpenses);
        lvExpenses.setAdapter(expenseAdapter);

        //handle show expenses
        tvShowExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShowExpenses){
                    listExpenses.addAll(Expense_CRUD.getExpensesByTrip(getContext(), IdTrip));
                    expenseAdapter.notifyDataSetChanged();
                    isShowExpenses = true;
                    tvShowExpenses.setText("Click to hide expenses");
                } else {
                    listExpenses.clear();
                    expenseAdapter.notifyDataSetChanged();
                    isShowExpenses = false;
                    tvShowExpenses.setText("Click to show expenses");
                }
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //inflate menu
        inflater.inflate(R.menu.setting_trip_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //handle menu item clicks
        int id = item.getItemId();

        switch (id){
            case  R.id.UpdateTrip:
                //set data bundle to pass to detail fragment
                Bundle bundle = new Bundle();
                bundle.putInt("Id", IdTrip);

                //Redirect To DetailTrip
                Navigation.findNavController(getView()).navigate(R.id.action_fragment_Detail_Trip_to_fragment_Update_Trip,bundle);
                break;
            case R.id.DeleteTrip:
                if(Trip_CRUD.delete(getContext(), IdTrip)){
                    Toast.makeText(getContext(), "Delete success!", Toast.LENGTH_SHORT).show();
                    //Redirect To list trip fragment
                    Navigation.findNavController(getView()).navigate(R.id.fragment_List_Trips);
                }
                break;
            case R.id.AddExpenses:
                showDialogAddExpenses();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private  void showDialogAddExpenses(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_expense, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        EditText edtDateExpense = view.findViewById(R.id.edtDateExpense);
        EditText edtTimeExpense = view.findViewById(R.id.edtTimeExpense);
        EditText edtAmountExpense = view.findViewById(R.id.edtAmountExpenses);
        EditText edtAdditionalComments = view.findViewById(R.id.edtAdditionalComments);
        EditText edtLocation = view.findViewById(R.id.edtLocation);
        Button btnAddExpenses = view.findViewById(R.id.btnAddExpenses);
        Spinner spinnerTypeExpense = view.findViewById(R.id.spinnerTypeExpense);

        //Set text current location
        edtLocation.setText(MainActivity.textLocation);

        //Set adapter for spinner to show options select type for expense
        ArrayAdapter<CharSequence> adapterTypesExpense = ArrayAdapter.createFromResource(getContext(), R.array.TypeOptionsExpense, android.R.layout.simple_spinner_item);
        adapterTypesExpense.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeExpense.setAdapter(adapterTypesExpense);

        //handle click item of spinner type of expense
        spinnerTypeExpense.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                selectedTypeExpense = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int mins = calendar.get(Calendar.MINUTE);

        edtDateExpense.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            month = month+1;
                            String date = day + "/" + month + "/" + year;
                            edtDateExpense.setText(date);
                            edtDateExpense.setError(null);
                        }
                    }, year, month, day);
                    datePickerDialog.show();
                }
            }
        });

        edtTimeExpense.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            calendar.set(Calendar.MINUTE, minute);
                            calendar.setTimeZone(TimeZone.getDefault());
                            SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
                            String time = format.format(calendar.getTime());
                            edtTimeExpense.setText(time);
                        }
                    }, hours, mins, false);
                    timePickerDialog.show();
                }
            }
        });

        btnAddExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValidData = true;

                if(TextUtils.isEmpty(edtDateExpense.getText().toString())){
                    edtDateExpense.setError("Date must not empty");
                    isValidData = false;
                } else {
                    boolean isValidDate = Date_Time_Validator.isValidDate(edtDateExpense.getText().toString());
                    if(isValidDate) {
                        edtDateExpense.setError(null);
                    } else  {
                        edtDateExpense.setError("Date not match format dd/mm/yyyy");
                    }
                }

                if(TextUtils.isEmpty(edtTimeExpense.getText().toString())){
                    edtTimeExpense.setError("Time must not empty");
                    isValidData = false;
                } else {
                    boolean isValidTime = Date_Time_Validator.isValidTime(edtTimeExpense.getText().toString());
                    if(isValidTime) {
                        edtTimeExpense.setError(null);
                    } else  {
                        edtTimeExpense.setError("Time not match format hh:mm a");
                    }
                }

                if(TextUtils.isEmpty(edtAmountExpense.getText().toString())){
                    edtAmountExpense.setError("Amount must not empty");
                    isValidData = false;
                } else {
                    edtAmountExpense.setError(null);
                }

                if(TextUtils.isEmpty(edtLocation.getText().toString())){
                    edtLocation.setError("Location must not empty");
                    isValidData = false;
                } else {
                    edtAdditionalComments.setError(null);
                }

                if(isValidData){
                    String dataDateExpense = edtDateExpense.getText().toString();
                    String dataTimeExpense = edtTimeExpense.getText().toString();
                    int dataAmountExpense = Integer.parseInt(edtAmountExpense.getText().toString());
                    String dataAdditionalComments = edtAdditionalComments.getText().toString();
                    String location = edtLocation.getText().toString();

                    if(Expense_CRUD.insert(getContext(),
                            selectedTypeExpense,
                            dataDateExpense,
                            dataTimeExpense,
                            dataAmountExpense,
                            dataAdditionalComments,
                            location,
                            IdTrip)){
                        Toast.makeText(getContext(), "Insert data success!", Toast.LENGTH_SHORT).show();
                        if(isShowExpenses){
                            listExpenses.clear();
                            listExpenses.addAll(Expense_CRUD.getExpensesByTrip(getContext(), IdTrip));
                            expenseAdapter.notifyDataSetChanged();
                        }
                        dialog.dismiss();
                    } else  {
                        Toast.makeText(getContext(), "Insert data false!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Data not valid", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}