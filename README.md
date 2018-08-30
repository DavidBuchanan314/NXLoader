![logo](https://i.imgur.com/o335KYo.png "logo")

# NXLoader
My first Android app: Launch Fusée Gelée payloads from stock Android

Heavily based on [Fusée Gelée](https://github.com/reswitched/fusee-launcher/) and [ShofEL2](https://github.com/fail0verflow/shofel2). [fusee.bin](https://github.com/ktemkin/Atmosphere/tree/poc_nvidia/fusee) is bundled as a default payload

## Note: Any proprietary payloads are neither tested nor supported by this software.

## Does it work on your device? [Report here](https://github.com/DavidBuchanan314/NXLoader/issues/1)
## [Get the APK release](https://github.com/DavidBuchanan314/NXLoader/releases)

This app is currently in "Alpha" state, it's my first Android app and there
is some rather disgusting code (Potentially blocking tasks on the UI thread 🤢). This will be improved soon™.

## HOWTO:
- Launch the app.
- (Optional) go to the Config tab, and select a custom payload file.
- Plug in your Switch. (On my Nexus 5, I use a micro USB OTG cable, and an A-to-C cable)
- Put it into RCM mode. (Note: your switch will power on by itself when plugged in, be sure to hold VOL+).
- Grant permission to the app to access the USB device.
- Enjoy!

Note: The app does not need to be running in order to launch the payload. The phone can even be locked!

## FAQ:
- Why use this over a web-based launcher?: No internet required, and can auto-launch even if your phone is locked. Plug and play!
- Can it load Linux?: soon™
- Will it brick my phone/switch?: Hopefully not, but I an certainly not responsible if it does!
- Does it need root?: Nope!

## TODO:
- Refactor the code so it's less hacky
- Improve UI/UX
- Implement loader for fail0verflow's Linux

For anyone who wants to look at the exploit source, the magic happens [here](https://github.com/DavidBuchanan314/NXLoader/blob/master/app/src/main/java/io/github/davidbuchanan314/nxloader/PrimaryLoader.java).
