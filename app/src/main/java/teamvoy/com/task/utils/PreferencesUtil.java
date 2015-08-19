package teamvoy.com.task.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lubomyrshershun on 8/19/15.
 */
public class PreferencesUtil{
    private static String PREFERENCE_FILE_KEY= "personal_data";
    private Context context;
    private SharedPreferences mPreferences;
    private static PreferencesUtil instance;
    SharedPreferences.Editor editor;

    private PreferencesUtil(Context context) {
        this.context = context;
        mPreferences=context.getSharedPreferences(PREFERENCE_FILE_KEY,Context.MODE_PRIVATE);
         editor = mPreferences.edit();
    }
    public static PreferencesUtil getInstance(Context context){
        if(instance==null) instance=new PreferencesUtil(context);
        return instance;
    }

    public void setID(String id){
        editor.putString("id", id);
        editor.commit();
    }
    public void setName(String name){
        editor.putString("name", name);
        editor.commit();
    }
    public void setEmail(String email){
        editor.putString("email", email);
        editor.commit();
    }
    public void setGender(String gender){
        editor.putString("gender", gender);
        editor.commit();
    }
    public void setBirthDay(String birthDay){
        editor.putString("birthDay", birthDay);
        editor.commit();
    }
    public void setImage(String path){
        editor.putString("path", path);
        editor.commit();
    }
    public String getID(String defValue){
        return mPreferences.getString("id",defValue);
    }
    public String getName(String defValue){
        return mPreferences.getString("name",defValue);
    }
    public String getEmail(String defValue){
        return mPreferences.getString("email",defValue);
    }
    public String getGender(String defValue){
        return mPreferences.getString("gender",defValue);
    }
    public String getBirthDay(String defValue){
        return mPreferences.getString("birthDay",defValue);
    }
    public String getImage(String defValue){return mPreferences.getString("path",defValue);}
}
