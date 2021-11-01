package com.example.cz2006;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cz2006.ui.busarrival.BusarrivalFragment;
import com.example.cz2006.ui.covidsitrep.CovidSitRepFragment;
import com.example.cz2006.ui.guide.GuideFragment;
import com.example.cz2006.ui.meet.MeetFragment;
import com.example.cz2006.ui.trainservice.TrainServiceFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private boolean viewIsAtHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.menu_meet);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );


        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        displayView(R.id.nav_meet);

        View headerView = navigationView.getHeaderView(0);
        TextView greeting = headerView.findViewById(R.id.greetingText);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if(timeOfDay >= 0 && timeOfDay < 12){
            greeting.setText("Good Morning");
        } else if(timeOfDay >= 12 && timeOfDay < 16){
            greeting.setText("Good Afternoon");
        }else if(timeOfDay >= 16 && timeOfDay < 24) {
            greeting.setText("Good Evening");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displayView(item.getItemId());
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private boolean firstTimeLoaded = false;
    // Navigating to different fragments based on input
    public void displayView(int viewId) {
        Fragment fragment = null;
        Fragment containerFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);

        String tagName = "";
        String title = "";

        switch (viewId) {
            case R.id.nav_meet:
                fragment = new MeetFragment();
                title = getString(R.string.menu_meet);
                tagName = "Meet_Tag";
                viewIsAtHome = true;
                break;
            case R.id.nav_spaceout:
                fragment = new CovidSitRepFragment();
                title = getString(R.string.menu_covidsitrep);
                tagName = "CovidSitRep_Tag";
                viewIsAtHome = false;
                break;
            case R.id.nav_busarrival:
                fragment = new BusarrivalFragment();
                title = getString(R.string.menu_busarrival);
                tagName = "Bus_Tag";
                viewIsAtHome = false;
                break;
            case R.id.nav_trainservice:
                fragment = new TrainServiceFragment();
                title = getString(R.string.menu_trainservice);
                tagName = "Train_Tag";
                viewIsAtHome = false;
                break;
            case R.id.nav_guide:
                fragment = new GuideFragment();
                title = getString(R.string.menu_guide);
                tagName = "Guide_Tag";
                viewIsAtHome = false;
                break;
        }

        String container = containerFragment.getClass().getName();
        String currentFrag = fragment.getClass().getName();

        Log.d("displayView: ", container);
        Log.d("displayView: ", currentFrag);

        // Pressing on the same navigation wont reload
        if (container.equalsIgnoreCase(currentFrag)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        // During first time load, dont pop, check if current fragment is meet fragment
        else if (firstTimeLoaded && currentFrag.equalsIgnoreCase("com.example.cz2006.ui.meet.MeetFragment")) {
            Log.d("displayView: ", "YSA");
            getSupportFragmentManager().popBackStack("Meet_Tag", 0);
        }
        else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.nav_host_fragment_content_main, fragment);
            Log.d("displayView: fragment ", tagName);
            ft.addToBackStack(tagName);
            ft.commit();
        }

        firstTimeLoaded = true;

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (!viewIsAtHome) { //if the current view is not the Meet fragment
            getSupportActionBar().setTitle(R.string.menu_meet);
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            moveTaskToBack(true);  //If view is in Meet fragment, exit application
        }
    }

}