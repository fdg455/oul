# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\AndroidSdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#-------------------------------------------定制化区域----------------------------------------------
#---------------------------------1.实体类---------------------------------

#理解完战略级思想后，我们开始第一部分补充-实体类。实体类由于涉及到与服务端的交互，各种gson的交互如此等等，
#是要保留的。将你项目中实体类都拎出来，用以下语法进行保留。
#-keep class 你的实体类所在的包.** { *; }

-keep class com.oushangfeng.ounews.bean.** { *; }

#-------------------------------------------------------------------------

#---------------------------------2.第三方包-------------------------------

#第2部分是第三方包。打开你的build.gradle文件，查看你用了哪些第三方的包
#用了glide，eventbus。我去他们的官网把已经写好的混淆copy下来

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

# LeakCanary
-keep class org.eclipse.mat.** { *; }
-keep class com.squareup.leakcanary.** { *; }

# rxjava
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    long producerNode;
    long consumerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

-dontwarn rx.internal.util.unsafe.**

# retrofit2

-dontwarn okio.**

-keep class com.parse.*{ *; }
-dontwarn com.parse.**
-dontwarn com.squareup.picasso.**

-keepattributes *Annotation*,EnclosingMethod

-keepnames class org.codehaus.jackson.** { *; }

-dontwarn javax.xml.**
-dontwarn javax.xml.stream.events.**
-dontwarn com.fasterxml.jackson.databind.**

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepattributes *Annotation*,EnclosingMethod,Signature
-keepnames class com.fasterxml.jackson.** { *; }
 -dontwarn com.fasterxml.jackson.databind.**
 -keep class org.codehaus.** { *; }
 -keepclassmembers public final enum org.codehaus.jackson.annotate.JsonAutoDetect$Visibility {
 public static final org.codehaus.jackson.annotate.JsonAutoDetect$Visibility *; }


# okhttp3
-dontwarn okhttp3.**
-keepattributes Signature
-keepattributes Annotation
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# greendao
-keep class de.greenrobot.dao.** { *; }
 #保持greenDao的方法不被混淆 # 用来保持生成的表名不被混淆
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
     public static java.lang.String TABLENAME;
 }
 -keep class **$Properties

 # richtext
 -dontwarn zhou.widget.**
 -keep class zhou.widget.** { *; }

# photoview
-dontwarn uk.co.senab.photoview.**
-keep class uk.co.senab.photoview.** { *; }

# Klog
-dontwarn com.socks.library.**
-keep class com.socks.library.** { *; }

# materialdialogs
-dontwarn com.afollestad.materialdialogs.**
-keep class com.afollestad.materialdialogs.** { *; }

# vitamio
-keep class io.vov.utils.** { *; }
-keep class io.vov.vitamio.** { *; }

# AndroidChangeSkin
-keep class com.zhy.changeskin.** { *; }
-keep interface com.zhy.changeskin.** { *; }

# PLDroidPlayer
-keep class com.pili.pldroid.player.** { *; }
-keep class tv.danmaku.ijk.media.player.** {*;}

# ijkplayer
-keep class tv.danmaku.ijk.media.player.** {*; }
-keepclasseswithmembernames class tv.danmaku.ijk.media.player.IjkMediaPlayer{
native ;
}
-keepclasseswithmembernames class tv.danmaku.ijk.media.player.ffmpeg.FFmpegApi{
native ;
}

#一般官网都是有混淆的，没有的话就google，也没有的话自己按照上面的写法自己写，
#还不会的话。。。。。只能换个包。。。。。如果你是直接包含的jar包的话，你这样写

#log4j
#-libraryjars log4j-1.2.17.jar
#-dontwarn org.apache.log4j.**
#-keep class  org.apache.log4j.** { *;}
#大致意思就是不混淆，不报warn。如果gradle报错的话，可以考虑注释掉-libraryjars log4j-1.2.17.jar这句。

#-------------------------------------------------------------------------

#---------------------------------3.与js互相调用的类------------------------

#第三部分与js互调的类，工程中没有直接跳过。一般你可以这样写
#-keep class 你的类所在的包.** { *; }

#如果是内部类的话，你可以这样
#-keepclasseswithmembers class 你的类所在的包.父类$子类 { <methods>; }

#例如
#-keepclasseswithmembers class com.demo.login.bean.ui.MainActivity$JSInterface {
#      <methods>;
#}

#-------------------------------------------------------------------------

#---------------------------------4.反射相关的类和方法-----------------------

#第四部分与反射有关的类，工程中没有直接跳过。类的话直接这样
#-keep class 你的类所在的包.** { *; }

#----------------------------------------------------------------------------
#---------------------------------------------------------------------------------------------------

