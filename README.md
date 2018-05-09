# Pocket Trainer

Personal trainer app with the ability to install and playback arbitrary training programs stored in JSON format.

## Disclaimer

This is a proof of concept / prototype and therefore it lacks unit tests, contains a lot of duplication and generally does not follow any good programming principles. Other than that there are some binary assets such as images and sounds that have been used or modified without consent of their respective authors.

## TODO

### Must have

* Prepare test data with muscles, genders, superset (no gaps between actions), different kinds of actions, etc.
* Install from remote location (HTTP hook already in place and tested, albeit synchronous)

### Bugs and glitches

* "Go" not shown / zero not spoken on real device (timed recovery action).
* Changing screen orientation breaks the countdown.
* When finished and clicking "Start over", the upcoming exercise is displayed. However, when clicking back it should go to today rather than "Start over" again.
* Menu icon in the program browser should be white instead of black (three dots in the upper right corner).
* Back button sometimes allows to navigate to forbidden views. 

### Nice to have

* When querying training program repository, send already installed programs to avoid downloading them unnecessarily (requires to bump format version on the client; the server should be backwards-compatible).
* Show enrolled first or display selected program more prominently.
* Program browser search and filter.
* Integrate running, maps and location (distance vs. time).
* Configurable number of seconds (e.g. announcement every 30 seconds, countdown when <= 10 seconds, etc.)
* Default music / custom music / vibrate / turn off.
* Variable (random?) music samples.
* Spotify integration.
* Multiple languages.
* Configurable TTS language and locale.
* Pre-generate TTS for better performance (earcon?).
* Audio mixer (volume down when speaking).
* Cast to ChromeCast.
* Responsive design (different screen orientations and sizes).
* Improve performance.
