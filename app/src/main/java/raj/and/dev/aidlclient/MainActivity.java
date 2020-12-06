package raj.and.dev.aidlclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import raj.and.dev.aidlclient.databinding.ActivityMainBinding;
import raj.and.dev.common.IMyAidlInterface;

public class MainActivity extends AppCompatActivity {

    TextView tv1, tv2;
    AppCompatButton btnBind;
    private IMyAidlInterface aidlInterface;

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            aidlInterface = IMyAidlInterface.Stub.asInterface(iBinder);
            getSensorData();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private void getSensorData() {
        List<String> datalist;
        try {
            datalist = aidlInterface.screenOrientation();
            if (datalist!=null && datalist.size()==2) {
                tv1.setText(datalist.get(0));
                tv2.setText(datalist.get(1));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        tv1 = binding.tv1;
        tv2 = binding.tv2;
        btnBind = binding.btnBind;
    }

    public void bindService(View view) {
        Intent intent = new Intent("raj.and.dev.service.AIDL");
        bindService(convertImplicitIntentToExplicit(intent), connection, BIND_AUTO_CREATE);
    }

    private Intent convertImplicitIntentToExplicit(Intent intent) {
        PackageManager manager = getPackageManager();
        List<ResolveInfo> resolveInfoList = manager.queryIntentServices(intent, 0);
        if (resolveInfoList.size() != 1)
            return null;

        ResolveInfo info = resolveInfoList.get(0);
        ComponentName name = new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name);
        Intent expIntent = new Intent(intent);
        expIntent.setComponent(name);
        return expIntent;

    }
}
