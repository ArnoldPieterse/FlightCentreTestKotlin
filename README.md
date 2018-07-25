# FlightCentreTestKotlin
Android app that shows flights in a List. When you select a flight it takes you to the flight description view. In the flight description view you can show basic info.

Technologies uses:
- Kotlin
- RecyclerView
- RXJava2
- Retrofit2

This app was built to support 100% of devices (Estimation by Android) targeting api 15

MainActivity Screenshot:
https://github.com/ArnoldPieterse/FlightCentreTestKotlin/commit/59cbf33586a3fc4826420b2d2c159c627905e485#diff-31387f13e6cecd0b83f3c3d666cf4504

FlightActivity (Detail view) Screenshot:
https://github.com/ArnoldPieterse/FlightCentreTestKotlin/commit/59cbf33586a3fc4826420b2d2c159c627905e485#diff-bca7a00441b45163100868d43ade21ff

Things that can be improved:
- Convert GMT times to local
- Jetpack's Room could be used to save flights locally
- Before app release, Firebase bug tracker integration would be recommended
- Layout design and potentially adding rounded corners

