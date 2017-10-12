import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.deverdie.templete.MainActivity;

/**
 * Created by USER275 on 10/10/2017.
 */

public class BootUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
