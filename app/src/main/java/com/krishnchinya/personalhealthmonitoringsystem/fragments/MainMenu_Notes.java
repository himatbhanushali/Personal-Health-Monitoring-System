package com.krishnchinya.personalhealthmonitoringsystem.fragments;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.krishnchinya.personalhealthmonitoringsystem.R;
import com.krishnchinya.personalhealthmonitoringsystem.activity.DB_Handler;
import com.krishnchinya.personalhealthmonitoringsystem.activity.DB_Setter_Getter;
import com.krishnchinya.personalhealthmonitoringsystem.activity.NewNotesActivity;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;


public class MainMenu_Notes extends Fragment  {
    View view;

    private RecyclerView notesRecyclerView;

    private RecyclerView.LayoutManager mLayoutManager;

    private DB_Setter_Getter db_setter_getter;

    private DB_Handler db_handler;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_menu_notes, container, false);




        notesRecyclerView = (RecyclerView) view.findViewById(R.id.notes_recycler_view);

        notesRecyclerView.setHasFixedSize(true);

        // mLayoutManager=new LinearLayoutManager();
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL,false));

        db_handler = new DB_Handler(getContext());

        ArrayList<String> details = db_handler.getAllNotes("krishna@gmail.com");

        myNotesAdapter adapter = new myNotesAdapter(details,getActivity());
        notesRecyclerView.setAdapter(adapter);


        FloatingActionButton newNote = (FloatingActionButton) view.findViewById(R.id.newNote);
        newNote.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getContext(), NewNotesActivity.class);
                intent.putExtra("caller", "newNote");
                startActivity(intent);

                //startActivityForResult(intent,1);
            }

        });

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }


}

class myNotesAdapter extends RecyclerView.Adapter<notesViewholder>
{
    Activity activity;
    private ArrayList<String> myData;
    private String[] Id;
    int count=0;
    DB_Handler db_handler;
    Drawable draw;

    public myNotesAdapter(ArrayList<String> mData,Activity context){
        myData=mData;
       // this.Id = Id;
        this.activity = context;
    }

    @Override
    public notesViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_veiw_notes,parent,false);

        notesViewholder vh =new notesViewholder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final notesViewholder holder, final int position) {
        holder.notesName.setText(myData.get(count++));
        holder.noteid.setText(myData.get(count++));


        holder.notesName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewNotesActivity.class);
                //draw=Drawable.createFromStream(new ByteArrayInputStream(ARRAY_BYTES),null)
                intent.putExtra("caller", holder.noteid.getText().toString());
                v.getContext().startActivity(intent);
            }
        });

        holder.notesName.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View v) {
                db_handler = new DB_Handler(v.getContext());
                AlertDialog.Builder ad= new AlertDialog.Builder(v.getContext());
                ad.setTitle("Delete");
                ad.setMessage("Do you want to delete the note?");
                final int id= Integer.parseInt(holder.noteid.getText().toString());
                ad.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db_handler.delete(id);

                        activity.finish();
//                        Fragment fragment = new MainMenu_Notes();
//                        FragmentTransaction fragmentTransaction = activity.getFragmentManager().beginTransaction();
//                        //fragmentTransaction.setCustomAnimations(FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
//                        // FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
//                        fragmentTransaction.replace(R.id.frame,fragment,"notes");
//                        fragmentTransaction.commitAllowingStateLoss();

                        activity.startActivity(activity.getIntent());
                    }
                });

                ad.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                ad.show();
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return myData.size()/2;
    }

}

class notesViewholder extends RecyclerView.ViewHolder{
    TextView notesName,noteid;
    ImageView bytesarray;
    public notesViewholder(View itemView) {
        super(itemView);
        notesName = (TextView) itemView.findViewById(R.id.tvNotesName);
        noteid = (TextView) itemView.findViewById(R.id.noteid);
        bytesarray=(ImageView) itemView.findViewById(R.id.camerabutton);
    }
}