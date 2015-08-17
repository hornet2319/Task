package teamvoy.com.task.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import teamvoy.com.task.DetailActivity;
import teamvoy.com.task.R;
import teamvoy.com.task.utils.InternetUtil;
import teamvoy.com.task.utils.JSONUtil;
import teamvoy.com.task.utils.Recipe;
import teamvoy.com.task.utils.RecipeCustomAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class ResultActivityFragment extends Fragment {
    private ListView listView;
    private List<Recipe> recipes=new ArrayList<>();
    private RecipeCustomAdapter adapter;
    private int preLast;
    private int page = 1;
    private Intent intent;
    private TextView tv;
    private ATask atask;


    public ResultActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_result, container, false);
        recipes=new ArrayList<>();
        listView = (ListView) rootView.findViewById(R.id.listview_main);
        tv=(TextView)rootView.findViewById(R.id.result_text);

        intent=getActivity().getIntent();

        tv.setText("Results for \""+intent.getStringExtra("ingredients").replaceAll(",",", ")+"\":");

        adapter = new RecipeCustomAdapter(getActivity(), recipes, true);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("id", adapter.getRecipeConverted(position).getRecipe_id());
                getActivity().startActivity(intent);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final int lastItem = firstVisibleItem + visibleItemCount;
                if (lastItem == totalItemCount) {
                    if (preLast != lastItem) { //to avoid multiple calls for last item
                        updateList(false);
                        preLast = lastItem;
                    }
                }
            }
        });

        updateList(true);
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (atask != null && (atask.getStatus() == AsyncTask.Status.RUNNING)) {
            atask.cancel(true);
        }
    }

    void updateList(boolean refresh) {
        InternetUtil internet=new InternetUtil(getActivity());
        atask = new ATask(refresh);
        if (atask.getStatus() != AsyncTask.Status.RUNNING) {
            if (internet.isConnected())
                atask.execute();
        }
    }
    private class ATask extends AsyncTask<Void,Void,Void>{
        JSONUtil util = new JSONUtil(intent.getStringExtra("ingredients"),"r");
        boolean refresh;
        List<Recipe> list=new ArrayList<>();
        private ProgressDialog refreshDialog;

        public ATask(boolean refresh) {
            this.refresh=refresh;
        }

        @Override
        protected void onPreExecute() {
            refreshDialog = new ProgressDialog(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
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
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            list.addAll(util.getSearchList(page++));


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            refreshDialog.dismiss();
            if (refresh) recipes.clear();
            recipes.addAll(list);
            adapter.notifyDataSetChanged();
        }
    }
}