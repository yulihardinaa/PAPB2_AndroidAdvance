package sv.ugm.komsi.broadcastreceiverb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class CustomReceiver extends BroadcastReceiver {
    static final String ACTIOON_CUSTOM_BROADCAST="sv.ugm.komsi.broadcastreceiverb.ACTION_CUSTOM_BROADCAST";

    @Override
    public void onReceive(Context context, Intent intent) {
//        // TODO: This method is called when the BroadcastReceiver is receiving
//        // an Intent broadcast.
//        throw new UnsupportedOperationException("Not yet implemented");
        String intentAction=intent.getAction();
        String message=null;
        switch (intentAction){
            case Intent.ACTION_POWER_CONNECTED:
                message="Power Connected!";
                break;
            case Intent.ACTION_POWER_DISCONNECTED:
                message="Power Disconnected!";
                break;
            case ACTIOON_CUSTOM_BROADCAST:
                message=intent.getStringExtra("DATA");
                break;

        }
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

}
