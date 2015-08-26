package teamvoy.com.task.Fragments;

import android.app.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
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

import com.nostra13.universalimageloader.core.ImageLoader;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import teamvoy.com.task.MainActivity;
import teamvoy.com.task.R;
import teamvoy.com.task.dialogs.BirthdayDialog;
import teamvoy.com.task.utils.ImageLoaderUtil;
import teamvoy.com.task.utils.PreferencesUtil;


/**
 * Created by lubomyr shershun on 8/18/15.
 */
public class PersonalDataFragment extends AbstractFragment {
    protected static final String ARG_SECTION_NUMBER = "section_number";
    private final int SELECT_PHOTO = 1;
    protected static final int YOUR_SELECT_PICTURE_REQUEST_CODE = 123;

    private static TextView name_tv;
    private static TextView email_tv;
    private static TextView gender_tv;
    private static TextView birthDay_tv;
    private TextView image_tv;

    private static EditText name_et;
    private static EditText email_et;
    private String genderData="";
    public static String birthDayData="";
    private static String imageData="";

    private View separator1,separator2,separator3,separator4,separator5,separator6;

    private Button profile_picture_btn;
    private Button birthDay_btn;

    private RadioGroup radioGroup;

    private static ImageView profile_picture_iv;

    private static PreferencesUtil mPrefs;



    private boolean isEditModeEnable=false;

    private static Context context;

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


        birthDay_tv.setOnClickListener(new View.OnClickListener() {
            BirthdayDialog dialog;
            @Override
            public void onClick(View view) {
                dialog = new BirthdayDialog(context);
                dialog.setMessage("Specify your birthDay, please");
                dialog.show();
            }
        });
        profile_picture_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditModeEnable){
                    openImageIntent();
                }
            }
        });


        mPrefs=PreferencesUtil.getInstance(getActivity());
        imageData=mPrefs.getImage("http://graph.facebook.com/" + mPrefs.getID("null") + "/picture?type=large");
        final RadioButton radio_male=(RadioButton)rootView.findViewById(R.id.radio_male);
        final RadioButton radio_female=(RadioButton)rootView.findViewById(R.id.radio_female);

        //listener for radioButtons
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                if(radio_male.isChecked()) genderData="male";
                if(radio_female.isChecked()) genderData="female";
                updateGender();

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

    }
    //updating content
    public static void update(){
        name_et.setHint(mPrefs.getName("username"));
        email_et.setHint(mPrefs.getEmail("email"));
        name_tv.setText(mPrefs.getName("user"));
        email_tv.setText(mPrefs.getEmail("no email found"));
        gender_tv.setText(mPrefs.getGender("unknown"));
       birthDay_tv.setText(mPrefs.getBirthDay("unknown"));

        //setting profile image
        ImageLoaderUtil imageLoaderUtil= new ImageLoaderUtil(context);
        ImageLoader imageLoader = imageLoaderUtil.getImageLoader();
        imageLoader.displayImage(imageData, profile_picture_iv);
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
                    dialog.show();
                    //Log.d("PersonalDataFragm data",data);
                    // birthDayData=data;

                    break;

                }


            }

        }
    }
    private Uri outputFileUri;

    //image change listener
    private void openImageIntent() {
// Determine Uri of camera image to save.
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "pic" + File.separator);
        root.mkdirs();
        final String fname = "img_"+ System.currentTimeMillis() + ".jpg";
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getActivity().getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, YOUR_SELECT_PICTURE_REQUEST_CODE);
    }

    private void turnEditModeOn(){
        separator1.setVisibility(View.VISIBLE);
        separator2.setVisibility(View.VISIBLE);
        separator3.setVisibility(View.VISIBLE);
        separator4.setVisibility(View.VISIBLE);
        separator5.setVisibility(View.VISIBLE);
        separator6.setVisibility(View.VISIBLE);
        image_tv.setVisibility(View.VISIBLE);
      //  profile_picture_btn.setVisibility(View.VISIBLE);
        name_et.setVisibility(View.VISIBLE);
        email_et.setVisibility(View.VISIBLE);
       // birthDay_btn.setVisibility(View.VISIBLE);
        radioGroup.setVisibility(View.VISIBLE);
        name_tv.setVisibility(View.GONE);
        email_tv.setVisibility(View.GONE);
        gender_tv.setVisibility(View.GONE);



    }
    private void turnEditModeOff(boolean changesConfirmed){
        separator1.setVisibility(View.GONE);
        separator2.setVisibility(View.GONE);
        separator3.setVisibility(View.GONE);
        separator4.setVisibility(View.GONE);
        separator5.setVisibility(View.GONE);
        separator6.setVisibility(View.GONE);
        image_tv.setVisibility(View.GONE);
       // profile_picture_btn.setVisibility(View.GONE);
        name_et.setVisibility(View.GONE);
        email_et.setVisibility(View.GONE);
      //  birthDay_btn.setVisibility(View.GONE);
        radioGroup.setVisibility(View.GONE);
        name_tv.setVisibility(View.VISIBLE);
        email_tv.setVisibility(View.VISIBLE);
        gender_tv.setVisibility(View.VISIBLE);

        if (changesConfirmed){
            //save name in preferences (when data is not null)
            if(name_et.getText().toString().length()>1) mPrefs.setName(name_et.getText().toString());
            //save email in preferences (when data is not null)
            if(email_et.getText().toString().length()>1) mPrefs.setEmail(email_et.getText().toString());
            //save image uri in preferences (when data is not null)
            if(imageData!="")mPrefs.setImage(imageData);
            if(genderData.length()>1)mPrefs.setGender(genderData);
            if(birthDayData.length()>1)mPrefs.setBirthDay(birthDayData);


        }
        imageData=mPrefs.getImage("null");
        update();
    }
    //update BirthDay;
    public static void updateBirth(){
        birthDay_tv.setText(birthDayData);
    }

    private void updateGender(){
        gender_tv.setText(genderData);
    }

    //checking camera orientation
    public void getCameraPhotoOrientation(String file) {
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, bounds);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(file, opts);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) :  ExifInterface.ORIENTATION_NORMAL;

        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
            Log.d("RotationAngle",""+rotationAngle);
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        //saving rotatedBitmap
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "pic" + File.separator);
        File img=new File(file);
        if (img.exists ()) img.delete ();
        try {
            FileOutputStream out = new FileOutputStream(img);
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateList(true);
        String filePath;
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == YOUR_SELECT_PICTURE_REQUEST_CODE) {
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                Uri selectedImageUri;
                if (isCamera) {
                    selectedImageUri = outputFileUri;

                } else {
                    selectedImageUri = data == null ? null : data.getData();
                }
                Log.d("Camera",selectedImageUri.toString());

                getCameraPhotoOrientation(selectedImageUri.getPath());

                imageData=selectedImageUri.toString();
                update();
            }
        }
    }
}
