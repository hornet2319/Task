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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import teamvoy.com.task.dialogs.SearchDialog;
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
    private TextView image_tv;

    private EditText name_et,email_et;
    private String genderData="";
    private static String birthDayData="";;
    private static String imageData="";

    private View separator1,separator2,separator3,separator4,separator5,separator6;

    private Button profile_picture_btn;
    private Button birthDay_btn;

    private RadioGroup radioGroup;

    private static ImageView profile_picture_iv;

    private static PreferencesUtil mPrefs;
    private Context context;


    private boolean isEditModeEnable=false;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_data_management, container, false);
        setHasOptionsMenu(true);
        // initializing views
         profile_picture_iv=(ImageView)rootView.findViewById(R.id.data_img);

        context=getActivity();

        profile_picture_btn=(Button)rootView.findViewById(R.id.data_img_btn);

        birthDay_btn=(Button)rootView.findViewById(R.id.data_bday_btn);

        separator1=rootView.findViewById(R.id.separator1);
        separator2=rootView.findViewById(R.id.separator2);
        separator3=rootView.findViewById(R.id.separator3);
        separator4=rootView.findViewById(R.id.separator4);
        separator5=rootView.findViewById(R.id.separator5);
        separator6=rootView.findViewById(R.id.separator6);

        radioGroup=(RadioGroup)rootView.findViewById(R.id.radio_group);

        name_et=(EditText)rootView.findViewById(R.id.data_name_et);
        email_et=(EditText)rootView.findViewById(R.id.data_email_et);

        name_tv=(TextView)rootView.findViewById(R.id.data_name_tv);
        email_tv=(TextView)rootView.findViewById(R.id.data_email_tv);
        gender_tv=(TextView)rootView.findViewById(R.id.data_gender_tv);
        birthDay_tv=(TextView)rootView.findViewById(R.id.data_bday_tv);
        image_tv=(TextView)rootView.findViewById(R.id.data_img_tv);



        mPrefs=PreferencesUtil.getInstance(getActivity());
        imageData=mPrefs.getImage("http://graph.facebook.com/" + mPrefs.getID("null") + "/picture?type=large");
        final RadioButton radio_male=(RadioButton)rootView.findViewById(R.id.radio_male);
        final RadioButton radio_female=(RadioButton)rootView.findViewById(R.id.radio_female);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                if(radio_male.isChecked()) genderData="male";
                if(radio_female.isChecked()) genderData="female";

            }


        });

        //refresh data
        update();


        //set listener for buttons
       View.OnClickListener listener=new BtnListener();
        profile_picture_btn.setOnClickListener(listener);
        birthDay_btn.setOnClickListener(listener);

        return rootView;


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
         isEditModeEnable=true;
            turnEditModeOn();
            getActivity().invalidateOptionsMenu();


        }
        if (id == R.id.action_ok) {
            turnEditModeOff(true);
            isEditModeEnable=false;
            getActivity().invalidateOptionsMenu();

        }
        if (id == R.id.action_cancel) {
            turnEditModeOff(false);
            isEditModeEnable=false;
            getActivity().invalidateOptionsMenu();
        }


        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        MenuItem edit = menu.findItem(R.id.action_edit);
        MenuItem ok = menu.findItem(R.id.action_ok);
        MenuItem cancel = menu.findItem(R.id.action_cancel);
        if(isEditModeEnable)
        {
            edit.setVisible(false);
            ok.setVisible(true);
            cancel.setVisible(true);
        }
        else
        {
            edit.setVisible(true);
            ok.setVisible(false);
            cancel.setVisible(false);
        }
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
        name_tv.setText(mPrefs.getName("user"));
        email_tv.setText(mPrefs.getEmail("no email found"));
        gender_tv.setText(mPrefs.getGender("unknown"));
        if(birthDayData!="")
        birthDay_tv.setText(birthDayData);
        else birthDay_tv.setText(mPrefs.getBirthDay("unknown"));

        //setting profile image
        ImageLoader.getInstance().displayImage(imageData, profile_picture_iv);
    }
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
    //listener for buttons
    private class BtnListener implements View.OnClickListener {
        BirthdayDialog dialog;
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.data_img_btn:{
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                    break;
                }



                case R.id.data_bday_btn:{
                     dialog = new BirthdayDialog(context);
                    dialog.setMessage("Specify your birthDay, please");
                     birthDayData=dialog.show();
                    break;

                }


            }

        }
    }
    private void turnEditModeOn(){
        separator1.setVisibility(View.VISIBLE);
        separator2.setVisibility(View.VISIBLE);
        separator3.setVisibility(View.VISIBLE);
        separator4.setVisibility(View.VISIBLE);
        separator5.setVisibility(View.VISIBLE);
        separator6.setVisibility(View.VISIBLE);
        image_tv.setVisibility(View.VISIBLE);
        profile_picture_btn.setVisibility(View.VISIBLE);
        name_et.setVisibility(View.VISIBLE);
        email_et.setVisibility(View.VISIBLE);
        birthDay_btn.setVisibility(View.VISIBLE);
        radioGroup.setVisibility(View.VISIBLE);



    }
    private void turnEditModeOff(boolean changesConfirmed){
        separator1.setVisibility(View.GONE);
        separator2.setVisibility(View.GONE);
        separator3.setVisibility(View.GONE);
        separator4.setVisibility(View.GONE);
        separator5.setVisibility(View.GONE);
        separator6.setVisibility(View.GONE);
        image_tv.setVisibility(View.GONE);
        profile_picture_btn.setVisibility(View.GONE);
        name_et.setVisibility(View.GONE);
        email_et.setVisibility(View.GONE);
        birthDay_btn.setVisibility(View.GONE);
        radioGroup.setVisibility(View.GONE);
        if (changesConfirmed){
            //save name in preferences (when data is not null)
            if(name_et.getText().toString().length()>1) mPrefs.setName(name_et.getText().toString());
            //save email in preferences (when data is not null)
            if(email_et.getText().toString().length()>1) mPrefs.setEmail(email_et.getText().toString());
            //save image uri in preferences (when data is not null)
            if(imageData!="")mPrefs.setImage(imageData);
            if(genderData.length()>1)mPrefs.setGender(genderData);
            if(birthDayData.length()>1)mPrefs.setBirthDay(birthDayData);

           update();
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
                    imageData=result.toString();
                    update();


                }
        }
    }
}
