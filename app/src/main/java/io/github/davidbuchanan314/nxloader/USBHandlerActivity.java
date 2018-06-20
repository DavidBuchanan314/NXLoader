package io.github.davidbuchanan314.nxloader;

import android.app.Activity;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;

// Ideally, this would be a Service, but Services can't handle USB Intents :(
public class USBHandlerActivity extends Activity {

    private static final int APX_VID = 0x0955;
    private static final int APX_PID = 0x7321;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {

            UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            int vid = device.getVendorId();
            int pid = device.getProductId();
            USBDevHandler handler = null;

            Logger.log(this, "[*] USB device connected: " + device.getDeviceName());

            if (vid == APX_VID && pid == APX_PID) {
                handler = new PrimaryLoader();
            }

            // in future, Linux loaders etc. will be here
            // maybe I'll have some kind of table mapping vid/pid to a handler interface

            if (handler != null)
                handler.handleDevice(this, device);

            Logger.log(this, "[*] Done talking to device: " + device.getDeviceName());

            finish();
        }
    }

}
