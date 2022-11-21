package vn.edu.greenwich.expensemanagementjavaapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import vn.edu.greenwich.expensemanagementjavaapp.Database.Trip;
import vn.edu.greenwich.expensemanagementjavaapp.Database.Trip_CRUD;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Update_Trip#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Update_Trip extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Update_Trip() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Update_Trip.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Update_Trip newInstance(String param1, String param2) {
        Fragment_Update_Trip fragment = new Fragment_Update_Trip();
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

    TextView dtNameUpdate;
    TextView dtDestinationUpdate;
    TextView dtDateUpdate;
    TextView dtDescriptionUpdate;
    TextView dtNoteUpdate;
    TextView dtTopicUpdate;
    Switch dtRiskAssessmentUpdate;
    Button btnUpdateTrip;
    Trip tripUpdate;
    int isRiskAssessmentChecked = 0;

    //Element for take picture camera or library
    Button takePictureCameraUpdateBtn, getPictureLibraryUpdateBtn;
    ImageView imageView;
    Uri uri;

    private static final int PERMISSION_CODE = 1234;
    private static final int CAPTURE_CODE = 1001;
    private final int SELECT_IMAGE_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Create root view
        View rootView = inflater.inflate(R.layout.fragment_update_trip, container, false);

        //element for take picture
        takePictureCameraUpdateBtn = rootView.findViewById(R.id.takePictureCameraUpdate);
        getPictureLibraryUpdateBtn = rootView.findViewById(R.id.getPictureLibraryUpdate);
        imageView = rootView.findViewById(R.id.imageTripUpdate);

        //element for update data trip
        dtNameUpdate = rootView.findViewById(R.id.edtTripNameUpdate);
        dtDestinationUpdate = rootView.findViewById(R.id.edtDestinationUpdate);
        dtDateUpdate = rootView.findViewById(R.id.edtDateUpdate);
        dtDescriptionUpdate = rootView.findViewById(R.id.edtDescriptionUpdate);
        dtNoteUpdate = rootView.findViewById(R.id.edtNoteUpdate);
        dtTopicUpdate = rootView.findViewById(R.id.edtTopicUpdate);
        dtRiskAssessmentUpdate = rootView.findViewById(R.id.switchRiskUpdateUpdate);
        btnUpdateTrip = rootView.findViewById(R.id.btnUpdateTrip);

        takePictureCameraUpdateBtn.setOnClickListener(new View.OnClickListener() {
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

        getPictureLibraryUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent();
                iGallery.setType("image/*");
                iGallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(iGallery, "Title"), SELECT_IMAGE_CODE);
            }
        });

        //Get data Trip from bundle
        Bundle bundle = this.getArguments();
        int Id = bundle.getInt("Id");
        tripUpdate = Trip_CRUD.getDetail(getContext(), Id);

        //Set data show screen
        dtNameUpdate.setText(tripUpdate.getName());
        dtDestinationUpdate.setText(tripUpdate.getDestination());
        dtDateUpdate.setText(tripUpdate.getDate());
        dtDescriptionUpdate.setText(tripUpdate.getDescription());
        dtNoteUpdate.setText(tripUpdate.getNote());
        dtTopicUpdate.setText(tripUpdate.getTopic());
        if(tripUpdate.getRequiredRiskAssessment() > 0){
            dtRiskAssessmentUpdate.setChecked(true);
            isRiskAssessmentChecked = 1;
        } else {
            dtRiskAssessmentUpdate.setChecked(false);
            isRiskAssessmentChecked = 0;
        }
        if(!TextUtils.isEmpty(tripUpdate.getImage())){
            imageView.setImageURI(Uri.parse(tripUpdate.getImage()));
            uri = Uri.parse(tripUpdate.getImage());
        }

        //Handle select date
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        dtDateUpdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            month = month+1;
                            String date = day + "/" + month + "/" + year;
                            dtDateUpdate.setText(date);
                            dtDateUpdate.setError(null);
                        }
                    }, year, month, day);
                    datePickerDialog.show();
                }
            }
        });

        // handle change switch risk assessment
        dtRiskAssessmentUpdate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    isRiskAssessmentChecked = 1;
                } else {
                    isRiskAssessmentChecked = 0;
                }
            }
        });

        //handle update trip
        btnUpdateTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValidData = true;

                if(TextUtils.isEmpty(dtNameUpdate.getText().toString())){
                    dtNameUpdate.setError("Name must not empty");
                    isValidData = false;
                } else {
                    dtNameUpdate.setError(null);
                }

                if(TextUtils.isEmpty(dtDestinationUpdate.getText().toString())){
                    dtDestinationUpdate.setError("Destination must not empty");
                    isValidData = false;
                } else {
                    dtDestinationUpdate.setError(null);
                }

                if(TextUtils.isEmpty(dtDateUpdate.getText().toString())){
                    dtDateUpdate.setError("Date must not empty");
                    isValidData = false;
                } else {
                    dtDateUpdate.setError(null);
                }

                if(TextUtils.isEmpty(dtDescriptionUpdate.getText().toString())){
                    dtDescriptionUpdate.setError("Description must not empty");
                    isValidData = false;
                } else {
                    dtDescriptionUpdate.setError(null);
                }

                if(TextUtils.isEmpty(dtNoteUpdate.getText().toString())){
                    dtNoteUpdate.setError("Note must not empty");
                    isValidData = false;
                } else {
                    dtNoteUpdate.setError(null);
                }

                if(TextUtils.isEmpty(dtTopicUpdate.getText().toString())){
                    dtTopicUpdate.setError("Topic must not empty");
                    isValidData = false;
                } else {
                    dtTopicUpdate.setError(null);
                }

                if(isValidData){
                    tripUpdate.setName(dtNameUpdate.getText().toString());
                    tripUpdate.setDestination(dtDestinationUpdate.getText().toString());
                    tripUpdate.setDate(dtDateUpdate.getText().toString());
                    tripUpdate.setDescription(dtDescriptionUpdate.getText().toString());
                    tripUpdate.setNote(dtNoteUpdate.getText().toString());
                    tripUpdate.setTopic(dtTopicUpdate.getText().toString());
                    tripUpdate.setRequiredRiskAssessment(isRiskAssessmentChecked);
                    tripUpdate.setImage(uri.toString());

                    if(Trip_CRUD.update(getContext(), tripUpdate)){
                        Toast.makeText(getContext(), "Update trip success!", Toast.LENGTH_SHORT).show();

                        //set data bundle to pass to detail fragment
                        Bundle bundle = new Bundle();
                        bundle.putInt("Id", tripUpdate.getId());
                        bundle.putString("Name", tripUpdate.getName());
                        bundle.putString("Destination", tripUpdate.getDestination());
                        bundle.putString("Date", tripUpdate.getDate());
                        bundle.putString("Description", tripUpdate.getDescription());
                        bundle.putString("Note", tripUpdate.getNote());
                        bundle.putString("Topic", tripUpdate.getTopic());
                        bundle.putInt("RequiredRiskAssessment", isRiskAssessmentChecked);
                    }else {
                        Toast.makeText(getContext(), "Update trip false!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Data not valid", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Inflate the layout for this fragment
        return rootView;
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

