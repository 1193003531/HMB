# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-ignorewarnings

-dontwarn android.support.**
-keep class android.support.** { *; }


-keepattributes InnerClasses
-dontoptimize


-keep class com.youth.xframe.takephoto.** { *; }
-dontwarn com.youth.xframe.takephoto.**

-keep class com.darsh.multipleimageselect.** { *; }
-dontwarn com.darsh.multipleimageselect.**

-keep class com.soundcloud.android.crop.** { *; }
-dontwarn com.soundcloud.android.crop.**

-keep class com.alibaba.sdk.android.oss.** { *; }
-dontwarn okio.**
-dontwarn org.apache.commons.codec.binary.**

-keep public class **.R$* { public static final int *; }
-keep public class * extends android.app.Activity
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.view.View
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep class * implements android.os.Parcelable {public static final android.os.Parcelable$Creator *; }

# 继承实现此接口的不予混淆
-keep public interface com.yuzhi.fine.common.NotObfuscateInterface{public *;}
-keep class * implements com.yuzhi.fine.common.NotObfuscateInterface{
	<methods>;
	<fields>;
}

-keepclassmembers class * extends com.tencent.smtt.sdk.WebViewClient{
    public void openFileChooser(...);
}