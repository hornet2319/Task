package teamvoy.com.task.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import teamvoy.com.task.R;
import teamvoy.com.task.utils.PreferencesUtil;

/**
 * Created by lubomyrshershun on 8/19/15.
 */
public abstract class AbstractDialog {
    protected Context context;
    protected String message;
    protected String data;

    protected PreferencesUtil mPrefs;

    protected AbstractDialog(Context context) {
        this.context = context;
        mPrefs=PreferencesUtil.getInstance(context);

    }

    abstract void write(String data);
  //  public void setData(String data){
   //     this.data=data;
  //  }
    public void setMessage(String message){
        this.message=message;
    }
    public void show() {

        final AlertDialog.Builder absDialog = new AlertDialog.Builder(context);


        LayoutInflater layoutInflater = (LayoutInflater)context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final EditText editText=new EditText(context);
        absDialog.setView(editText);
        absDialog.setTitle("personal data");
        absDialog.setMessage(message);
        absDialog.setIcon(R.drawable.ic_settings);


        absDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                data=editText.getText().toString();
                write(data);
            }
        });
        absDialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });



        absDialog.show();
    }
}
