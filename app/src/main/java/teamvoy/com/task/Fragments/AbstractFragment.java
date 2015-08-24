package teamvoy.com.task.Fragments;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ProgressBar;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import teamvoy.com.task.MainActivity;
import teamvoy.com.task.utils.PreferencesUtil;

/**
 * Created by Lubomyr Shershun on 04.08.2015.
 * l.sherhsun@gmail.com
 */
public abstract class AbstractFragment extends Fragment {
    protected static final String ARG_SECTION_NUMBER = "section_number";
    protected ProgressBar progress;
    protected int preLast;
    private List<String> permissionNeeds;
    private PreferencesUtil mPrefs;
    protected CallbackManager callbackManager;
    protected SwipeRefreshLayout swipe;


    public AbstractFragment() {
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateList(true);
    }
    protected void facebookLogin(){
        mPrefs=PreferencesUtil.getInstance(getActivity());
        permissionNeeds= Arrays.asList("public_profile", "email", "user_birthday", "user_friends", "user_photos");
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
