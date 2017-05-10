package com.hsy.ptoto.selectphoto;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.util.DisplayUtils;
import com.yanzhenjie.album.widget.recyclerview.AlbumVerticalGirdDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.photo)
    Button photo;
    @BindView(R.id.takephoto)
    Button takephoto;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private ArrayList<String> mImageList;//已经选择过的照片列表
    MyRecylerViewAdapter adpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initRecyle();
    }

    //初始化列表
    private void initRecyle() {
        DisplayUtils.initScreen(this);//初始化屏幕测量
        RecyclerView.LayoutManager manager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(manager);
        //边距
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.decoration_white, null);
        recyclerView.addItemDecoration(new AlbumVerticalGirdDecoration(drawable));

        assert drawable != null;
        int itemSize = (DisplayUtils.screenWidth - (drawable.getIntrinsicWidth() * 4)) / 3;//计算显示高度
        adpter = new MyRecylerViewAdapter(this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getCurrentImage(position);
            }
        }, itemSize);
        recyclerView.setAdapter(adpter);
        mImageList = new ArrayList<>();
    }

    /*点击显示当前图片*/
    //画廊
    private void getCurrentImage(int position) {
        Album.gallery(this)
                .requestCode(555) // 请求码，返回时onActivityResult()的第一个参数。
                .toolBarColor(ContextCompat.getColor(this, R.color.colorPrimary))// Toolbar 颜色，默认蓝色。
                .statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark)) // StatusBar 颜色，默认蓝色。
                .navigationBarColor(ActivityCompat.getColor(this, R.color.colorPrimaryBlack)) // NavigationBar 颜色，默认黑色，建议使用默认。
                .checkedList(mImageList) // 要预览的图片list。
                .currentPosition(position) // 预览的时候要显示list中的图片的index。
                .checkFunction(true) // 预览时是否有反选功能。
                .start();
    }


    @OnClick({R.id.photo, R.id.takephoto})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.photo:
                //打开相册
                Album.album(this)
                        .requestCode(999) // 请求码，返回时onActivityResult()的第一个参数。
                        .toolBarColor(ActivityCompat.getColor(this, R.color.colorPrimaryBlack)) // Toolbar 颜色，默认蓝色。
                        .statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark)) // StatusBar 颜色，默认蓝色。
                        .navigationBarColor(ActivityCompat.getColor(this, R.color.colorPrimaryBlack)) // NavigationBar 颜色，默认黑色，建议使用默认。
                        .title("图库") // 配置title。
                        .selectCount(9) // 最多选择几张图片。
                        .columnCount(3) // 相册展示列数，默认是2列。
                        .camera(true) // 是否有拍照功能。
                        .checkedList(mImageList) // 已经选择过得图片，相册会自动选中选过的图片，并计数。
                        .start();
                break;
            case R.id.takephoto:
                //拍照

                Album.camera(this)
                        .requestCode(666)
                        // .imagePath() // 指定相机拍照的路径，建议非特殊情况不要指定.
                        .start();
                break;
        }
    }

    //    重写onActivityResult()方法，接受图片选择结果：
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 999:
                //图库
                if (resultCode == RESULT_OK) { // Successfully.
                    // 不要质疑你的眼睛，就是这么简单。
                    mImageList = Album.parseResult(data); // Parse path.
                    refreshView();

                    Log.i("获取到图片链接", mImageList.size() + "," + mImageList.get(0));
                } else if (resultCode == RESULT_CANCELED) { // User canceled.
                    // 用户取消了操作。
                    Toast.makeText(this, "用户取消选择", Toast.LENGTH_SHORT).show();
                }
                break;
            case 666:
                //相机
                if (resultCode == RESULT_OK) { // Successfully.
                    // 这里的List的size肯定是1。
                    List<String> imageList = Album.parseResult(data); // Parse path.
                    mImageList.addAll(imageList);
                    refreshView();
                } else if (resultCode == RESULT_CANCELED) {
                    // 用户取消了操作。
                    Toast.makeText(this, "用户取消选择", Toast.LENGTH_SHORT).show();
                }
                break;
            case 555: {
                if (resultCode == RESULT_OK) { // Successfully.
                    mImageList = Album.parseResult(data); // Parse select result.
                    refreshView();
                }
            }
            break;
        }

    }

    private void refreshView() {
        adpter.notifyDataSetChanged(mImageList);//刷新数据
    }
}
