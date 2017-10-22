package com.krishnchinya.personalhealthmonitoringsystem.activity;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.krishnchinya.personalhealthmonitoringsystem.R;
import com.krishnchinya.personalhealthmonitoringsystem.other.GlobalVars;

public class VitalSigns extends Activity {

    GlobalVars globalVars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vital);

        globalVars = (GlobalVars)getApplicationContext();

        // setting up image dialog for question marks
        ImageView i = (ImageView)findViewById(R.id.question1);
        //SeekBar sb =(SeekBar)findViewById(R.id.seekBar1);

        final Spinner spbloodtype = (Spinner)findViewById(R.id.spinner1);

        spbloodtype.requestFocus();
        final Spinner spcholestrol = (Spinner)findViewById(R.id.spinner2);
        final Spinner spbp = (Spinner) findViewById(R.id.spinner3);

        final EditText etgclevel = (EditText)findViewById(R.id.etGlucoseLevel);
        final EditText ethemolevel = (EditText)findViewById(R.id.etHemoLevel);
        final EditText etheartrate = (EditText)findViewById(R.id.etHeartRate);

        final Button btnsave = (Button)findViewById(R.id.btnSave);


        final EditText etbodytemp = (EditText)findViewById(R.id.etBodyTemp);

        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog settingsDialog = new Dialog(VitalSigns.this);
                settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                settingsDialog.setContentView(VitalSigns.this.getLayoutInflater().inflate(R.layout.imagelayout1
                        , null));
                settingsDialog.show();

            }
        });


        ImageView i1 = (ImageView)findViewById(R.id.question2);

        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog settingsDialog = new Dialog(VitalSigns.this);
                settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                settingsDialog.setContentView(VitalSigns.this.getLayoutInflater().inflate(R.layout.imagelayout2
                        , null));
                settingsDialog.show();

            }
        });


