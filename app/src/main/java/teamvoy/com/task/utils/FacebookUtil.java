package teamvoy.com.task.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.facebook.CallbackManager;

/**
 * Created by lubomyrshershun on 8/17/15.
 */
public class FacebookUtil {

    private static FacebookUtil instance;

    private CallbackManager callbackManager;
    private FacebookUtil() {
        callbackManager=CallbackManager.Factory.create();
    }

    public static FacebookUtil getInstance(){
        if (instance==null) instance= new FacebookUtil();
        return instance;
    }
    //getters and setters
    public CallbackManager getCallbackManager() {
        return callbackManager;
    }
    public boolean isFacebookAvailable(Context context) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "Test; please ignore");
        intent.setType("text/plain");

        final PackageManager pm = context.getPackageManager();
        for(ResolveInfo resolveInfo: pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)){
            ActivityInfo activity = resolveInfo.activityInfo;
            // Log.i("actividad ->", activity.name);
            if (activity.name.contains("com.facebook.katana")) {
                return true;
            }
        }
        return false;
    }

}
