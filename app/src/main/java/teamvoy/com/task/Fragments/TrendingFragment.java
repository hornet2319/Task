package teamvoy.com.task.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import teamvoy.com.task.DetailActivity;
import teamvoy.com.task.R;
import teamvoy.com.task.utils.InternetUtil;
import teamvoy.com.task.utils.JSONUtil;
import teamvoy.com.task.utils.Recipe;
import teamvoy.com.task.utils.RecipeCustomAdapter;

/**
 * Created by Lubomyr Shershun on 04.08.2015.
 * l.sherhsun@gmail.com
 */
public class TrendingFragment extends AbstractFragment {
    protected List<Recipe> recipes;
    protected ListView listView=null;
    protected RecipeCustomAdapter adapter;
    private int page=1;
    private ATask atask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        listView=(ListView)rootView.findViewById(R.id.listview_main);
        progress=(ProgressBar)rootView.findViewById(R.id.list_progress);
        swipe = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        recipes= new ArrayList<>();


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
                        Log.d("Last", "Last");
                        updateList(false);
                        preLast = lastItem;
                    }
                }
            }
        });
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page=1;
                updateList(true);

            }
        });


        return rootView;
    }

    @Override
    public void onStart() {
        page=1;
        progress.setVisibility(View.VISIBLE);
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (atask != null && (atask.getStatus() == AsyncTask.Status.RUNNING)) {
            atask.cancel(true);
        }
    }

    @Override
    void updateList(boolean refresh) {
        InternetUtil internet=new InternetUtil(getActivity());
        atask = new ATask(refresh);
        if (atask.getStatus() != AsyncTask.Status.RUNNING) {
            if (internet.isConnected())
                atask.execute();
        }
    }

    private class ATask extends AsyncTask<Void,Void,Void>{
        JSONUtil util = new JSONUtil("","t");
        List<Recipe> list=new ArrayList<>();
        boolean refresh;

        public ATask(boolean refresh) {
            this.refresh=refresh;
        }

        @Override
        protected void onPreExecute() {
            if(progress.getVisibility()==View.VISIBLE) progress.setVisibility(View.GONE);
            swipe.setRefreshing(true);
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
            if (refresh) recipes.clear();
            recipes.addAll(list);
            adapter.notifyDataSetChanged();
            swipe.setRefreshing(false);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
