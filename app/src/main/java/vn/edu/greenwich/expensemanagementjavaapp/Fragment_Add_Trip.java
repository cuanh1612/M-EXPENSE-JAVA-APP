package vn.edu.greenwich.expensemanagementjavaapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import vn.edu.greenwich.expensemanagementjavaapp.Database.Trip_CRUD;
import vn.edu.greenwich.expensemanagementjavaapp.Helpers.Date_Time_Validator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Add_Trip#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Add_Trip extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Add_Trip() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Add_Trip.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Add_Trip newInstance(String param1, String param2) {
        Fragment_Add_Trip fragment = new Fragment_Add_Trip();
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

    // Declare important variables that map to elements in the layout.
    EditText edtName;
    EditText edtDestination;
    EditText edtDate;
    EditText edtDescription;
    EditText edtNote;
    EditText edtTopic;
    Switch switchRisk;
    Button btnAdd;

    // I create variable switchRiskChecked to store data for risk assessment
    // since SQLite doesn't store boolean values so I will store 1 as true and 0 as false
    int switchRiskChecked = 0;

    //Element for take picture camera or library
    Button takePictureCameraBtn, getPictureLibraryBtn;
    ImageView imageView;
    Uri uri = Uri.parse("");

    //Create authorization codes are assigned when requesting permission from the user
    private static final int PERMISSION_CODE = 1001;
    private static final int CAPTURE_CODE = 1002;
    private final int SELECT_IMAGE_CODE = 1003;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // create rootView inflate the layout for this fragment to variable mapping
        View rootView = inflater.inflate(R.layout.fragment_add_trip, container, false);

        // I do the variable mapping to the elements present on the interface
        edtName = rootView.findViewById(R.id.edtTripName);
        edtDestination = rootView.findViewById(R.id.edtDestination);
        edtDate = rootView.findViewById(R.id.edtDate);
        edtDescription = rootView.findViewById(R.id.edtDescription);
        edtNote = rootView.findViewById(R.id.edtNote);
        edtTopic = rootView.findViewById(R.id.edtTopic);
        switchRisk = rootView.findViewById(R.id.switchRisk);
        btnAdd = rootView.findViewById(R.id.btnAddTrip);

        // I use Calendar class to get current time
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Here I set every time user focus on edit Date will execute the below logic code
        edtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){

                    // I use the DatePickerDialog to create a date picker dialog that shows the user to choose a date
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            month = month+1;
                            String date = day + "/" + month + "/" + year;
                            edtDate.setText(date);
                            edtDate.setError(null);
                        }
                    }, year, month, day);
                    datePickerDialog.show();
                }
            }
        });

        switchRisk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked){
                    switchRiskChecked = 1;
                } else {
                    switchRiskChecked = 0;
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValidData = true;

                if(TextUtils.isEmpty(edtName.getText().toString())){
                    edtName.setError("Name must not empty");
                    isValidData = false;
                } else {
                    edtName.setError(null);
                }

                if(TextUtils.isEmpty(edtDestination.getText().toString())){
                    edtDestination.setError("Destination must not empty");
                    isValidData = false;
                } else {
                    edtDestination.setError(null);
                }

                if(TextUtils.isEmpty(edtDate.getText().toString())){
                    edtDate.setError("Date must not empty");
                    isValidData = false;
                } else {
                    boolean isValidDate = Date_Time_Validator.isValidDate(edtDate.getText().toString());
                    if(isValidDate) {
                        edtDate.setError(null);
                    } else  {
                        edtDate.setError("Date not match format dd/mm/yyyy");
                    }
                }

                if(TextUtils.isEmpty(edtNote.getText().toString())){
                    edtNote.setError("Note must not empty");
                    isValidData = false;
                } else {
                    edtNote.setError(null);
                }

                if(TextUtils.isEmpty(edtTopic.getText().toString())){
                    edtTopic.setError("Topic must not empty");
                    isValidData = false;
                } else {
                    edtTopic.setError(null);
                }

                if(isValidData){


                    showDialogConfirmAdd();


                } else {
                    Toast.makeText(rootView.getContext(), "Data not valid", Toast.LENGTH_LONG).show();
                }
            }
        });

        takePictureCameraBtn = rootView.findViewById(R.id.takePictureCamera);
        getPictureLibraryBtn = rootView.findViewById(R.id.getPictureLibrary);
        imageView = rootView.findViewById(R.id.imageTrip);

        takePictureCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(ActivityCompat.checkSelfPermission (rootView.getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    } else {
                        openCamera();
                    }
                } else {
                    openCamera();
                }
            }
        });

        getPictureLibraryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent();
                iGallery.setType("image/*");
                iGallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(iGallery, "Title"), SELECT_IMAGE_CODE);
            }
        });

        return rootView;
    }

    private  void showDialogConfirmAdd(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_confirm_add_trip, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        TextView tvConfirmTripName = view.findViewById(R.id.tvConfirmTripName);
        TextView tvConfirmTripDestination = view.findViewById(R.id.tvConfirmTripDestination);
        TextView tvConfirmTripDate = view.findViewById(R.id.tvConfirmTripDate);
        TextView tvConfirmTripDescription = view.findViewById(R.id.tvConfirmTripDescription);
        TextView tvConfirmTripNote = view.findViewById(R.id.tvConfirmTripNote);
        TextView tvConfirmTripTopic = view.findViewById(R.id.tvConfirmTripTopic);
        TextView tvConfirmTripAssessment = view.findViewById(R.id.tvConfirmTripAssessment);
        Button btnEditTrip = view.findViewById(R.id.btnEditTrip);
        Button btnConfirmAddTrip = view.findViewById(R.id.btnConfirmAddTrip);


        //Set data show to confirm
        tvConfirmTripName.setText(edtName.getText().toString());
        tvConfirmTripDestination.setText(edtDestination.getText().toString());
        tvConfirmTripDate.setText(edtDate.getText().toString());
        tvConfirmTripDescription.setText(edtDescription.getText().toString());
        tvConfirmTripNote.setText(edtNote.getText().toString());
        tvConfirmTripTopic.setText(edtTopic.getText().toString());
        if(switchRiskChecked == 1){
            tvConfirmTripAssessment.setText("Yes");
        } else {
            tvConfirmTripAssessment.setText("No");
        }

        btnEditTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnConfirmAddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dataName = edtName.getText().toString();
                String dataDestination = edtDestination.getText().toString();
                String dataDate = edtDate.getText().toString();
                String dataDescription = edtDescription.getText().toString();
                String dataNote = edtNote.getText().toString();
                String dataTopic = edtTopic.getText().toString();

                if(Trip_CRUD.insert( getContext(),
                        dataName,
                        dataDestination,
                        dataDate,
                        dataDescription,
                        dataNote,
                        dataTopic,
                        switchRiskChecked,
                        uri.toString())){
                    Toast.makeText(getContext(), "Insert data success!", Toast.LENGTH_SHORT).show();
                    edtName.setText("");
                    edtDestination.setText("");
                    edtDate.setText("");
                    edtDescription.setText("");
                    edtNote.setText("");
                    edtTopic.setText("");
                    switchRisk.setChecked(false);
                    switchRiskChecked = 0;

                } else  {
                    Toast.makeText(getContext(), "Insert data false!", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "new Image");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the camera");
        uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent camintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camintent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(camintent, CAPTURE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_LONG).show();
                }
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == getActivity().RESULT_OK) {
            if(requestCode == 1) {
                uri = data.getData();
                imageView.setImageURI(uri);
            } else {
                imageView.setImageURI(uri);
            }
        }
    }
}