//        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//
//                int tv = Integer.parseInt(etbodytemp.getText().toString());
//                tv +=i;
//                etbodytemp.setText(tv+"");
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//
//                etbodytemp.setEnabled(false);
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//                etbodytemp.setEnabled(true);
//
//            }
//        });

        spbloodtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0) {

                    if(!(btnsave.isEnabled()))
                        btnsave.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spcholestrol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0) {

                    if(!(btnsave.isEnabled()))
                        btnsave.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spbp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0) {

                    if(!(btnsave.isEnabled()))
                        btnsave.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {

                int blood_post = spbloodtype.getSelectedItemPosition();

                if(blood_post==0)
                {
                    Toast t = Toast.makeText(VitalSigns.this,"Please select Blood Group",Toast.LENGTH_LONG);
                    t.show();
                    btnsave.setEnabled(false);
                    return;
                }


                EditText gloucose_level = (EditText) findViewById(R.id.etGlucoseLevel);
                gloucose_level.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);

                gloucose_level.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {}

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        btnsave.setEnabled(true);
                    }
                });

                if((gloucose_level.getText().toString()).equals(""))
                {
                    Toast t =   Toast.makeText(VitalSigns.this, "Please enter glucose level", Toast.LENGTH_SHORT);
                    t.show();
                    btnsave.setEnabled(false);
                    return;

                }
                else
                {
                    float  gc_level = (Float.parseFloat(gloucose_level.getText().toString()));



                    if(gc_level<70 || gc_level>180)
                    {
                        Toast t = Toast.makeText(VitalSigns.this,"Please enter valid glucose level",Toast.LENGTH_SHORT);
                        t.show();
                        gloucose_level.setText("0");
                        gloucose_level.requestFocus();
                        btnsave.setEnabled(false);
                        return;
                    }
                }

                int choles_post = spcholestrol.getSelectedItemPosition();

                if(choles_post==0)
                {
                    Toast t = Toast.makeText(VitalSigns.this,"Please select Cholestrol Level",Toast.LENGTH_LONG);
                    t.show();
                    btnsave.setEnabled(false);
                    return;
                }

                int pressure_post = spbp.getSelectedItemPosition();

                if(pressure_post==0)
                {
                    Toast t = Toast.makeText(VitalSigns.this,"Please select Blood pressure  Level",Toast.LENGTH_LONG);
                    t.show();
                    btnsave.setEnabled(false);
                    return;
                }


                EditText etbtemp = (EditText)findViewById(R.id.etBodyTemp);
                etbtemp.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);

                etbtemp.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {}

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        btnsave.setEnabled(true);
                    }
                });

                if((etbtemp.getText().toString()).equals(""))
                {
                    Toast t =   Toast.makeText(VitalSigns.this, "Please enter Body Temprature ", Toast.LENGTH_SHORT);
                    t.show();
                    btnsave.setEnabled(false);
                    return;

                }
                else
                {
                    float btemp = (Float.parseFloat(etbtemp.getText().toString()));



                    if(btemp<35.0f || btemp>105.8f)
                    {
                        Toast t = Toast.makeText(VitalSigns.this,"Please enter valid body temprature",Toast.LENGTH_SHORT);
                        t.show();
                        etbtemp.setText("35");
                        etbtemp.requestFocus();
                        btnsave.setEnabled(false);
                        return;
                    }
                }

                EditText etHemolevel = (EditText)findViewById(R.id.etHemoLevel);

                etHemolevel.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);

                etHemolevel.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {}

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        btnsave.setEnabled(true);
                    }
                });

                if((etHemolevel.getText().toString()).equals(""))
                {
                    Toast t =   Toast.makeText(VitalSigns.this, "Please enter Correct Hemoglobin Level ", Toast.LENGTH_SHORT);
                    t.show();
                    btnsave.setEnabled(false);
                    return;

                }
                else
                {
                    int hemo = (Integer.parseInt(etHemolevel.getText().toString()));



                    if(hemo<0 || hemo>23)
                    {
                        Toast t = Toast.makeText(VitalSigns.this,"Please enter valid HemoGlobin Value",Toast.LENGTH_SHORT);
                        t.show();
                        ethemolevel.setText("12");
                        ethemolevel.requestFocus();
                        btnsave.setEnabled(false);
                        return;
                    }
                }


                EditText etHRate = (EditText)findViewById(R.id.etHeartRate);

                etHRate.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);

                etHRate.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {}

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        btnsave.setEnabled(true);
                    }
                });

                if((etHRate.getText().toString()).equals(""))
                {
                    Toast t =   Toast.makeText(VitalSigns.this, "Please enter Correct Heart Rate ", Toast.LENGTH_SHORT);
                    t.show();
                    btnsave.setEnabled(false);
                    return;

                }
                else
                {
                    int hr = (Integer.parseInt(etHRate.getText().toString()));



                    if(hr<75 || hr>170)
                    {
                        Toast t = Toast.makeText(VitalSigns.this,"Please enter valid Heart Rate",Toast.LENGTH_SHORT);
                        t.show();
                        etHRate.setText("90");
                        etHRate.requestFocus();
                        btnsave.setEnabled(false);
                        return;
                    }
                }

                EditText etHeight = (EditText)findViewById(R.id.etHeight);
                EditText etWeight = (EditText)findViewById(R.id.etWeight);

                double height;
                double weight;
                String msg="";
                height= Double.parseDouble(etHeight.getText().toString());
                weight  = Double.parseDouble(etWeight.getText().toString());


                double BMI = (weight * 703) / (height * height);
                if(BMI<18.5)
                {
                    msg="Under weight";
                }
                else if(BMI>18.5 && BMI<24.9)
                {
                    msg="Normal";
                }
                else if(BMI>24.9 && BMI<29.9)
                {
                    msg="Over Weight";
                }
                else if(BMI>29.9)
                {
                    msg="Obese";
                }

                DB_Setter_Getter db_setter_getter = new DB_Setter_Getter(globalVars.getMailid(), spbloodtype.getItemAtPosition(spbloodtype.getSelectedItemPosition()).toString()
                ,spcholestrol.getItemAtPosition(spcholestrol.getSelectedItemPosition()).toString(),spbp.getItemAtPosition(spbp.getSelectedItemPosition()).toString(),
                        etHemolevel.getText().toString(),etgclevel.getText().toString(),etheartrate.getText().toString(), msg,
                        etbodytemp.getText().toString(),1);

                DB_Handler db_handler = new DB_Handler(VitalSigns.this);
                db_handler.addvitalSigns(db_setter_getter);

                Intent intent = new Intent(VitalSigns.this, MainMenu.class);
                //startActivityForResult(intent,1);
                startActivity(intent);
                finish();


            }


        });

    }
}
