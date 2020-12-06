package raj.and.dev.orientationsdk.common;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.List;

import raj.and.dev.orientationsdk.screenorientation.SensorDetectClass;


public class MyService extends Service {

    private static final String TAG = "MyService";
    MyImpl impl;
    SensorDetectClass sensorDetectClass;

    @Override
    public IBinder onBind(Intent intent) {
        impl = new MyImpl();
        return impl;
    }

    private class MyImpl extends IMyAidlInterface.Stub {

        public MyImpl() {
        }

        @Override
        public List<String> screenOrientation() throws RemoteException {
            sensorDetectClass = new SensorDetectClass(MyService.this);
            return sensorDetectClass.getSensorData();
        }
    }
}
