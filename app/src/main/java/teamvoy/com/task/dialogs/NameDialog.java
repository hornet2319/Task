package teamvoy.com.task.dialogs;

import android.content.Context;

import teamvoy.com.task.Fragments.PersonalDataFragment;

/**
 * Created by lubomyrshershun on 8/19/15.
 */
public class NameDialog extends AbstractDialog {

    public NameDialog(Context context) {
        super(context);
    }

    @Override
    void write(String data) {
        mPrefs.setName(data);
        PersonalDataFragment.update();
    }
}
