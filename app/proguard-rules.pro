# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Chan\Android Studio SDK\Android Studio SDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:


#Note: the configuration explicitly specifies 'android.webkit.**' to keep library class 'android.webkit.ClientCertRequest'
# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interfaces
# class:
#-keepclassmembers class fqcn.of.javascript.interfaces.for.webview {
#   public *;
#}
#fresco shink
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
#fresco shink end

-ignorewarnings
-optimizationpasses 5

-keepclassmembers public class * extends android.view.View{
    public <init>(android.content.Content);
    public <init>(android.content.Content ,android.util.AttributeSet );
    public <init>(android.content.Content,android.util.AttributeSet ,int );
}
-keepclassmembers public class * extends android.view.ViewGroup{
    public <init>(android.content.Content);
    public <init>(android.content.Content,android.util.AttributeSet);
    public <init>(android.content.Content,android.util.AttributeSet,int );
}
#event bus proGuard
-keepclassmembers class ** {
    public void onEvent*(**);
}

-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.szbb.pro.entity.**{*;}
-keep class com.szbb.pro.eum.**{*;}
-keep class me.drakeet.** {*;}
##databinding proGuard
-dontwarn android.databinding.**
-keep class android.databinding.** { *; }


-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }

#baidu proGuard
-keep class com.baidu.** { *; }
-keep class vi.com.gdi.bgl.android.**{*;}
#jpush proGuard
-dontoptimize
-dontpreverify
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-dontwarn com.google.**
-keep class com.google.protobuf.** {*;}
#sharesdk proGuard
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-dontwarn cn.sharesdk.**
-dontwarn **.R$*

#Picasso proGuard
-dontwarn com.squareup.okhttp.**

#galleryFinal proGuard
-keep class cn.finalteam.galleryfinal.widget.*{*;}
-keep class cn.finalteam.galleryfinal.widget.crop.*{*;}
-keep class cn.finalteam.galleryfinal.widget.zoonview.*{*;}