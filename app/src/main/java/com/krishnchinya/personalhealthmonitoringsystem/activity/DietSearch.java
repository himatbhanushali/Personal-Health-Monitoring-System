package com.krishnchinya.personalhealthmonitoringsystem.activity;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.krishnchinya.personalhealthmonitoringsystem.R;
import com.krishnchinya.personalhealthmonitoringsystem.other.FoodItems;
import com.krishnchinya.personalhealthmonitoringsystem.other.fooddetails;
import com.krishnchinya.personalhealthmonitoringsystem.other.RetrofitObjectAPI;

import java.util.ArrayList;
import java.util.List;
;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DietSearch extends Activity {
    AutoCompleteTextView acFoodname;
    TextView item_name,nf_calories,nf_total_fat,nf_cholesterol,nf_total_carbohydrate,nf_serving_size_qty;
    TextWatcher watcher;
    Retrofit retrofit;
    List<String> names = new ArrayList<String>();
    List<String> foodid = new ArrayList<String>();
    FoodItems newFoodItem;
    fooddetails fooddetails;
    Button Adddiet;
    DB_Setter_Getter db_setter_getter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_search);
        acFoodname= (AutoCompleteTextView) findViewById(R.id.foodname);
        item_name = (TextView) findViewById(R.id.item_name);
        nf_calories = (TextView) findViewById(R.id.nf_calories);
        nf_total_fat = (TextView) findViewById(R.id.nf_total_fat);
        nf_cholesterol = (TextView) findViewById(R.id.nf_cholesterol);
        nf_total_carbohydrate = (TextView) findViewById(R.id.nf_total_carbohydrate);
        nf_serving_size_qty = (TextView) findViewById(R.id.nf_serving_size_qty);
        Adddiet = (Button) findViewById(R.id.nfadddiet);

        retrofit = new Retrofit.Builder().
                baseUrl("https://api.nutritionix.com/v1_1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String foodItem = acFoodname.getText().toString().trim();
                String result = "0:20";
                String fields="item_name,brand_name,item_id,brand_id,nf_calories,nf_total_fat,nf_serving_size_qty,nf_serving_size_unit";
                String appkey = "1f6e92133088fdca6de950239db2a965";
                String appid ="3dbeaba2";

                if (foodItem.length() > 1) {
                    acFoodname.clearListSelection();
                    acFoodname.dismissDropDown();

                    RetrofitObjectAPI object = retrofit.create(RetrofitObjectAPI.class);
                    Call<FoodItems> call = object.getFoodItem(foodItem,result,fields,appid,appkey);

                    call.enqueue(new Callback<FoodItems>() {
                        @Override
                        public void onResponse(Call<FoodItems> call, Response<FoodItems> response) {
                            newFoodItem = response.body();
                            names.removeAll(names);
                            foodid.removeAll(foodid);
                            if(newFoodItem!=null) {
                                for (int i = 0; i < newFoodItem.hits.size(); i++) {
                                    names.add(newFoodItem.hits.get(i).fields.getItem_name());
                                    foodid.add(newFoodItem.hits.get(i).fields.getItem_id());

                                }
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                    (getApplicationContext(),android.R.layout.simple_dropdown_item_1line,names);
                            acFoodname.setAdapter(adapter);
                            adapter.setNotifyOnChange(true);

                            adapter.addAll(names);
                            adapter.notifyDataSetChanged();
                            acFoodname.showDropDown();

                        }

                        @Override
                        public void onFailure(Call<FoodItems> call, Throwable t) {

                            int a=0;
                        }
                    });
                }
            }
        };


        acFoodname.addTextChangedListener(watcher);

        acFoodname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final String itemname=acFoodname.getText().toString();
                    String itemid = foodid.get(position);
                    String appkey = "1f6e92133088fdca6de950239db2a965";
                    String appid ="3dbeaba2";

                    RetrofitObjectAPI object = retrofit.create(RetrofitObjectAPI.class);
                    Call<fooddetails> call = object.getFooddetails("item",itemid,appid,appkey);

                    call.enqueue(new Callback<fooddetails>() {
                        @Override
                        public void onResponse(Call<fooddetails> call, Response<fooddetails> response) {
                            fooddetails = response.body();
                            item_name.setText("Item Name:"+fooddetails.itemName);
                            nf_calories.setText("Calories:"+fooddetails.nfCalories.toString());
                            nf_total_fat.setText("Total Fat:"+fooddetails.nfTotalFat.toString());
                            nf_cholesterol.setText("Total Cholesterol:"+fooddetails.nfCholesterol.toString());
                            nf_total_carbohydrate.setText("Total Carbohydrate:"+fooddetails.nfTotalCarbohydrate.toString());
                            nf_serving_size_qty.setText("Serving Quantity:"+fooddetails.nfServingSizeQty.toString());

                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy/MM/dd");
                            String strDate = mdformat.format(calendar.getTime());
                            Double notApplicable = 0.0;

                            db_setter_getter = new DB_Setter_Getter("krishna@gmail.com", "abc",fooddetails.itemName.toString(),
                                    fooddetails.nfCalories.toString(),fooddetails.nfTotalFat.toString(),fooddetails.nfCholesterol.toString(),
                                    fooddetails.nfTotalCarbohydrate.toString(),fooddetails.nfServingSizeQty.toString(), notApplicable);

                        }

                        @Override
                        public void onFailure(Call<fooddetails> call, Throwable t) {

                        }
                    });
                }

        });

        Adddiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DB_Handler db_handler = new DB_Handler(DietSearch.this);
                db_handler.addCalories(db_setter_getter);
                Intent intent = new Intent(DietSearch.this,MainMenu.class);
                startActivity(intent);
                finish();

            }
        });



    }
}
