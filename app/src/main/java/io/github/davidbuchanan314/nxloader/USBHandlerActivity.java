package io.github.davidbuchanan314.nxloader;

import android.app.Activity;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;

import java.lang.ref.WeakReference;

// Ideally, this would be a Service, but Services can't handle USB Intents :(
public class USBHandlerActivity extends Activity {
    private static final int APX_VID = 0x0955;
    private static final int APX_PID = 0x7321;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usb_handler);

        new USBHandlerTask(new WeakReference<Activity>(this)).execute();
    }

    private static class USBHandlerTask extends AsyncTask<Void, Void, Void>
    {
        private WeakReference<Activity> activityWeakReference;

        USBHandlerTask(WeakReference<Activity> activityWeakReference) {
            this.activityWeakReference = activityWeakReference;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Intent intent = this.activityWeakReference.get().getIntent();
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(intent.getAction())) {

                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                int vid = device.getVendorId();
                int pid = device.getProductId();
                USBDevHandler handler = null;

                Logger.log(this.activityWeakReference.get(), activityWeakReference.get().getString(R.string.log_usb_device_connected, device.getDeviceName()));

                if (vid == APX_VID && pid == APX_PID) {
                    handler = new PrimaryLoader();
                }

                // in future, Linux loaders etc. will be here
                // maybe I'll have some kind of table mapping vid/pid to a handler interface

                if (handler != null)
                    handler.handleDevice(this.activityWeakReference.get() ,device);

                Logger.log(this.activityWeakReference.get(), activityWeakReference.get().getString(R.string.log_done_talking, device.getDeviceName()));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            this.activityWeakReference.get().finish();
        }
    }

}
