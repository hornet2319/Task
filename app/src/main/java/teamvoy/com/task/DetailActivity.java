package teamvoy.com.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import teamvoy.com.task.utils.JSONUtil;
import teamvoy.com.task.utils.Recipe;

public class DetailActivity extends AppCompatActivity {
    private String TAG = "DetailActivity";
    private String recipe_id;
    private Recipe recipe = null;
    private ShareLinkContent linkContent;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private ATask aTask;
    CustomTabsIntent customTabsIntent;
    private CustomTabsSession mCustomTabsSession;
    private CustomTabsClient mClient;
    private CustomTabsServiceConnection mConnection;
    private String mPackageNameToBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_web);
        bindCustomTabsService();
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(getSession());
        builder.setToolbarColor(getResources().getColor(R.color.myPrimaryColor));
        builder.setShowTitle(true);
        builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
        builder.setCloseButtonIcon(
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_back));
         customTabsIntent= builder.build();

        Intent i = getIntent();
        aTask = new ATask(this);

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        recipe_id = i.getStringExtra("id");
        aTask.execute();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_acivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id==R.id.action_share){
            if (ShareDialog.canShow(ShareLinkContent.class)) {

                if(linkContent!=null)
                    shareDialog.show(linkContent);
            }
        }

        return super.onOptionsItemSelected(item);
    }
    private void bindCustomTabsService() {
        if (mClient != null) return;
        if (TextUtils.isEmpty(mPackageNameToBind)) {
            mPackageNameToBind = "teamvoy.com.task";
        }
        mConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                mClient = client;
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mClient=null;
            }
        };
    }
    private class ATask extends AsyncTask<Void,Void,Void> {
        private Activity activity;
        private Context context;

        public ATask(Activity activity) {
            this.context = activity.getBaseContext();
            this.activity = activity;
        }
        @Override
        protected Void doInBackground(Void... params) {
            JSONUtil json=new JSONUtil();
            recipe=json.getRecipe(recipe_id);
            return null;
        }
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "onPostExecute ");

            customTabsIntent.launchUrl(activity, Uri.parse(recipe.getSource_url()));
        }
    }
    private CustomTabsSession getSession() {
        if (mClient == null) {
            mCustomTabsSession = null;
        } else if (mCustomTabsSession == null) {
            mCustomTabsSession = mClient.newSession(new CustomTabsCallback() {
                @Override
                public void onNavigationEvent(int navigationEvent, Bundle extras) {
                    Log.w(TAG, "onNavigationEvent: Code = " + navigationEvent);
                }
            });
        }
        return mCustomTabsSession;
    }
}
