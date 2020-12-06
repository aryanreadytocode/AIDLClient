package raj.and.dev.orientationsdk;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raj Aryan on 12/6/2020.
 * Mahiti Infotech
 * raj.aryan@mahiti.org
 */
public class SensorDetectClass {
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private static final int SENSOR_DELAY = 8 * 1000 * 1000; // 8sec
    private static final int FROM_RADS_TO_DEGS = -57;
    private static final String TAG = "SensorDetectClass";
    List<String> sensorData;


    public SensorDetectClass(Context context) {
        try {
            sensorManager = (SensorManager) context.getSystemService(Activity.SENSOR_SERVICE);
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        } catch (Exception e) {
            Log.e(TAG, "MyImpl: " + e.getMessage(), e);
        }

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor == sensor) {
                    if (event.values.length > 4) {
                        float[] truncatedRotationVector = new float[4];
                        System.arraycopy(event.values, 0, truncatedRotationVector, 0, 4);
                        setUpdatedData(truncatedRotationVector);
                    } else {
                        setUpdatedData(event.values);
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        sensorManager.registerListener(sensorEventListener, sensor, SENSOR_DELAY);
    }

    private void setUpdatedData(float[] vectors) {
        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, vectors);
        int worldAxisX = SensorManager.AXIS_X;
        int worldAxisZ = SensorManager.AXIS_Z;
        float[] adjustedRotationMatrix = new float[9];
        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisX, worldAxisZ, adjustedRotationMatrix);
        float[] orientation = new float[3];
        SensorManager.getOrientation(adjustedRotationMatrix, orientation);
        float pitch = orientation[1] * FROM_RADS_TO_DEGS;
        float roll = orientation[2] * FROM_RADS_TO_DEGS;
        sensorData = new ArrayList<>();
        sensorData.add(String.valueOf(pitch));
        sensorData.add(String.valueOf(roll));
    }


    public List<String> getSensorData(){
        return sensorData;
    }


}
