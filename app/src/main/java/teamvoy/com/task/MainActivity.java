package teamvoy.com.task;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import teamvoy.com.task.dialogs.SearchDialog;
import teamvoy.com.task.Fragments.AbstractFragment;
import teamvoy.com.task.Fragments.NavigationDrawerFragment;
import teamvoy.com.task.Fragments.TopFragment;
import teamvoy.com.task.Fragments.TrendingFragment;
import teamvoy.com.task.utils.InternetUtil;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    static final String ARG_SECTION_NUMBER = "section_number";
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    private AbstractFragment fragment;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

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
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, getFragment(position + 1))
                .commit();
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

        return super.onOptionsItemSelected(item);
    }


}
