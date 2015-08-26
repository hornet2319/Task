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
    private String recipe_id;
    private Recipe recipe=null;
    private Toolbar mToolbar;
    private WebView mWeb;
    private ImageView main_img;
    private TextView description,bar_text,header,publisher;
    private RatingBar bar;
    private ATask aTask;
    private ShareButton shareButton;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private ShareLinkContent linkContent;
    private ProgressBar progress;
    private CustomTabsSession mCustomTabsSession;
    private CustomTabsClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_web);
        Intent i = getIntent();
        aTask = new ATask(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Back");

        //initialling views
        recipe_id = i.getStringExtra("id");
      //  progress = (ProgressBar) findViewById(R.id.progressBar);
     //   progress.setMax(100);

        mWeb = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mWeb.getSettings();
        webSettings.setJavaScriptEnabled(false);


        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);


        // Binds to the service.


        CustomTabsClient.bindCustomTabsService(this,"teamvoy.com.task", new CustomTabsServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                // mClient is no longer valid. This also invalidates sessions.
                mClient = null;
            }

            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                // mClient is now valid.
                mClient = client;
                aTask.execute();
            }

        });
// With a valid mClient.
            if(mClient!=null) mClient.warmup(0);





    }

    @Override
    protected void onStop() {
        super.onStop();
      //  if (aTask != null && (aTask.getStatus() == AsyncTask.Status.RUNNING)) {
      //      aTask.cancel(true);
      //  }
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
    private class MyWebViewClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            setValue(newProgress);

            super.onProgressChanged(view, newProgress);
        }
    }


    public void setValue(int progress) {
        if(progress==100)this.progress.setVisibility(View.GONE);
        this.progress.setProgress(progress);

    }
    private class ATask extends AsyncTask<Void,Void,Void>{
        private Context context;
        private ProgressDialog refreshDialog;
        private Activity activity;

        public ATask(Activity activity) {
            this.context = activity.getBaseContext();
            this.activity=activity;
        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          //  refreshDialog = new ProgressDialog(new ContextThemeWrapper(context, R.style.AppTheme));
            // Inform of the refresh
          //  refreshDialog.setMessage("Loading...");
            // Spin the wheel whilst the dialog exists
          //  refreshDialog.setIndeterminate(false);
            // Don't exit the dialog when the screen is touched
         //   refreshDialog.setCanceledOnTouchOutside(false);
            // Don't exit the dialog when back is pressed
          //  refreshDialog.setCancelable(true);
            // Show the dialog
         //   refreshDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            JSONUtil json=new JSONUtil();
            recipe=json.getRecipe(recipe_id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            refreshDialog.dismiss();
            mWeb.setWebChromeClient(new MyWebViewClient());
            mWeb.setWebViewClient(new CustomWebViewClient(recipe.getSource_url()));
           // if(recipe==null) onBackPressed();
         /*   ImageLoader.getInstance().displayImage(recipe.getImage_url(), main_img);

            header.setText(Html.fromHtml("<strong>" + recipe.getTitle() + "</strong>"));
            description.setText(recipe.getIngredients().replace("[\"", "").replaceAll("\",\"","\n").replace("\\n","")
            .replace("\"]","").replaceAll("/",""));
            bar.setRating(Float.parseFloat(recipe.getSocial_rank()));
            bar_text.setText("" + (Float.parseFloat(recipe.getSocial_rank()) / 10));
            publisher.setText(Html.fromHtml("<a href=\"" + recipe.getPublisher_url() + "\">" + recipe.getPublisher() + "</a>"));
            publisher.setClickable(true);
            publisher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(recipe.getPublisher_url()));
                    startActivity(i);
                }
            });
            Log.i("Recipelink",recipe.getSource_url());
            content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(recipe.getSource_url()))
                    .setContentTitle(recipe.getTitle())
                    .setImageUrl(Uri.parse(recipe.getImage_url()))
                    .build();
            shareButton.setShareContent(content); */
            linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(recipe.getSource_url()))
                    .setContentTitle(recipe.getTitle())
                    .setImageUrl(Uri.parse(recipe.getImage_url()))
                    .build();
     //   mWeb.loadUrl(recipe.getSource_url());
       //     progress.setVisibility(View.VISIBLE);
       //     progress.setProgress(0);


            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(getSession());
            builder.setToolbarColor(getResources().getColor(R.color.myPrimaryColor)).setShowTitle(true);
// Application exit animation, Chrome enter animation.
            builder.setStartAnimations(getApplicationContext(), R.anim.slide_in_right, R.anim.slide_out_left);
// vice versa
            builder.setExitAnimations(getApplicationContext(), R.anim.slide_in_left, R.anim.slide_out_right);
            builder.setCloseButtonIcon(
                    BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_back));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(activity, Uri.parse(recipe.getSource_url()));
        }
    }
    private CustomTabsSession getSession() {

            mCustomTabsSession = mClient.newSession(new CustomTabsCallback() {
                @Override
                public void onNavigationEvent(int navigationEvent, Bundle extras) {
                    Log.w("ChromeCustomTabs", "onNavigationEvent: Code = " + navigationEvent);
                }
            });

        return mCustomTabsSession;
    }

    private class CustomWebViewClient extends WebViewClient {
        private String currentUrl;

        public CustomWebViewClient(String currentUrl){
            this.currentUrl = currentUrl;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.equals(currentUrl)){
                view.loadUrl(url);
            }
            return true;
        }
    }
}
