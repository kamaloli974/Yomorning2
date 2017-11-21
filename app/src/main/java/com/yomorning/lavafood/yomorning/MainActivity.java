package com.yomorning.lavafood.yomorning;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.yomorning.lavafood.yomorning.adapters.HomeAdapter;
import com.yomorning.lavafood.yomorning.fragments.RailRestroFoodOrder;
import com.yomorning.lavafood.yomorning.fragments.RailRestroFoodOrderSystem;
import com.yomorning.lavafood.yomorning.fragments.StationInfo;
import com.yomorning.lavafood.yomorning.models.RailRestroVendorsModel;
import com.yomorning.lavafood.yomorning.organization.Home;
import com.yomorning.lavafood.yomorning.user.UserLogin;
import com.yomorning.lavafood.yomorning.user.UserProfile;

import java.security.MessageDigest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        RailRestroFoodOrder.OnFragmentInteractionListener, StationInfo.receiveListOfVendors,
        Home.OnCommunicationWithHomeFragment, View.OnClickListener, HomeAdapter.OnHomeAdapterCommunication, GoogleApiClient.OnConnectionFailedListener {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    FragmentManager fragmentManager;
    RailRestroFoodOrder railRestroFoodOrder;
    StationInfo stationInfo;
    TextView profileFullName,edit,profileEmail,profileMobile,signOut,profileFirstLetter;
    GoogleApiClient googleApiClient;

    Home homeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferences=getApplicationContext().getSharedPreferences("UserCredential",MODE_PRIVATE);

        String token=preferences.getString("token",null);
        if(token!=null){
            homeFragment=Home.newInstance();
            fragmentManager=getFragmentManager();
            android.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
            Fragment fragment=fragmentManager.findFragmentByTag("Home");
            GoogleSignInOptions signInOption=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            googleApiClient=new GoogleApiClient.Builder(this)
                    .enableAutoManage(this,this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API,signInOption)
                    .build();
            if(fragment!=null){
                fragmentTransaction.remove(fragment);
            }
            else{
                fragmentTransaction.add(R.id.home_activity_container,homeFragment,"Home");
                fragmentTransaction.commit();
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


            View view =navigationView.getHeaderView(0);
            profileFullName=view.findViewById(R.id.profile_full_name);
            edit=(TextView)view.findViewById(R.id.edit);
            profileEmail=(TextView)view.findViewById(R.id.profile_email);
            profileFirstLetter=(TextView)view.findViewById(R.id.name_first_letter);
            profileMobile=(TextView)view.findViewById(R.id.profile_mobile) ;
            signOut=(TextView)view.findViewById(R.id.sign_out);

            profileEmail.setText(preferences.getString("emailAddress","Email Address"));
            profileMobile.setText(preferences.getString("mobileNumber","Mobile Number"));
            String name=preferences.getString("firstName","Full");
            profileFirstLetter.setText(name.substring(0,1));
            profileFullName.setText(preferences.getString("firstName","Full")+" "+preferences.getString("lastName","Name"));
            navigationView.setNavigationItemSelectedListener(this);
            signOut.setOnClickListener(this);
            edit.setOnClickListener(this);
        }
        else{
            startActivity(new Intent(MainActivity.this, UserLogin.class));
            finish();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            if (getFragmentManager().getBackStackEntryCount()>0){
                getFragmentManager().popBackStack();
            }
            else {
                System.exit(1);
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.travel_food) {
            Fragment fragment=fragmentManager.findFragmentByTag("stationInfo");
            if (fragment==null){
                FragmentTransaction transaction=fragmentManager.beginTransaction();
                transaction.replace(R.id.home_activity_container,StationInfo.newInstance(),"stationInfo").commit();
            }
            else {
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.home_activity_container,fragment,"stationInfo");
                transaction.addToBackStack(null);
                transaction.commit();
            }
            // Handle the camera action
        }  else if (id == R.id.home_delivery) {

        } else if (id == R.id.ready_cook) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            Toast.makeText(this, "We can send gift to our friend", Toast.LENGTH_SHORT).show();
        }
        else if(id==R.id.history){
            startActivity(new Intent(MainActivity.this,OrderHistory.class));
        }
        else if(id==R.id.home){
            Fragment fragment=fragmentManager.findFragmentByTag("Home");
            if(fragment==null){
                Home homeFragment=Home.newInstance();
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.home_activity_container,homeFragment,"Home");
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else{
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.home_activity_container,fragment,"Home");
                transaction.addToBackStack(null);
                transaction.commit();
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onFragmentInteraction(ArrayList<RailRestroVendorsModel> list) {
        Log.e("FoodStore",list.toString());
        Fragment fragment=getFragmentManager().findFragmentByTag("railRestroFoodOrder");
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        if(fragment!=null){
            transaction.remove(fragment);
        }
        else{
            transaction.replace(R.id.home_activity_container,RailRestroFoodOrder.newInstance(list),"railRestroFoodOrder");
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onCommunicationWithHomeFragment(int userChoice) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sign_out:
                preferences.getString("signOut","signOut");
                SharedPreferences preferences=getApplicationContext().getSharedPreferences("UserCredential",MODE_PRIVATE);
                if((preferences.getString("media","")).equals("google")){

                    Auth.GoogleSignInApi.signOut(googleApiClient);
                    Log.e("google","google signOut");
                    signOut();
                }
                else if ((preferences.getString("media","")).equals("facebook")){
                    FacebookSdk.sdkInitialize(this);
                    LoginManager.getInstance().logOut();
                    Log.e("facebook","facebook signout");
                    signOut();
                }
                else{
                    Log.e("normalSignOut","normalSignOut");
                    Log.e("Facebook",preferences.getString("facebook","null"));
                    Log.e("Facebook",preferences.getString("google","null"));
                    signOut();

                }
                break;
            case R.id.edit:
                startActivity(new Intent(MainActivity.this, UserProfile.class));
                break;
        }
    }

    @Override
    public void userSelectedChoice(int userChoice) {
        switch (userChoice){
            case 1:
                Fragment fragment=fragmentManager.findFragmentByTag("stationInfo");
                if (fragment==null){
                    FragmentTransaction transaction=fragmentManager.beginTransaction();
                    transaction.replace(R.id.home_activity_container,StationInfo.newInstance(),"stationInfo").commit();
                }
                else {
                    FragmentTransaction transaction=getFragmentManager().beginTransaction();
                    transaction.replace(R.id.home_activity_container,fragment,"stationInfo");
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                break;
        }
    }

    private void signOut(){
        SharedPreferences.Editor editor=preferences.edit();
        editor.clear();
        editor.apply();
        startActivity(new Intent(this,UserLogin.class));
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}
