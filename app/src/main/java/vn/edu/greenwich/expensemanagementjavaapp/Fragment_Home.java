package vn.edu.greenwich.expensemanagementjavaapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import vn.edu.greenwich.expensemanagementjavaapp.Database.Expense;
import vn.edu.greenwich.expensemanagementjavaapp.Database.Expense_CRUD;
import vn.edu.greenwich.expensemanagementjavaapp.Database.Trip;
import vn.edu.greenwich.expensemanagementjavaapp.Database.Trip_CRUD;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Home newInstance(String param1, String param2) {
        Fragment_Home fragment = new Fragment_Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private Button btnNewTrip;
    private Button btnTripList;
    private Button btnContact;
    private Button btnReset;
    private Button btnBackup;
    private BottomNavigationView bottomNavigationView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        btnNewTrip = rootView.findViewById(R.id.btnNewTrip);
        btnTripList = rootView.findViewById(R.id.btnTripList);
        btnContact = rootView.findViewById(R.id.btnContact);
        btnReset = rootView.findViewById(R.id.btnReset);
        btnBackup = rootView.findViewById(R.id.btnBackup);
        bottomNavigationView = getActivity().findViewById(R.id.bottonNavigationView);

        btnNewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNavigationView.setSelectedItemId(R.id.fragment_Add_Trip);
            }
        });

        btnTripList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNavigationView.setSelectedItemId(R.id.fragment_List_Trips);
            }
        });

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNavigationView.setSelectedItemId(R.id.fragment_Contact);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Trip_CRUD.reset(getContext());
            }
        });

        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isResetTrip = Trip_CRUD.reset(getContext());
                boolean isResetExpense = Expense_CRUD.reset(getContext());

                if(!isResetExpense && !isResetTrip){
                    Toast.makeText(getContext(), "Data is empty or has an error!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Reset data successfully!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backupFirebase();
            }
        });

        return rootView;
    }

    private void backupFirebase() {
        ArrayList<Trip> allTrips = Trip_CRUD.getAll(getContext());
        ArrayList<Expense> allExpenses = Expense_CRUD.getAll(getContext());

        if(allTrips.size() > 0 ){
            db.collection("Trip")
                    .get()
                    .addOnCompleteListener( task -> {
                        if(task.isSuccessful()){
                            //Clear data trip in firebase
                            for(QueryDocumentSnapshot document : task.getResult()){
                                document.getReference().delete();
                            }

                            //backup data trip to firebase
                            for(Trip itemTrip : allTrips){
                                db.collection("Trip").add(itemTrip);
                            }
                        } else {
                            Toast.makeText(getContext(), "Firebase get fail", Toast.LENGTH_SHORT).show();
                        }
                    });


        }

        if(allExpenses.size() > 0 ){
            db.collection("Expense")
                    .get()
                    .addOnCompleteListener( task -> {
                        if(task.isSuccessful()){
                            //Clear data expense in firebase
                            for(QueryDocumentSnapshot document : task.getResult()){
                                document.getReference().delete();
                            }

                            //backup data expense to firebase
                            for(Expense itemExpense : allExpenses){
                                db.collection("Expense").add(itemExpense);
                            }
                        } else {
                            Toast.makeText(getContext(), "Firebase get fail", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        Toast.makeText(getContext(), "Backup data to firebase successfully!", Toast.LENGTH_SHORT).show();
    }
}