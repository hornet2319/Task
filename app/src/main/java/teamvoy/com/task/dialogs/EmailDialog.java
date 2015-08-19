package teamvoy.com.task.dialogs;

import android.content.Context;

import teamvoy.com.task.Fragments.PersonalDataFragment;

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
        PersonalDataFragment.update();
    }
}
