package teamvoy.com.task.dialogs;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.DialogPreference;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import teamvoy.com.task.R;
import teamvoy.com.task.ResultActivity;

public class SearchDialog {
    private Context context;
    String text="";

    public SearchDialog(Context context) {
        this.context = context;
    }
    public void show() {

        final AlertDialog.Builder searchDialog = new AlertDialog.Builder(context);


        LayoutInflater layoutInflater = (LayoutInflater)context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.dialog_search,null);
        searchDialog.setView(view);
        searchDialog.setTitle(R.string.search_header);
        searchDialog.setMessage(R.string.search_message);
        searchDialog.setIcon(R.drawable.ic_action_search_dark);
        final EditText et=(EditText)view.findViewById(R.id.sea_et);

        final TextView tv=(TextView)view.findViewById(R.id.sea_tv);
        Button btn=(Button)view.findViewById(R.id.sea_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!text.contains(et.getText().toString()))
                    text+=et.getText().toString()+"\n";
                    tv.setText(text);
                et.setText("");
            }
        });
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    if (!text.contains(et.getText().toString()))
                    text+=et.getText().toString()+"\n";
                    tv.setText(text);
                    et.setText("");

                }
                return false;
            }
        });
        searchDialog.setPositiveButton("Find", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(context, ResultActivity.class);
                Log.d("INGredients",text.replaceAll("\\n",","));

                 intent.putExtra("ingredients",method(text.replaceAll("\\n",",")) );
                context.startActivity(intent);
            }
        });



        searchDialog.show();
    }
    public String method(String str) {
        if (str.length() > 0 && str.charAt(str.length()-1)==',') {
            str = str.substring(0, str.length()-1);
        }
        return str;
    }
}

