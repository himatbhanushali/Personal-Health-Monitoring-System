package com.krishnchinya.personalhealthmonitoringsystem.fragments;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.krishnchinya.personalhealthmonitoringsystem.R;
import com.krishnchinya.personalhealthmonitoringsystem.activity.DB_Handler;
import com.krishnchinya.personalhealthmonitoringsystem.activity.DB_Setter_Getter;
import com.krishnchinya.personalhealthmonitoringsystem.activity.DietActivity;
import com.krishnchinya.personalhealthmonitoringsystem.activity.DietSearch;
import com.krishnchinya.personalhealthmonitoringsystem.activity.NewNotesActivity;
import com.krishnchinya.personalhealthmonitoringsystem.activity.VitalSigns;

import java.util.ArrayList;


public class MainMenu_Diet extends Fragment {
    View view;
    FloatingActionButton addDiet;
    TextView TotalCal;
    ImageView calories;
    private RecyclerView dietRV;

    private RecyclerView.LayoutManager mLayoutManager;

    private DB_Handler db_handler;
    private DB_Setter_Getter db_setter_getter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_menu_diet, container, false);
        float clry=0.0f;

        dietRV= (RecyclerView) view.findViewById(R.id.dietRV);
        TotalCal = (TextView) view.findViewById(R.id.TotalCal);
        dietRV.setHasFixedSize(true);

        dietRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL,false));

        db_handler = new DB_Handler(getContext());

        ArrayList<ArrayList<String>> details = db_handler.getDietRecords("krishna@gmail.com", "abc");

        for(int i =0;i<details.size();i++)
        {
            for(int j=0;j<details.get(i).size();j=j+4)
            {
                clry = clry + Float.valueOf(details.get(i).get(j));
            }
        }

        TotalCal.setText("Total Calorie:"+clry);

        myDietAdapter adapter = new myDietAdapter(details);
        dietRV.setAdapter(adapter);

        //calories = (ImageView) view.findViewById(R.id.calories);
        addDiet = (FloatingActionButton) view.findViewById(R.id.addDiet);

        addDiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),DietSearch.class);
                startActivityForResult(intent,1);
            }
        });

        return view;
    }
}


class myDietAdapter extends RecyclerView.Adapter<dietViewholder>
{
    private ArrayList<ArrayList<String>> myData;
    private String[] Id;
    int count=0;
    DB_Handler db_handler;
    Drawable draw;

    public myDietAdapter(ArrayList<ArrayList<String>> mData){
        myData=mData;
        // this.Id = Id;
    }

    @Override
    public dietViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_diet,parent,false);

        dietViewholder vh =new dietViewholder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final dietViewholder holder, final int position) {
        holder.tvdiet4.setText("Calorie:"+myData.get(position).get(0));
        holder.tvdiet2.setText("Total Fat:"+myData.get(position).get(1));
        holder.tvdiet3.setText("Cholestrol:"+myData.get(position).get(2));
        holder.tvdiet1.setText("Item Name:"+myData.get(position).get(3));
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

}

class dietViewholder extends RecyclerView.ViewHolder{
    TextView tvdiet1,tvdiet2,tvdiet3,tvdiet4;
    //ImageView bytesarray;
    public dietViewholder(View itemView) {
        super(itemView);
        tvdiet1 = (TextView) itemView.findViewById(R.id.tvDiet1);
        tvdiet2 = (TextView) itemView.findViewById(R.id.tvDiet2);
        tvdiet3 = (TextView) itemView.findViewById(R.id.tvDiet3);
        tvdiet4 = (TextView) itemView.findViewById(R.id.tvDiet4);
        //bytesarray=(ImageView) itemView.findViewById(R.id.camerabutton);
    }
}