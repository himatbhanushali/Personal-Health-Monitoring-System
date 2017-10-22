package com.krishnchinya.personalhealthmonitoringsystem.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.krishnchinya.personalhealthmonitoringsystem.R;
import com.krishnchinya.personalhealthmonitoringsystem.other.GlobalVars;
import com.krishnchinya.personalhealthmonitoringsystem.other.Medicine;
import com.krishnchinya.personalhealthmonitoringsystem.other.RetrofitObjectAPI;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Addmedication extends Activity {

    TextInputLayout tliMedName;
    AutoCompleteTextView medicineName;
    TextWatcher watcher;
    Retrofit retrofit;
    List<String> names = new ArrayList<String>();
    String[] result;
    EditText scheduledate,remaindertime,et_noofdays,alertnoofdays,doctorName;
    SimpleDateFormat dateFormat;
    DatePickerDialog datePickerDialog;
    RadioGroup duration,days;
    final CharSequence[] daysofweek = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    boolean[] daysofweekchecked = {false,false,false,false,false,false,false};
    final ArrayList seletedItems=new ArrayList();
    AlertDialog.Builder builder;
    Button save_medication;
    GlobalVars globalVars;
    String noofdays;
    Spinner medicineType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmedication);

        globalVars = (GlobalVars)getApplicationContext();

        retrofit = new Retrofit.Builder().
                baseUrl("https://dailymed.nlm.nih.gov/").
                addConverterFactory(GsonConverterFactory.create())
                .build();

        medicineName = (AutoCompleteTextView) findViewById(R.id.medicineName);

        tliMedName = (TextInputLayout) findViewById(R.id.tilMedName);
        scheduledate = (EditText) findViewById(R.id.scheduledate);
        duration = (RadioGroup) findViewById(R.id.duration);
        days = (RadioGroup) findViewById(R.id.days);
        //et_noofdays = (EditText)findViewById(R.id.et_noofdays);
        save_medication = (Button) findViewById(R.id.save_medication);
        doctorName = (EditText) findViewById(R.id.doctorName);
        medicineType = (Spinner) findViewById(R.id.medicineType);
        remaindertime = (EditText) findViewById(R.id.remaindertime);

        scheduledate.setInputType(InputType.TYPE_NULL);
