AndroidHelperLib
=======
[![Bintray](https://img.shields.io/bintray/v/itachi1706/ccn-android-lib/helper-lib-android)](https://bintray.com/itachi1706/ccn-android-lib/helper-lib-android/_latestVersion)
[![GitHub Actions](https://github.com/itachi1706/AndroidHelperLib/workflows/Android%20CI/badge.svg)](https://github.com/itachi1706/AndroidHelperLib/actions)
[![GitHub release](https://img.shields.io/github/release/itachi1706/AndroidHelperLib.svg)](https://github.com/itachi1706/AndroidHelperLib/releases) 
[![GitHub license](https://img.shields.io/github/license/itachi1706/AndroidHelperLib.svg)](https://github.com/itachi1706/AndroidHelperLib/blob/master/LICENSE) 
[![Code Climate](https://codeclimate.com/github/itachi1706/AndroidHelperLib/badges/gpa.svg)](https://codeclimate.com/github/itachi1706/AndroidHelperLib) 
[![Test Coverage](https://codeclimate.com/github/itachi1706/AndroidHelperLib/badges/coverage.svg)](https://codeclimate.com/github/itachi1706/AndroidHelperLib/coverage) 
[![Issue Count](https://codeclimate.com/github/itachi1706/AndroidHelperLib/badges/issue_count.svg)](https://codeclimate.com/github/itachi1706/AndroidHelperLib)

**NOTE: We are in the midst of allowing an easier way to access through JCenter, use the Bintray method in the meantime**

This library contains various helper classes for use in Android-based projects

## Usage - Bintray
To use this library in an Android Project, add the following lines into your app-level build.gradle file

```gradle
repositories {
	maven {
		url  "https://dl.bintray.com/itachi1706/ccn-android-lib"
	}
}
…
dependencies {
  implementation 'com.itachi1706.helpers:helperlib:<latest-version>' // See bintray badge for latest version
}
```

## Usage - JCenter
To use this library in an Android Project, add the following lines into your app-level build.gradle file

```gradle
dependencies {
  implementation 'com.itachi1706.helpers:helperlib:<latest-version>' // See Bintray badge for latest version number
}
```

## Helper Classes in the library

### Deprecation Classes
These classes act basically as a compatability layer for versions of Android, using the newer APIs when possible and falling back to deprecated APIs when the new APIs are not available  
* [HTML](https://github.com/itachi1706/AndroidHelperLib/blob/master/app/src/main/java/com/itachi1706/helperlib/deprecation/Html.kt) - Handles HTML text formatting for TextViews
* [StatFs](https://github.com/itachi1706/AndroidHelperLib/blob/master/app/src/main/java/com/itachi1706/helperlib/deprecation/StatFs.kt) - Compatibility methods for handling file sizes
* [TextView](https://github.com/itachi1706/AndroidHelperLib/blob/master/app/src/main/java/com/itachi1706/helperlib/deprecation/TextView.kt) - Handles the method of setting text appearance of a TextView

### Helper classes
These classes provides some helper methods for various tasks that you may use when developing an Android Project
* [Connectivity](https://github.com/itachi1706/AndroidHelperLib/blob/master/app/src/main/java/com/itachi1706/helperlib/helpers/ConnectivityHelper.kt) - Handles check for internet/WiFi/Cellular connections and whether Data Saver is enabled on the device
* [Preference](https://github.com/itachi1706/AndroidHelperLib/blob/master/app/src/main/java/com/itachi1706/helperlib/helpers/PrefHelper.kt) - Various helper methods to handle day/night themes and dark mode switching, as well as the ability to access SharedPreference on the MainThread without triggering StrictMode
* [URL](https://github.com/itachi1706/AndroidHelperLib/blob/master/app/src/main/java/com/itachi1706/helperlib/helpers/URLHelper.kt) - Helper methods for connecting to a HTTP/HTTPS url (with fallback support) and retrieving a String of its data ___(More return types coming soon)___
* [App Validation](https://github.com/itachi1706/AndroidHelperLib/blob/master/app/src/main/java/com/itachi1706/helperlib/helpers/ValidationHelper.kt) - Validates where the application has been installed from. Useful to help check if the app is installed from places like Google Play Store to disable features that may be against the ToS of the respective stores. Also allows you to obtain the application signature for further verification

### Utility classes
These classes provides utility methods for aiding in specific tasks such as conversion of file types or retrieval from resource files
* [Bitmap](https://github.com/itachi1706/AndroidHelperLib/blob/master/app/src/main/java/com/itachi1706/helperlib/utils/BitmapUtil.kt) - Retrieve bitmap from various places (drawable, vectors etc)
* [Color](https://github.com/itachi1706/AndroidHelperLib/blob/master/app/src/main/java/com/itachi1706/helperlib/utils/ColorUtil.kt) - Obtain color from attributes
* [User Notification](https://github.com/itachi1706/AndroidHelperLib/blob/master/app/src/main/java/com/itachi1706/helperlib/utils/NotifyUserUtil.kt) - Quick  Snackbar and Toast messages notifications

### Fingerprint
Handles fingerprint authentication with the Fingerprint API  
*Note: This has been deprecated and is soon slated for removal. It is only present to provide time for applications to migrate. Users are recommended to use the [AndroidX Biometric API](https://developer.android.com/jetpack/androidx/releases/biometric) instead*
