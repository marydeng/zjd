package poll.com.zjd.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import poll.com.zjd.R;
import poll.com.zjd.app.AppContext;
import poll.com.zjd.wxapi.WBShareActivity;
import poll.com.zjd.wxapi.WXshareUtils;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/22  21:14
 * 包名:     poll.com.zjd.utils
 * 项目名:   zjd
 */

public class ShareUtils {
    private static Dialog bottomDialog;
    public static void shareDialog(final Context context, final Resources res, final String title, final String description, final String shareUrl, final Object imgUrl) {
        final Activity activity =(Activity) context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
                return;
            }
        }
        LogUtils.e("---------url----"+shareUrl);
        if (bottomDialog==null) bottomDialog = new Dialog(context, R.style.BottomDialog);
        if (bottomDialog.isShowing())return;

        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_bottom_share, null);
        bottomDialog.setContentView(contentView);

        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = res.getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        try{
            bottomDialog.show();
        }catch (Exception e){
            LogUtils.e("分享弹窗异常");
            return;
        }


        contentView.findViewById(R.id.dialog_share_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissShareDialog();
            }
        });

        contentView.findViewById(R.id.share_weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //微信
                WXshareUtils.share(WXshareUtils.SHARE_TYPE.Type_WXSceneSession,res,title,description,shareUrl,imgUrl);
            }
        });

        contentView.findViewById(R.id.share_circle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //朋友圈
                WXshareUtils.share(WXshareUtils.SHARE_TYPE.Type_WXSceneTimeline,res,title,description,shareUrl,imgUrl);
            }
        });

        contentView.findViewById(R.id.share_weibo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //微博
                Bundle bundle = new Bundle();
                bundle.putString(WBShareActivity.SHARE_TITLE,title);
                bundle.putString(WBShareActivity.SHARE_DESCRIPTION,description);
                bundle.putString(WBShareActivity.SHARE_URL,shareUrl);
                if (imgUrl instanceof String){
                    bundle.putString(WBShareActivity.SHARE_IMGURL,(String) imgUrl);
                }
                AppContext.getInstance().startActivity(context, WBShareActivity.class,bundle);
            }
        });


    }

    //关闭弹窗
    public static void dismissShareDialog(){
        if (bottomDialog!=null){
            bottomDialog.dismiss();
            bottomDialog = null;
        }
    }
}
