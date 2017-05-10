package com.hsy.ptoto.selectphoto;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yanzhenjie.album.impl.AlbumImageLoader;
import com.yanzhenjie.album.task.LocalImageLoader;

import java.io.File;

/**
 * Created by huangshuyuan on 2017/5/10.
 */

public class Application extends android.app.Application {
    private static Application instance;
    @Override
    public void onCreate() {
        super.onCreate();
        //配置图片加载,自定义
        Album.initialize(
                new AlbumConfig.Build()
                        .setImageLoader(new GlideImageLoader()) // 使用默认loader.----------new LocalImageLoader()
                        .build()
        );

    }
    public static Application getInstance() {
        return instance;
    }
//    用Glide实现：

    public class GlideImageLoader implements AlbumImageLoader {
        @Override
        public void loadImage(ImageView imageView, String imagePath, int width, int height) {
            Glide.with(imageView.getContext())
                    .load(new File(imagePath))
                    .into(imageView);
        }
    }
}
