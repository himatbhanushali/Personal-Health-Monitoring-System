package com.krishnchinya.personalhealthmonitoringsystem.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.krishnchinya.personalhealthmonitoringsystem.R;
import com.krishnchinya.personalhealthmonitoringsystem.fragments.MainMenu_Diet;
import com.krishnchinya.personalhealthmonitoringsystem.fragments.MainMenu_Home;
import com.krishnchinya.personalhealthmonitoringsystem.fragments.MainMenu_Medication;
import com.krishnchinya.personalhealthmonitoringsystem.fragments.MainMenu_Notes;
import com.krishnchinya.personalhealthmonitoringsystem.fragments.MainMenu_Vitals;
import com.krishnchinya.personalhealthmonitoringsystem.other.GlobalVars;


/**
 * Created by KrishnChinya on 2/24/17.
 */

public class MainMenu extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    TextView log_name, log_email;
    NavigationView nav_view;
    Handler handler;
    View view;
    GlobalVars globalVars;
    Intent intent;


    String[] activitytitle;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private boolean shouldLoadHomeFragOnBackPress = true;

    public static String CURRENT_TAG = TAG_HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        nav_view = (NavigationView) findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);

        handler = new Handler();

        view = nav_view.getHeaderView(0);

        log_name = (TextView) view.findViewById(R.id.loggedinname);
        log_email = (TextView) view.findViewById(R.id.loggedinemail);

        activitytitle = getResources().getStringArray(R.array.nav_item_activity_titles);

        //loading the header items such as image, profile pic, name and email ID
        loadNavigationHeader();

        //setting up navigation view
        navigationView();

        if(savedInstanceState==null)
        {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }

    }

    private void loadNavigationHeader(){
        globalVars = (GlobalVars)getApplicationContext();
        //set text and email id for the textviews
        log_name.setText(globalVars.getUsername());
        log_email.setText(globalVars.getMailid());

    }

    private void navigationView(){
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;

                    case R.id.nav_vital_signs:
                        navItemIndex = 1;
                        CURRENT_TAG = "vital_signs";
                        break;
                    case R.id.nav_medication:
                        navItemIndex = 2;
                        CURRENT_TAG = "medication";
                        break;
                    case R.id.nav_notes:
                        navItemIndex = 3;
                        CURRENT_TAG = "notes";
                        break;
                    case R.id.nav_diet:
                        navItemIndex = 4;
                        CURRENT_TAG = "diet";
                        break;
                    case R.id.nav_updateprofile:
                        // launch new intent instead of loading fragment
                        intent  = new Intent(MainMenu.this,UpdateRegistration.class);
                        startActivityForResult(intent,1);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_logout:
                        intent = new Intent(MainMenu.this,Login_Activity.class);
                        startActivity(intent);
                        //setResult(Activity.RESULT_CANCELED,intent);
                        finish();
                        return true;
                    //insert cases for others
                    default:navItemIndex = 0;
                }

                if(item.isChecked())
                {
                    item.setChecked(false);
                }else
                {
                    item.setChecked(true);
                }

                item.setChecked(true);

                loadHomeFragment();

                return true;
            }

        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        //drawerLayout.DrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

    }


    private void loadHomeFragment(){

        //set the selected menu to checked in the item list
        nav_view.getMenu().getItem(navItemIndex).setChecked(true);

        //set the title on the tool bar
        getSupportActionBar().setTitle(activitytitle[navItemIndex]);

        //using threads to load the fragment for smooth transistion if the data is huge.
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getFragement();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                //fragmentTransaction.setCustomAnimations(FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
                       // FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                fragmentTransaction.replace(R.id.frame,fragment,CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();

            }
        };

        // If Runnable is not null, then add to the message queue
        if (runnable != null) {
            handler.post(runnable);
        }

        //Closing drawer on item click
        drawerLayout.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();

    }

    private Fragment getFragement()
    {
        switch (navItemIndex)
        {
            case 0:
                return new MainMenu_Home();
            case 1:
                return new MainMenu_Vitals();
            case 2:
                return new MainMenu_Medication();
            case 3:
                return new MainMenu_Notes();
            case 4:
                return new MainMenu_Diet();
            default:
                return new MainMenu_Home();
        }

    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }else
            {
               // CURRENT_TAG = TAG_HOME;
                //loadHomeFragment();
                return;
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.mainmenu, menu);
        }

        // when fragment is notifications, load the menu created for notifications
        if (navItemIndex == 3) {
            getMenuInflater().inflate(R.menu.notification, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
            return true;
        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_mark_all_read) {
            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

}
