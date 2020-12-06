package raj.and.dev.common;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;

import raj.and.dev.orientationsdk.SensorDetectClass;


public class MyService extends Service {

    private static final String TAG = "MyService";
    SensorDetectClass sensorDetectClass;

    private final IMyAidlInterface.Stub binder = new IMyAidlInterface.Stub() {
        @Override
        public List<String> screenOrientation() throws RemoteException {
            Log.i(TAG, "screenOrientation: AIDL method get called");
            sensorDetectClass = new SensorDetectClass(MyService.this);
            return sensorDetectClass.getSensorData();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: bind got called");
        return binder;
    }

}
