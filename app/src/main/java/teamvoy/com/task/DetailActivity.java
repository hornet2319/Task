package teamvoy.com.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import teamvoy.com.task.utils.JSONUtil;
import teamvoy.com.task.utils.Recipe;

public class DetailActivity extends ActionBarActivity {
    private String recipe_id;
    private Recipe recipe=null;
    private Toolbar mToolbar;
    private ImageView main_img;
    private TextView description,bar_text,header,publisher;
    private RatingBar bar;
    private ATask aTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent i=getIntent();
        aTask=new ATask(this);
        mToolbar=(Toolbar)findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Back");

        recipe_id=i.getStringExtra("id");
       main_img=(ImageView)findViewById(R.id.det_main_img);
        header=(TextView)findViewById(R.id.det_header);
        description=(TextView)findViewById(R.id.det_description);
        bar=(RatingBar)findViewById(R.id.det_rating);
        bar_text=(TextView)findViewById(R.id.det_rating_text);
        publisher=(TextView)findViewById(R.id.det_publisher);
       aTask.execute();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (aTask != null && (aTask.getStatus() == AsyncTask.Status.RUNNING)) {
            aTask.cancel(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_detail_acivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
    private class ATask extends AsyncTask<Void,Void,Void>{
        private Context context;
        private ProgressDialog refreshDialog;

        public ATask(Context context) {
            this.context = context;
        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            refreshDialog = new ProgressDialog(new ContextThemeWrapper(context, R.style.AppTheme));
            // Inform of the refresh
            refreshDialog.setMessage("Loading...");
            // Spin the wheel whilst the dialog exists
            refreshDialog.setIndeterminate(false);
            // Don't exit the dialog when the screen is touched
            refreshDialog.setCanceledOnTouchOutside(false);
            // Don't exit the dialog when back is pressed
            refreshDialog.setCancelable(true);
            // Show the dialog
            refreshDialog.show();
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
            refreshDialog.dismiss();
           // if(recipe==null) onBackPressed();
            ImageLoader.getInstance().displayImage(recipe.getImage_url(), main_img);

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
        }
    }
}
