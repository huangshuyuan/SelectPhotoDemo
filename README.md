# SelectPhotoDemo
图片选择，类似微信的图片选择器，实用
# 简介

1. 完美支持7.0，不存在**Android7.0 FileUriExposedException**。
2. 支持组件：`Activity`、`Fragment`。
3. UI可以配置，比如：`Toolbar`、`StatusBar`、`NavigationBar`。
4. 相册选图，单选、多选、文件夹预览。
5. 相机拍照，可以单独调用、也可以以Item展示在相册中。
6. 画廊，支持缩放、支持浏览本地图片、支持浏览网络图片。
7. 支持配置相册列数，支持配置相册是否使用相机。
8. 画廊预览选择的图片，预览时可以反选。
9. 支持自定义`LocalImageLoader`，例如使用：`Glide`、`Picasso`、`ImageLoader`实现。

***
## Demo效果预览

![效果图](http://upload-images.jianshu.io/upload_images/3805053-03e8534f8b872dc2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


# 参考文档：

 PhotoView：  https://github.com/chrisbanes/PhotoView
 
 LoadingDrawable：https://github.com/dinuscxj/LoadingDrawable
 
本项目开源地址：[https://github.com/yanzhenjie/album](https://github.com/yanzhenjie/album)

Demo地址：https://github.com/huangshuyuan/SelectPhotoDemo

我的简书：http://www.jianshu.com/p/f91dcf91feec

# 使用方法
### Gradle：
```
compile 'com.yanzhenjie:album:1.0.0'
```

###  Maven:
```
<dependency>
  <groupId>com.yanzhenjie</groupId>
  <artifactId>album</artifactId>
  <version>1.0.6</version>
  <type>pom</type>
</dependency>
```

Eclipse请下载源码自行转换成Library project。
### mainifest.xml中需要注册
```
<activity
    android:name="com.yanzhenjie.album.AlbumActivity"
    android:configChanges="orientation|keyboardHidden|screenSize"
    android:theme="@style/Theme.AppCompat.Light.NoActionBar"
    android:windowSoftInputMode="stateAlwaysHidden|stateHidden" />
```

其中android:label="xx"中的xx是调起的Activity的标题，你可以自定义，其它请照抄即可。
### 需要的权限
```
<uses-permission android:name="android.permission.CAMERA"/>
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
```

开发者不需要担心Android6.0的运行时权限，Album已经非常完善的处理过了。
# Album
主要功能分为三部分：相册选图、相机拍照、画廊预览，下面分别说明。
### Album 相册
使用Album.album(context).start()即可调起相册。
```
Album.album(context)
    .requestCode(999) // 请求码，返回时onActivityResult()的第一个参数。
    .toolBarColor(toolbarColor) // Toolbar 颜色，默认蓝色。
    .statusBarColor(statusBarColor) // StatusBar 颜色，默认蓝色。
    .navigationBarColor(navigationBarColor) // NavigationBar 颜色，默认黑色，建议使用默认。
    .title("图库") // 配置title。
    
    .selectCount(9) // 最多选择几张图片。
    .columnCount(2) // 相册展示列数，默认是2列。
    .camera(true) // 是否有拍照功能。
    .checkedList(mImageList) // 已经选择过得图片，相册会自动选中选过的图片，并计数。
    .start();
```


重写`onActivityResult()`方法，接受图片选择结果：  
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(requestCode == 999) {
        if (resultCode == RESULT_OK) { // Successfully.
            // 不要质疑你的眼睛，就是这么简单。
            ArrayList<String> pathList = Album.parseResult(data);
        } else if (resultCode == RESULT_CANCELED) { // User canceled.
            // 用户取消了操作。
        }
    }
}
```

## Camera 相机
使用`Album.camera(context).start()`即可调起相机，已经处理了权限和`Android7.0`的`FileProvider`问题。
```java
Album.camera(context)
    .requestCode(666)
	// .imagePath() // 指定相机拍照的路径，建议非特殊情况不要指定.
    .start();
```

重写`onActivityResult()`方法，接受图片选择结果： 
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(requestCode == 666) {
        if (resultCode == RESULT_OK) { // Successfully.
            // 这里的List的size肯定是1。
            List<String> pathList = Album.parseResult(data); // Parse path.
        } else if (resultCode == RESULT_CANCELED) {
            // 用户取消了操作。
        }
    }
}
```

## Gallery 画廊
使用`Album.gallery(context).start()`即可调起画廊。画廊默认支持预览本地图片，如果要预览网络图片，你需要在初始化的时候配置`ImageLoader`，具体见下文。  

调用的时候你只需要传入一个路径集合：  
```java
Album.gallery(context)
    .requestCode(555) // 请求码，返回时onActivityResult()的第一个参数。
    .toolBarColor(toolbarColor) // Toolbar 颜色，默认蓝色。
    .statusBarColor(statusBarColor) // StatusBar 颜色，默认蓝色。
    .navigationBarColor(navigationBarColor) // NavigationBar 颜色，默认黑色，建议使用默认。
    
    .checkedList(mImageList) // 要预览的图片list。
    .currentPosition(position) // 预览的时候要显示list中的图片的index。
    .checkFunction(true) // 预览时是否有反选功能。
    .start();
```

**注意：**

* 一定要传入要预览的图片集合，否则启动会立即返回。
* 调用画廊预览时判断`if(currentPosition < mImageList.size())`，这样才能保证传入的`position`在`list`中，否则会立即返回。

如果你需要预览时的反选功能，那么重写`onActivityResult()`方法，接受反选后的图片`List`结果：  
```java
ArrayList<String> mImageList;

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(requestCode == 555) {
        if (resultCode == RESULT_OK) { // Successfully.
            // 不要再次质疑你的眼睛，还是这么简单。
            mImageList = Album.parseResult(data);
        } else if (resultCode == RESULT_CANCELED) { // User canceled.
            // 用户取消了操作。
        }
    }
}
```

## 高级配置
**这个配置不是必须的，不配置也完全可以用：**。

1. `ImageLoader`，默认使用`LocalImageLoader`，你可以用`Glide`和`Picasso`等其它第三方框架来实现。
2. `Locale`，默认已经支持国际化了，支持简体中文、繁体中文、英语。如果你要指定语言，可以使用`Locale`配置。

### ImageLoader配置
我推荐优先使用默认`ImageLoader`，其次用`Glide`实现、其次是`Picasso`，最后是`ImageLoader`，暂时不支持`Fresco`。

Album提供的默认的`LocalImageLoader`如下：
```java
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Album.initialize(
            new AlbumConfig.Build()
                .setImageLoader(new LocalImageLoader()) // 使用默认loader.
                .build()
        );
    }
}
```

**用Glide实现：**
```java
public class GlideImageLoader implements AlbumImageLoader {
    @Override
    public void loadImage(ImageView imageView, String imagePath, int width, int height) {
        Glide.with(imageView.getContext())
            .load(new File(imagePath))
            .into(imageView);
    }
}

...

Album.initialize(new AlbumConfig.Build()
    .setImageLoader(new GlideImageLoader()) // Use glide loader.
    .build()
```

**用Picasso实现**
```java
public class PicassoImageLoader implements AlbumImageLoader {

    @Override
    public void loadImage(ImageView imageView, String imagePath, int width, int height) {
        Picasso.with(imageView.getContext())
            .load(new File(imagePath))
            .centerCrop()
            .resize(width, height)
            .into(imageView);
    }
}

...

Album.initialize(new AlbumConfig.Build()
    .setImageLoader(new PicassoImageLoader()) // Use picasso loader.
    .build()
```

# 混淆
`Album`是完全可以混淆的，如果混淆后相册出现了问题，请在混淆规则中添加：  
```txt
-dontwarn com.yanzhenjie.album.**
-keep class com.yanzhenjie.album.**{*;}
```



***

