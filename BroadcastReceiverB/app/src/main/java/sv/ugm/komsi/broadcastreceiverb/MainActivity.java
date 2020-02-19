package sv.ugm.komsi.broadcastreceiverb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private CustomReceiver mReceiver= new CustomReceiver();
    private ComponentName mRecieverComponentName;
    private PackageManager mPackageManager;

    Button broadcastButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        broadcastButton=findViewById(R.id.broadcastButton);
        mRecieverComponentName=new ComponentName(this, CustomReceiver.class);
        mPackageManager=getPackageManager();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mReceiver,new IntentFilter(
                        CustomReceiver.ACTIOON_CUSTOM_BROADCAST));
        broadcastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCustomBroadcast();
            }

        });
    }
    private void sendCustomBroadcast(){
        Intent broadcastIntent=new Intent(CustomReceiver.ACTIOON_CUSTOM_BROADCAST);
        broadcastIntent.putExtra("DATA","Data Broadcast!");
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }


    protected void onStart() {
        super.onStart();
        mPackageManager.setComponentEnabledSetting(mRecieverComponentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

    }

    protected void onStop() {
        super.onStop();
        mPackageManager.setComponentEnabledSetting(mRecieverComponentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }
}
