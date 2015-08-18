package teamvoy.com.task.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.media.tv.TvInputService;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;



import teamvoy.com.task.MainActivity;
import teamvoy.com.task.R;
import teamvoy.com.task.utils.FacebookUtil;

/**
 * Created by lubomyrshershun on 8/18/15.
 */
public class LoginFragment extends AbstractFragment {
    protected static final String ARG_SECTION_NUMBER = "section_number";

    private FacebookUtil facebook;
    private TextView info;
    private LoginButton loginButton;

    private CallbackManager callbackManager;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

     callbackManager = CallbackManager.Factory.create();

        info=(TextView)rootView.findViewById(R.id.facebook_info);
        loginButton=(LoginButton)rootView.findViewById(R.id.facebook_login_button);
        loginButton.setReadPermissions("user_friends", "email");

        loginButton.setFragment(this);

        loginButton.registerCallback(callbackManager,new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed.");
            }
        });
        /* make the API call */
        return rootView;


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebook.getCallbackManager().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    void updateList(boolean refresh) {

    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
