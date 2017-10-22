package com.krishnchinya.personalhealthmonitoringsystem.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.krishnchinya.personalhealthmonitoringsystem.R;
import com.krishnchinya.personalhealthmonitoringsystem.activity.Addmedication;
import com.krishnchinya.personalhealthmonitoringsystem.activity.DB_Handler;
import com.krishnchinya.personalhealthmonitoringsystem.activity.MainMenu;
import com.krishnchinya.personalhealthmonitoringsystem.activity.Medication_Confirmation;
import com.krishnchinya.personalhealthmonitoringsystem.activity.Registration;
import com.krishnchinya.personalhealthmonitoringsystem.other.GlobalVars;

import java.util.ArrayList;
import java.util.Locale;


public class MainMenu_Medication extends Fragment {
    View view;
    EditText medicationdate;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormat;
    FloatingActionButton addmedication;
    LinearLayout morning,afternoon,evening,night;
    ImageView morningimag;
    GlobalVars globalVars;
    CardView cvmorning,cvafternoon,cvevening,cvnight;
    ArrayList<String> almor = new ArrayList<>();
    ArrayList<String> alaft = new ArrayList<>();
    ArrayList<String> aleve = new ArrayList<>();
    ArrayList<String> alnight = new ArrayList<>();

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_menu_medication, container, false);
        globalVars = (GlobalVars) getActivity().getApplicationContext();
        medicationdate = (EditText) view.findViewById(R.id.medicationdate);
        morning = (LinearLayout) view.findViewById(R.id.morning);
        afternoon = (LinearLayout) view.findViewById(R.id.afternoon);
        evening = (LinearLayout) view.findViewById(R.id.evening);
        night = (LinearLayout) view.findViewById(R.id.night);
       // morningimag = (ImageView) view.findViewById(R.id.morningimag);

        cvmorning = (CardView) view.findViewById(R.id.cvmorning);
        cvafternoon = (CardView) view.findViewById(R.id.cvafternoon);
        cvevening = (CardView) view.findViewById(R.id.cvevening);
        cvnight = (CardView) view.findViewById(R.id.cvnight);

        medicationdate.setInputType(InputType.TYPE_NULL);

        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        medicationdate.setText(dateFormat.format(Calendar.getInstance().getTime()));

        medicationdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide the keyboard
                InputMethodManager inputManager = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(v.getWindowToken(),0);

                Calendar calenderinstance = Calendar.getInstance();
                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar newdate = Calendar.getInstance();
                        newdate.set(year,month,dayOfMonth);
                        medicationdate.setText(dateFormat.format(newdate.getTime()));

                    }
                },calenderinstance.get(Calendar.YEAR), calenderinstance.get(Calendar.MONTH),calenderinstance.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();


            }

        });

        medicationdate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loadMedicines(morning,afternoon,evening,night);
            }
        });

        addmedication = (FloatingActionButton) view.findViewById(R.id.addMedication);

        addmedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Addmedication.class);
                //startActivityForResult(intent,1);
                startActivity(intent);
            }
        });

        loadMedicines(morning,afternoon,evening,night);


        cvmorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Medication_Confirmation.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("Meddetails",almor);
                intent.putExtras(bundle);
                startActivityForResult(intent,1);

            }
        });


        cvafternoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Medication_Confirmation.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("Meddetails",alaft);
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
            }
        });

        cvevening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Medication_Confirmation.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("Meddetails",aleve);
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
            }
        });

        cvnight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Medication_Confirmation.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("Meddetails",alnight);
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
            }
        });


        return view;
    }

    public void loadMedicines(LinearLayout morning, LinearLayout afternoon, LinearLayout evening, LinearLayout night)
    {
        morning.removeAllViews();
        afternoon.removeAllViews();
        evening.removeAllViews();
        night.removeAllViews();
        almor.removeAll(almor);
        alaft.removeAll(alaft);
        aleve.removeAll(aleve);
        alnight.removeAll(alnight);

        DB_Handler db_handler = new DB_Handler(getContext());

        String medicationdetails[][] = db_handler.getmedication(globalVars.getMailid(),medicationdate.getText().toString());

        for(int i=0;i<medicationdetails.length;i++)
        {
            String houroftheday;
            String time = medicationdetails[i][5];
            String medicineName = medicationdetails[i][0];
            String medicineType = medicationdetails[i][1];

            String[] Spilt = time.split(":");

            if(Integer.parseInt(Spilt[0]) > 5 && Integer.parseInt(Spilt[0]) <= 12)
            {
                almor.add(medicineName);
                almor.add(medicineType);
                almor.add(time);
                almor.add(globalVars.getMailid());
                medicinesToView(morning, medicineType);

            }else if(Integer.parseInt(Spilt[0]) > 12 && Integer.parseInt(Spilt[0]) <= 17)
             {
                 alaft.add(medicineName);
                 alaft.add(medicineType);
                 alaft.add(time);
                 alaft.add(globalVars.getMailid());
            medicinesToView(afternoon,medicineType);

            }else if(Integer.parseInt(Spilt[0]) > 17 && Integer.parseInt(Spilt[0]) <= 21)
            {
                aleve.add(medicineName);
                aleve.add(medicineType);
                aleve.add(time);
                aleve.add(globalVars.getMailid());
            medicinesToView(evening,medicineType);
            }else{
                alnight.add(medicineName);
                alnight.add(medicineType);
                alnight.add(time);
                alnight.add(globalVars.getMailid());
            medicinesToView(night,medicineType);
            }
        }
    }

    public void medicinesToView(LinearLayout linearLayout,String medicinetype)
    {
        ImageView imageView = new ImageView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(96,96);
        params.topMargin = 20;
        params.leftMargin = 20;
        imageView.setLayoutParams(params);
        if(medicinetype.equals("Capsule")) {
            imageView.setImageResource(R.drawable.capsule1);
        }else if(medicinetype.equals("Tablet")){
            params.topMargin = 10;
            params.leftMargin = 10;
            imageView.setLayoutParams(params);
        imageView.setImageResource(R.drawable.tablet);
        }else {
            imageView.setImageResource(R.drawable.capsule1);
        }
        linearLayout.addView(imageView);
    }


}
