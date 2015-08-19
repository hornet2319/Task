package teamvoy.com.task.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import teamvoy.com.task.R;

/**
 * Created by lubomyrshershun on 8/19/15.
 */
public class GenderDialog extends AbstractDialog {
    String data="male";
    public GenderDialog(Context context) {
        super(context);
    }
    public void show(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);


        LayoutInflater layoutInflater = (LayoutInflater)context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.dialog_gender,null);
        dialog.setView(view);
        dialog.setTitle("personal data");
        dialog.setMessage(message);
        dialog.setIcon(R.drawable.ic_settings);

        final RadioButton radio_male=(RadioButton)view.findViewById(R.id.radio_male);
        final RadioButton radio_female=(RadioButton)view.findViewById(R.id.radio_female);


        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               write(data);
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        RadioGroup radioGroup = (RadioGroup)view.findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                  @Override
                                                  public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                      // checkedId is the RadioButton selected

                                                      if(radio_male.isChecked()) data="male";
                                                      else data="female";

                                                  }


                                              });

        dialog.show();

    }
    @Override
    void write(String data) {
    mPrefs.setGender(data);
    }

}
