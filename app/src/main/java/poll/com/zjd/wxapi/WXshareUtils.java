package poll.com.zjd.wxapi;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import poll.com.zjd.R;
import poll.com.zjd.api.Urls;
import poll.com.zjd.app.AppContext;
import poll.com.zjd.utils.LogUtils;

import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession;
import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneTimeline;



/**
 * 创建者:   张文辉
 * 创建时间: 2017/8/19  16:58
 * 包名:     poll.com.zjd.wxapi
 * 项目名:   zjd
 */

public class WXshareUtils {

    public static final String addFriendTitle = "%s邀请您成为臻酒到会员，尽享臻实惠";     //邀请好友的标题
    public static final String addFriendDescription = "品全球美酒，用臻酒到APP，就GO了"; //邀请好友描述
    public static final String addFriendUrl = Urls.H5_LOGIN;                          //点开的地址H5的注册页面

    public enum SHARE_TYPE {
        Type_WXSceneSession,
        Type_WXSceneTimeline
    }

    /**
     *
     * @param type         分享到哪里  session 微信朋友  Timeline 朋友圈
     * @param res          资源
     * @param title        分享标题
     * @param description  分享描述
     * @param shareUrl     分享出去用户点击跳转的url
     * @param image        分享的图片 支持int本地图片,以及网络图片(bitmap)
     */
    public static void share(final SHARE_TYPE type, final Resources res, final String title, final String description, final String shareUrl, final Object image) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WXWebpageObject webpageObject = new WXWebpageObject();
                webpageObject.webpageUrl = shareUrl;
                WXMediaMessage msg = new WXMediaMessage(webpageObject);
                msg.title = title;
                msg.description = description;
                Bitmap thumb;

                if (image instanceof Integer){
                    thumb = BitmapFactory.decodeResource(res, (Integer) image);

                } else if(image instanceof Bitmap){
                    thumb = (Bitmap) image;

                }else if(image instanceof String){
                    try {
                        thumb = BitmapFactory.decodeStream(new URL((String) image).openStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                        thumb = BitmapFactory.decodeResource(res, R.drawable.logo);
                    }
                }else {
                    thumb = BitmapFactory.decodeResource(res, R.drawable.logo);
                }

                msg.thumbData = bmpToByteArray(thumb, true);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("Req");
                req.message = msg;
                switch (type) {
                    case Type_WXSceneSession:
                        req.scene = WXSceneSession;
                        break;
                    case Type_WXSceneTimeline:
                        req.scene = WXSceneTimeline;
                        break;
                }
                AppContext.mWxApi.sendReq(req);
            }
        }).start();

    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        int i;
        int j;
        if (bmp.getHeight() > bmp.getWidth()) {
            i = bmp.getWidth();
            j = bmp.getWidth();
        }  else {
            i = bmp.getHeight();
            j = bmp.getHeight();
        }

        Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);
        Canvas localCanvas =  new Canvas(localBitmap);

        while ( true) {
            localCanvas.drawBitmap(bmp,  new Rect(0, 0, i, j),  new Rect(0, 0,i, j),  null);
            if (needRecycle)bmp.recycle();
            ByteArrayOutputStream localByteArrayOutputStream =  new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 50,localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                LogUtils.e("长度:"+arrayOfByte.length);
                return arrayOfByte;
            }  catch (Exception e) {
                // F.out(e);
            }
            i = bmp.getHeight();
            j = bmp.getHeight();
        }
    }
}
