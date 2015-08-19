package teamvoy.com.task.dialogs;

import android.content.Context;

/**
 * Created by lubomyrshershun on 8/19/15.
 */
public class EmailDialog extends AbstractDialog {

    public EmailDialog(Context context) {
        super(context);
    }

    @Override
    void write(String data) {
        mPrefs.setEmail(data);
    }
}
