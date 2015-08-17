package teamvoy.com.task.Fragments;

import android.app.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ProgressBar;

import teamvoy.com.task.MainActivity;

/**
 * Created by Lubomyr Shershun on 04.08.2015.
 * l.sherhsun@gmail.com
 */
public abstract class AbstractFragment extends Fragment {
    protected static final String ARG_SECTION_NUMBER = "section_number";
    protected ProgressBar progress;
    protected int preLast;

    protected SwipeRefreshLayout swipe;


    public AbstractFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();
        updateList(true);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    abstract void updateList(boolean refresh);
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
