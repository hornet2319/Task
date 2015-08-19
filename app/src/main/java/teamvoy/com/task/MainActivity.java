package teamvoy.com.task;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import teamvoy.com.task.Fragments.PersonalDataFragment;
import teamvoy.com.task.dialogs.SearchDialog;
import teamvoy.com.task.Fragments.AbstractFragment;
import teamvoy.com.task.Fragments.NavigationDrawerFragment;
import teamvoy.com.task.Fragments.TopFragment;
import teamvoy.com.task.Fragments.TrendingFragment;
import teamvoy.com.task.utils.InternetUtil;
import teamvoy.com.task.utils.PreferencesUtil;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    static final String ARG_SECTION_NUMBER = "section_number";
    static final String appPackageName = "com.facebook.katana";
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    private AbstractFragment fragment;
    private String mTitle;
    private CallbackManager callbackManager;
    private List<String> permissionNeeds;
    private PreferencesUtil mPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        permissionNeeds= Arrays.asList("public_profile",  "email", "user_birthday", "user_friends","user_photos");
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        //setting actionBar
        setSupportActionBar(mToolbar);
        mPrefs=PreferencesUtil.getInstance(this);
        callbackManager = CallbackManager.Factory.create();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

        InternetUtil inet=new InternetUtil(this);
        if(!inet.isConnected()){
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme));
            // Tell the user what happened
            builder.setMessage("Unable to reach server.\nPlease check your connectivity.")
                    // Alert title
                    .setTitle("Connection Error")
                            // Can't exit via back button
                    .setCancelable(false)
                            // Create exit button
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // Exit the application
                            finish();
                        }
                    });
            // Create dialog from builder
            AlertDialog alert = builder.create();
            // Show dialog
            alert.show();
            // Center the message of the dialog
            ((TextView)alert.findViewById(android.R.id.message)).setGravity(Gravity.CENTER);
            // Center the title of the dialog
            ((TextView)alert.findViewById((getResources().getIdentifier("alertTitle", "id", "android")))).setGravity(Gravity.CENTER);
        }
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "teamvoy.com.task",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }
    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        //AppEventsLogger.activateApp(getApplicationContext());
    }
    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        //AppEventsLogger.deactivateApp(getApplicationContext());
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, getFragment(position + 1))
                .commit();
    }
    private void facebookLogin(){
        LoginManager.getInstance().logInWithReadPermissions(
                this,
                permissionNeeds);
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResults) {

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResults.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        // Application code

                                        Log.v("LoginActivity", response.toString());
                                        try {
                                            mPrefs.setID(object.getString("id"));
                                            mPrefs.setName(object.getString("name"));
                                            mPrefs.setEmail(object.getString("email"));
                                            mPrefs.setGender(object.getString("gender"));
                                            mPrefs.setBirthDay(object.getString("birthday"));
                                            mPrefs.setImage("http://graph.facebook.com/" + mPrefs.getID("null") + "/picture?type=large");
                                            PersonalDataFragment.update();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();


                    }
                    @Override
                    public void onCancel() {

                        Log.e("dd","facebook login canceled");

                    }


                    @Override
                    public void onError(FacebookException e) {



                        Log.e("dd", "facebook login failed error");

                    }
                });
    }
    private AbstractFragment getFragment(int i) {
        fragment = null;
        Bundle args = new Bundle();
        switch (i) {
            case 1:

                fragment = new TopFragment();

                args.putInt(ARG_SECTION_NUMBER, i);
                fragment.setArguments(args);
                break;
            case 2:

                fragment = new TrendingFragment();
                args.putInt(ARG_SECTION_NUMBER, i);
                fragment.setArguments(args);
                break;
            case 3:
                fragment = new PersonalDataFragment();
                args.putInt(ARG_SECTION_NUMBER, i);
                fragment.setArguments(args);
                break;

            default:
                fragment = new TopFragment();
                args.putInt(ARG_SECTION_NUMBER, i);
                fragment.setArguments(args);
                break;
        }
        return fragment;
    }
    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;

        }
    }
    public void restoreActionBar() {

        getSupportActionBar().setTitle(mTitle);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
          getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            SearchDialog dialog=new SearchDialog(this);
            dialog.show();
        }
        if(id==R.id.action_login){
           facebookLogin();
        }


        return super.onOptionsItemSelected(item);
    }


}
