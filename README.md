# NXLoader
My first Android app: Launch Fusée Gelée payloads from stock Android

Heavily based on [Fusée Gelée](https://github.com/reswitched/fusee-launcher/) and [ShofEL2](https://github.com/fail0verflow/shofel2).

## Does it work on your device? [Report here](https://github.com/DavidBuchanan314/NXLoader/issues/1)

This app is currently in "Alpha" state, it's my first Android app and there
is some rather disgusting code (Potentially blocking tasks on the UI thread 🤢). This will be improved soon™.

## HOWTO:
- Launch the app
- (Optional) go to the Config tab, and select a custom payload file

## FAQ:
- Can it load Linux?: soon™
- Will it brick my phone/switch?: Hopefully not, but I an certainly not responsible if it does!
- 

## TODO:
- Refactor the code so it's less hacky
- Improve UI/UX
- Implement loader for fail0verflow's Linux

For anyone who wants to look at the exploit source, the magic happens [here](https://github.com/DavidBuchanan314/NXLoader/blob/master/app/src/main/java/io/github/davidbuchanan314/nxloader/PrimaryLoader.java).
