package poll.com.zjd.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import poll.com.zjd.R;
import poll.com.zjd.app.AppConfig;


/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/8  15:12
 * 包名:     poll.com.zjd.manager
 * 项目名:   zjd
 */

public class GlideManager {
    private static final int PLACEHOLDER_PIC = R.drawable.default_img;  //设置占位图片
    private static final int ERROR_PIC = R.drawable.default_img;        //设置错误图片

    public static void showImageDefaultSplash(Context context, String imageUrl, ImageView imageView, int placeholder, int error) {
        Glide.with(context)
                .load(AppConfig.getAppRequestUrl(imageUrl))
                .placeholder(placeholder == 0 ? PLACEHOLDER_PIC : placeholder) //设置占位图 R.mipmap.ic_launcher
                .error(error == 0 ? ERROR_PIC : error) //设置错误图片 R.mipmap.future_studio_launcher
                .crossFade()  //设置淡入淡出效果，默认300ms，可以传参
                .centerCrop()  //centerCrop:等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。fitCenter：等比例缩放图片，宽或者是高等于ImageView的宽或者是高。
                .dontAnimate() //不显示动画效果
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }

    public static void showImageDefault(Context context, String imageUrl, ImageView imageView, int placeholder, int error) {
        Glide.with(context)
                .load(AppConfig.getAppRequestUrl(imageUrl))
                .placeholder(placeholder == 0 ? PLACEHOLDER_PIC : placeholder) //设置占位图 R.mipmap.ic_launcher
                .error(error == 0 ? ERROR_PIC : error) //设置错误图片 R.mipmap.future_studio_launcher
                .crossFade(1000)  //设置淡入淡出效果，默认300ms，可以传参
                .thumbnail(0.1f)
                .centerCrop() //centerCrop:等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。fitCenter：等比例缩放图片，宽或者是高等于ImageView的宽或者是高。
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }
    //高
    public static void showImageDefaultA(Context context, String imageUrl, ImageView imageView, int placeholder, int error) {
        Glide.with(context)
                .load(AppConfig.getAppRequestUrl(imageUrl))
                .placeholder(placeholder == 0 ? PLACEHOLDER_PIC : placeholder) //设置占位图 R.mipmap.ic_launcher
                .error(error == 0 ? ERROR_PIC : error) //设置错误图片 R.mipmap.future_studio_launcher
//                .crossFade(1000)  //设置淡入淡出效果，默认300ms，可以传参
//                .fitCenter() //centerCrop:等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。fitCenter：等比例缩放图片，宽或者是高等于ImageView的宽或者是高。
                .thumbnail(0.1f)
                .centerCrop()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }

    //宽度固定
    public static void showImageDefaultB(Context context, String imageUrl, ImageView imageView, int placeholder, int error) {
        Glide.with(context)
                .load(AppConfig.getAppRequestUrl(imageUrl))
                .placeholder(placeholder == 0 ? PLACEHOLDER_PIC : placeholder) //设置占位图 R.mipmap.ic_launcher
                .error(error == 0 ? ERROR_PIC : error) //设置错误图片 R.mipmap.future_studio_launcher
                .thumbnail(0.1f)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }
    //方形圆角
    public static void showImage(Context context, String imageUrl, ImageView imageView, int placeholder, int error) {
        Glide.with(context)
                .load(AppConfig.getAppRequestUrl(imageUrl))
                .placeholder(placeholder == 0 ? PLACEHOLDER_PIC : placeholder) //设置占位图 R.mipmap.ic_launcher
                .error(error == 0 ? ERROR_PIC : error) //设置错误图片 R.mipmap.future_studio_launcher
                .crossFade(1000)  //设置淡入淡出效果，默认300ms，可以传参
                .bitmapTransform(new CropSquareTransformation(context), new RoundedCornersTransformation(context, 10, 10))
                //.dontAnimate() //不显示动画效果
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }
    //方形
    public static void showImageSqu(Context context, String imageUrl, ImageView imageView, int placeholder, int error) {
        Glide.with(context)
                .load(AppConfig.getAppRequestUrl(imageUrl))
                .placeholder(placeholder == 0 ? PLACEHOLDER_PIC : placeholder) //设置占位图 R.mipmap.ic_launcher
                .error(error == 0 ? ERROR_PIC : error) //设置错误图片 R.mipmap.future_studio_launcher
                .crossFade(1000)  //设置淡入淡出效果，默认300ms，可以传参
                .thumbnail(0.1f)
                .bitmapTransform(new CropSquareTransformation(context))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }
    //圆角
    public static void showImageToCircle(Context context,
                                         String url, ImageView imageView, int placeholder, int error) {
        Glide.with(context)
                .load(AppConfig.getAppRequestUrl(url))  // 加载图片
                .error(error == 0 ? ERROR_PIC : error) // 设置错误图片
                .placeholder(placeholder == 0 ? PLACEHOLDER_PIC : placeholder)// 设置占位图
                .thumbnail(0.1f)
                .crossFade(1000)// 设置淡入淡出效果，默认300ms，可以传参
                .bitmapTransform(new CropCircleTransformation(context))//圆角
                .diskCacheStrategy(DiskCacheStrategy.RESULT)// 缓存修改过的图片
                .into(imageView);
    }

    //设置一些详情上边头部稍微大一点的布局背景图片
    public static void showImage(Context context,
                                 String url, final View frameBg, int placeholder, int error) {
        Glide.with(context).load(AppConfig.getAppRequestUrl(url))
                .asBitmap()                                          //加载bitmap
                .error(error == 0 ? ERROR_PIC : error)        // 设置错误图片
                .placeholder(placeholder == 0 ? PLACEHOLDER_PIC : placeholder)  // 设置占位图
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)// 缓存修改过的图片
                .into(new SimpleTarget<Bitmap>() {

                    @SuppressLint("NewApi")
                    @Override
                    public void onResourceReady(Bitmap loadedImage,
                                                GlideAnimation<? super Bitmap> arg1) {
                        BitmapDrawable bd = new BitmapDrawable(loadedImage);

                        frameBg.setBackground(bd);

                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        // TODO Auto-generated method stub
                        super.onLoadFailed(e, errorDrawable);

                        frameBg.setBackgroundDrawable(errorDrawable);
                    }

                });

    }

    //高斯模糊
    public static void showImage(Context context,
                                 String url, final LinearLayout bgLayout, int placeholder, int error) {
        Glide.with(context).load(AppConfig.getAppRequestUrl(url))
                .asBitmap()
                .error(error == 0 ? ERROR_PIC : error)// 设置错误图片
                .placeholder(placeholder == 0 ? PLACEHOLDER_PIC : placeholder)   // 设置占位图
                .diskCacheStrategy(DiskCacheStrategy.RESULT)// 缓存修改过的图片
                .transform(new BlurTransformation(context))// 高斯模糊处理
                .into(new SimpleTarget<Bitmap>() {

                    @SuppressLint("NewApi")
                    @Override
                    public void onResourceReady(Bitmap loadedImage,
                                                GlideAnimation<? super Bitmap> arg1) {
                        BitmapDrawable bd = new BitmapDrawable(loadedImage);

                        bgLayout.setBackground(bd);

                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        // TODO Auto-generated method stub
                        super.onLoadFailed(e, errorDrawable);

                        bgLayout.setBackgroundDrawable(errorDrawable);
                    }

                });

    }
}
