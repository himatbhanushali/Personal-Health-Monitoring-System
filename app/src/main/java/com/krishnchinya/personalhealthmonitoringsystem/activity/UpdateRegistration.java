package com.krishnchinya.personalhealthmonitoringsystem.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import com.krishnchinya.personalhealthmonitoringsystem.R;
import com.krishnchinya.personalhealthmonitoringsystem.other.GlobalVars;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

/**
 * Created by KrishnChinya on 2/11/17.
 */

public class UpdateRegistration extends Activity{

    EditText dob,etFirstName,etLastName,etWeight,etHeight,etPhone;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormat;
    RadioButton btnMale ,btnFemale;

    Button updatepro;
    myTextWatcher watcher1,watcher2,watcher4,watcher5,watcher6;
    TextInputLayout input_etFirstName,input_etLastName,input_etWeight,
            input_etHeight,input_etPhone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprofile);

        final DB_Handler db_handler = new DB_Handler(UpdateRegistration.this);

        final GlobalVars globalVars = (GlobalVars)getApplicationContext();

        DB_Setter_Getter dbSetterGetter = new DB_Setter_Getter(globalVars.getMailid().toString(),"dummy-ignore");

        String[] details = db_handler.getRegistrationDetails(dbSetterGetter);

        dob = (EditText) findViewById(R.id.etUpdatedob);

        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        try {
            Date date = dateFormat.parse(details[2]);
            dob.setText(dateFormat.format(date));
        }catch (ParseException E)
        {

        }

        updatepro = (Button) findViewById(R.id.btnUpdatepro);
        dob.setInputType(InputType.TYPE_NULL);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide the keyboard
                InputMethodManager inputManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(v.getWindowToken(),0);

                Calendar calenderinstance = Calendar.getInstance();
                datePickerDialog = new DatePickerDialog(UpdateRegistration.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar newdate = Calendar.getInstance();
                        newdate.set(year,month,dayOfMonth);
                        dob.setText(dateFormat.format(newdate.getTime()));

                    }
                },calenderinstance.get(Calendar.YEAR), calenderinstance.get(Calendar.MONTH),calenderinstance.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        etFirstName = (EditText)findViewById(R.id.etUpdateFirstName);
        etLastName = (EditText) findViewById(R.id.etUpdateLastName);
        etWeight = (EditText) findViewById(R.id.etUpdateWeight);
        etHeight = (EditText) findViewById(R.id.etUpdateHeight);
        etPhone = (EditText) findViewById(R.id.etUpdatePhone);
        btnMale = (RadioButton)findViewById(R.id.btnUpdateMale);
        btnFemale = (RadioButton)findViewById(R.id.btnUpdateFeale);

        //setting value
        etFirstName.setText(details[0]);
        etLastName.setText(details[1]);
        etWeight.setText(details[4]);
        etHeight.setText(details[5]);
        etPhone.setText("1234567890");
        btnMale.setChecked(true);

        input_etFirstName = (TextInputLayout) findViewById(R.id.input_etUpdateFirstName);
        input_etLastName= (TextInputLayout) findViewById(R.id.input_etUpdateLastName);
        input_etWeight= (TextInputLayout) findViewById(R.id.input_etUpdateWeight);
        input_etHeight= (TextInputLayout) findViewById(R.id.input_etUpdateHeight);
        input_etPhone= (TextInputLayout) findViewById(R.id.input_etUpdatePhone);

        watcher1 = new myTextWatcher(etFirstName,input_etFirstName,UpdateRegistration.this);
        watcher2 = new myTextWatcher(etLastName,input_etLastName,UpdateRegistration.this);
        watcher4 = new myTextWatcher(etWeight,input_etWeight,UpdateRegistration.this);
        watcher5 = new myTextWatcher(etHeight,input_etHeight,UpdateRegistration.this);
        watcher6 = new myTextWatcher(etPhone,input_etPhone,UpdateRegistration.this);
       // watcher9 = new myTextWatcher(btnMale,btnFemale,Registration.this);

        etFirstName.addTextChangedListener(watcher1);
        etLastName.addTextChangedListener(watcher2);
        etWeight.addTextChangedListener(watcher4);
        etHeight.addTextChangedListener(watcher5);
        etPhone.addTextChangedListener(watcher6);

        updatepro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!watcher1.validateName()) | (!watcher2.validateName())) {
                    return;
                }

                if((!watcher4.validateWight()) | (!watcher5.validateHeight()) | (!watcher6.validatePhone())){
                    return;
                }
//                if(!watcher9.validateGender())
//                {
//                    return;
//                }

                DB_Setter_Getter dbSetterGetter = new DB_Setter_Getter(etFirstName.getText().toString(),
                        etLastName.getText().toString(),dob.getText().toString(),"Male",
                        etWeight.getText().toString(),etHeight.getText().toString(),etPhone.getText().toString()
                        ,globalVars.getMailid().toString());

                    db_handler.editegistration(dbSetterGetter);

                    Intent intent = new Intent();
                    setResult(Activity.RESULT_CANCELED,intent);
                    finish();
            }
        });
    }
}
