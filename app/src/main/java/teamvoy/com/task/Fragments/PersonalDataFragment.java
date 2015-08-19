package teamvoy.com.task.Fragments;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.tv.TvInputService;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.nostra13.universalimageloader.core.ImageLoader;


import java.io.FileNotFoundException;
import java.io.InputStream;

import teamvoy.com.task.MainActivity;
import teamvoy.com.task.R;
import teamvoy.com.task.dialogs.AbstractDialog;
import teamvoy.com.task.dialogs.BirthdayDialog;
import teamvoy.com.task.dialogs.EmailDialog;
import teamvoy.com.task.dialogs.GenderDialog;
import teamvoy.com.task.dialogs.NameDialog;
import teamvoy.com.task.utils.PreferencesUtil;


/**
 * Created by lubomyrshershun on 8/18/15.
 */
public class PersonalDataFragment extends AbstractFragment {
    protected static final String ARG_SECTION_NUMBER = "section_number";
    private final int SELECT_PHOTO = 1;

    private static TextView name_tv;
    private static TextView email_tv;
    private static TextView gender_tv;
    private static TextView birthDay_tv;

    private Button profile_picture_btn;
    private Button name_btn;
    private Button email_btn;
    private Button gender_btn;
    private Button birthDay_btn;

    private static ImageView profile_picture_iv;

    private static PreferencesUtil mPrefs;
    private Context context;





    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        // initializing views
         profile_picture_iv=(ImageView)rootView.findViewById(R.id.data_img);

        context=getActivity();

        profile_picture_btn=(Button)rootView.findViewById(R.id.data_img_btn);
        name_btn=(Button)rootView.findViewById(R.id.data_name_btn);
        email_btn=(Button)rootView.findViewById(R.id.data_email_btn);
        gender_btn=(Button)rootView.findViewById(R.id.data_gender_btn);
        birthDay_btn=(Button)rootView.findViewById(R.id.data_bday_btn);

        name_tv=(TextView)rootView.findViewById(R.id.data_name_tv);
        email_tv=(TextView)rootView.findViewById(R.id.data_email_tv);
        gender_tv=(TextView)rootView.findViewById(R.id.data_gender_tv);
        birthDay_tv=(TextView)rootView.findViewById(R.id.data_bday_tv);

        mPrefs=PreferencesUtil.getInstance(getActivity());

        //refresh data
        update();


        //set listener for buttons
        View.OnClickListener listener=new BtnListener();
        profile_picture_btn.setOnClickListener(listener);
        name_btn.setOnClickListener(listener);
        email_btn.setOnClickListener(listener);
        gender_btn.setOnClickListener(listener);
        birthDay_btn.setOnClickListener(listener);

        return rootView;


    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }
    @Override
    public void updateList(boolean refresh) {
        //setting data in TextViews
    }
    //updating content
    public static void update(){
        name_tv.setText("Name: " + mPrefs.getName("user"));
        email_tv.setText("Email: " + mPrefs.getEmail("no email found"));
        gender_tv.setText("Gender: " + mPrefs.getGender("unknown"));
        birthDay_tv.setText("BirthDay: " + mPrefs.getBirthDay("unknown"));

        //setting profile image
        ImageLoader.getInstance().displayImage(mPrefs.getImage("http://graph.facebook.com/" + mPrefs.getID("null") + "/picture?type=large"), profile_picture_iv);
    }
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
    //listener for buttons
    private class BtnListener implements View.OnClickListener {
        AbstractDialog dialog;
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.data_img_btn:{
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                    break;
                }
                case R.id.data_name_btn:{
                    dialog=new NameDialog(context);
                    dialog.setMessage("Specify your name, please");
                    dialog.show();
                    break;
                }

                case R.id.data_email_btn:{
                    dialog=new EmailDialog(context);
                    dialog.setMessage("Specify your email, please");
                    dialog.show();
                    break;
                }

                case R.id.data_bday_btn:{
                     dialog = new BirthdayDialog(context);
                    dialog.setMessage("Specify your birthDay, please");
                    dialog.show();
                    break;

                }

                case R.id.data_gender_btn:{
                    dialog=new GenderDialog(context);
                    dialog.setMessage("Specify your gender, please");
                    dialog.show();
                    break;
                }

            }
            updateList(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateList(true);
        switch(requestCode) {
            case SELECT_PHOTO:
                if (data == null) {
                    return;
                } else {

                    Uri result = data.getData();
                    mPrefs.setImage(result.toString());


                }
        }
    }
}
