package teamvoy.com.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;


public class FacebookActivity extends ActionBarActivity {

    private static final String target_url="https://www.facebook.com/login";


    private WebView mWebview;


    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);
        mToolbar=(Toolbar)findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Back");
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        mWebview = (WebView) findViewById(R.id.webview);
        mWebview.setWebViewClient(new MyBrowser());



        mWebview.loadUrl(target_url);

    }
    private class MyBrowser extends WebViewClient {

               String getID(){
            Profile profile = Profile.getCurrentProfile();
                   Log.d("Facebook id", profile.getId());
                   return profile.getId();
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if(!url.contains("login")){
                onBackPressed();
               // getID();

                Toast.makeText(getApplicationContext(),"connected successfully",Toast.LENGTH_SHORT).show();
            }
        /*    new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/"+getID(),
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
            //handle the result
                        }
                    }
            ).executeAsync(); */
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_facebook, menu);
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



}