//
//        medicineName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                inputMethodManager.setInputMethod();
//            }
//        });

        watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String medName = medicineName.getText().toString().trim();

                if (medName.length() > 1) {
                    medicineName.clearListSelection();
                    medicineName.dismissDropDown();
                    //adapter.clear();
                    RetrofitObjectAPI object = retrofit.create(RetrofitObjectAPI.class);
                    Call<Medicine> call = object.getMedicines(medName);

                    call.enqueue(new Callback<Medicine>() {
                        @Override
                        public void onResponse(Call<Medicine> call, Response<Medicine> response) {
                            Medicine medicine = response.body();
                            names.removeAll(names);
                            for (int i = 0; i < medicine.datainfo.size(); i++) {
                                if(medicine.getDatainfo().get(i).getTitle().toString() == null)
                                {

                                }else {
                                    names.add(medicine.getDatainfo().get(i).getTitle().toString());
                                }
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                    (getApplicationContext(),android.R.layout.simple_dropdown_item_1line,names);
                            medicineName.setAdapter(adapter);
                            adapter.setNotifyOnChange(true);

                            //adapter.addAll(names);
                            adapter.notifyDataSetChanged();
                            medicineName.showDropDown();

                        }

                        @Override
                        public void onFailure(Call<Medicine> call, Throwable t) {

                        }
                    });
                }
            }
        };

        medicineName.addTextChangedListener(watcher);

        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        scheduledate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide the keyboard
                InputMethodManager inputManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(v.getWindowToken(),0);

                Calendar calenderinstance = Calendar.getInstance();
                datePickerDialog = new DatePickerDialog(Addmedication.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
                    Calendar newdate = Calendar.getInstance();
                    newdate.set(year, month, dayOfMonth);
                    scheduledate.setText(dateFormat.format(newdate.getTime()));
                    }
                },calenderinstance.get(Calendar.YEAR), calenderinstance.get(Calendar.MONTH),calenderinstance.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();

            }
        });


        duration.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == findViewById(R.id.noofdays).getId())
                {
                    alertnoofdays = new EditText(Addmedication.this);
                    alertnoofdays.setId(R.id.noofdaysviewId);
                    alertnoofdays.setInputType(InputType.TYPE_CLASS_NUMBER);

                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    alertnoofdays.setLayoutParams(lp);


                    builder = new AlertDialog.Builder(Addmedication.this);
                    builder.setTitle("Enter Number of Days");
                    builder.setView(alertnoofdays);


                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            noofdays =  ((EditText)(((AlertDialog) dialog).findViewById(R.id.noofdaysviewId))).getText().toString();
                           // noofdays = "10";
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //select back continous days checkbox

                        }
                    });

                    builder.create();
                    builder.show();
                }else
                {
                    //for continous days setting default period as 30;
                    noofdays = "30";
                }

            }
        });


        days.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == findViewById(R.id.specificdays).getId())
                {
                    builder = new AlertDialog.Builder(Addmedication.this);
                    builder.setTitle("Select day in a week");
                    builder.setMultiChoiceItems(daysofweek, daysofweekchecked,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                // indexSelected contains the index of item (of which checkbox checked)
                                @Override
                                public void onClick(DialogInterface dialog, int indexSelected,
                                                    boolean isChecked) {
                                    if (isChecked) {
                                        // If the user checked the item, add it to the selected items
                                        seletedItems.add(indexSelected);
                                        daysofweekchecked[indexSelected] = true;
                                    } else if (seletedItems.contains(indexSelected)) {
                                        // Else, if the item is already in the array, remove it
                                        seletedItems.remove(Integer.valueOf(indexSelected));
                                        daysofweekchecked[indexSelected] = true;
                                    }
                                }
                            })
                            // Set the action buttons
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Your code when user clicked on OK
                                    //  You can write the code  to save the selected item here
                                    int a = 0;
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Your code when user clicked on Cancel
                                }
                            });

                    builder.create();//AlertDialog dialog; create like this outside onClick
                    builder.show();
                }

            }
        });

        remaindertime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide the keyboard
                InputMethodManager inputManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(v.getWindowToken(),0);

                Calendar calenderinstance = Calendar.getInstance();

                TimePickerDialog timePickerDialog = new TimePickerDialog(Addmedication.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        remaindertime.setText(hourOfDay+":"+minute);
                    }
                },calenderinstance.get(Calendar.HOUR_OF_DAY),calenderinstance.get(Calendar.MINUTE),false);


                timePickerDialog.show();

            }
        });

        save_medication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String end_date="";
                GregorianCalendar cal;
                Date dscheduledate;
                //Check all fields are filled or not

                if (medicineName.getText().toString().isEmpty()) {
                    alertwindow("Medicine Name cannot be empty");
                    return;
                }

                if (scheduledate.getText().toString().isEmpty()) {
                    alertwindow("Schedule date cannot be empty");
                    return;
                }

                if (duration.getCheckedRadioButtonId() == -1)
                {
                    alertwindow("Please select duration");
                    return;
                }

                if(days.getCheckedRadioButtonId() == -1)
                {
                    alertwindow("Please select days");
                    return;
                }

                if(doctorName.getText().toString().isEmpty())
                {
                    alertwindow("Please enter the Doctor Name");
                }

                String wkofd="";
                globalVars = (GlobalVars) getApplicationContext();

                for(int i=0;i<daysofweekchecked.length;i++)
                {
                    if(i==daysofweekchecked.length) {
                        wkofd = wkofd + daysofweekchecked[i];
                    }else{
                        wkofd = wkofd + daysofweekchecked[i] + ",";
                    }
                }

                try {
                    dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                    String sheduledate1 = scheduledate.getText().toString();
                    dscheduledate = dateFormat.parse(sheduledate1);

                    cal = new GregorianCalendar();
                    cal.setTime(dscheduledate);
                    cal.add(Calendar.DATE, Integer.parseInt(noofdays));

                    end_date = dateFormat.format(cal.getTime()).toString();

                }catch (Exception e)
                {

                }

                int a=0;

                DB_Setter_Getter db_setter_getter = new DB_Setter_Getter("",globalVars.getMailid(),medicineName.getText().toString(),
                        scheduledate.getText().toString(),end_date,wkofd,"",medicineType.getSelectedItem().toString(),doctorName.getText().toString()
                ,remaindertime.getText().toString());

                DB_Handler db_handler = new DB_Handler(Addmedication.this);
                db_handler.addmedication(db_setter_getter);

                Intent intent = new Intent(Addmedication.this, MainMenu.class);
                //startActivityForResult(intent,1);
                startActivity(intent);
                finish();


            }

            public void alertwindow(String message)
            {
                builder = new AlertDialog.Builder(Addmedication.this);
                builder.setTitle("Error");
                builder.setMessage(message);
                builder.show();
            }

        });


    }
}