#-------------------------------------------基本不用动区域--------------------------------------------
#---------------------------------基本指令区----------------------------------
#optimizationpasses 表示proguard对你的代码进行迭代优化的次数，首先要明白optimization 会对代码进行各种优化，每次优化后的代码还可以再次优化，
#所以就产生了优化次数的问题，这里面的 passes 应该翻译成 ‘次数’ 而不是 ‘通道’。楼上默认写 5 ，应该是做Android的，关于Android里面为什么写 5 ，
#因为作者本来写 99 ，但是每次迭代时间都很长团队成员天天抱怨，就改成 5 了，迭代会在最后一次无法优化的时候停止，也就是虽然你写着 99 ，但是可能就优化了 几次，一般情况下迭代10次左右的时候代码已经不能再次优化了
-optimizationpasses 5

# 混淆时不使用大小写混合，混淆后的类名为小写
# windows下的同学还是加入这个选项吧(windows大小写不敏感)
-dontusemixedcaseclassnames  # 是否使用大小写混合

# 指定不去忽略非公共的库的类
# 默认跳过，有些情况下编写的代码与类库中的类在同一个包下，并且持有包中内容的引用，此时就需要加入此条声明
-dontskipnonpubliclibraryclasses # 是否混淆第三方jar

# 指定不去忽略非公共的库的类的成员
-dontskipnonpubliclibraryclassmembers #不去忽略包可见的库类的成员

# 不做预检验，preverify是proguard的四个步骤之一
# Android不需要preverify，去掉这一步可以加快混淆速度
-dontpreverify # 混淆时是否做预校验

# 有了verbose这句话，混淆后就会生成映射文件
# 包含有类名->混淆后类名的映射关系
# 然后使用printmapping指定映射文件的名称
-verbose # 混淆时是否记录日志
-printmapping proguardMapping.txt #mapping.txt文件中找到proguard究竟给你混淆了什么,同时保留了什么

# 指定混淆时采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不改变
-optimizations !code/simplification/cast,!field/*,!class/merging/* # 混淆时所采用的算法

# 保护代码中的Annotation不被混淆
# 这在JSON实体映射时非常重要，比如fastJson
-keepattributes *Annotation*,InnerClasses #保护给定的可选属性,例如LineNumberTable, LocalVariableTable, SourceFile, Deprecated, Synthetic, Signature, and InnerClasses

# 避免混淆泛型
# 这在JSON实体映射时非常重要，比如fastJson
-keepattributes Signature #避免混淆泛型 如果混淆报错建议关掉

# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable
#----------------------------------------------------------------------------

#---------------------------------默认保留区---------------------------------
# 保留了继承自Activity、Application这些类的子类
# 因为这些子类有可能被外部调用
# 比如第一行就保证了所有Activity的子类不要被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}

# 如果有引用android-support-v4.jar包，可以添加下面这行
# -keep public class com.null.test.ui.fragment.** {*;}
#-keep class com.null.test.entities.** {
#    //全部忽略
#    *;
#}
#-keep class com.null.test.entities.** {
#    //忽略get和set方法
#    public void set*(***);
#    public *** get*();
#    public *** is*();
#}
#//以上两种任意一种都行
#一个项目中最好把所有的实体都放到同一个包下，这样针对包混淆就行了，避免了实体混淆遗漏而造成的崩溃

# 保留所有的本地native方法不被混淆。这点在我们做ndk开发的时候很重要
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保留Activity中的方法参数是view的方法，
# 从而我们在layout里面编写onClick就不会影响
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

#保持枚举 enum 类不被混淆 如果混淆报错，建议直接使用上面的 -keepclassmembers class * implements java.io.Serializable即可
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保留自定义控件(继承自View)不能被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
# 保留Parcelable序列化的类不能被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
# 保留Serializable 序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
   static final long serialVersionUID;
   private static final java.io.ObjectStreamField[] serialPersistentFields;
   !static !transient <fields>;
   private void writeObject(java.io.ObjectOutputStream);
   private void readObject(java.io.ObjectInputStream);
   java.lang.Object writeReplace();
   java.lang.Object readResolve();
}
#不混淆资源类,对R文件下的所有类及其方法，都不能被混淆
-keep class **.R$* {
 *;
}

# 对于带有回调函数onXXEvent的，不能混淆
-keepclassmembers class * {
    void *(**On*Event);
}
#----------------------------------------------------------------------------

#---------------------------------webview------------------------------------
# 如果项目中用到了WebView的复杂操作，请加入以下代码
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}
#----------------------------------------------------------------------------
#---------------------------------------------------------------------------------------------------

#将build.gradle中minifyEnabled设置为true打个包试试吧
#release {
#   minifyEnabled true
#   proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
#}
