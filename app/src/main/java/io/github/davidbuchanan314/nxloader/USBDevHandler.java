package io.github.davidbuchanan314.nxloader;

import android.content.Context;
import android.hardware.usb.UsbDevice;

public interface USBDevHandler {
    void handleDevice(Context ctx, UsbDevice device);
}
