package com.xmx.androidframeworkbase.module.cart;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.base.activity.BaseTempActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

@ContentView(R.layout.activity_order_code)
public class OrderCodeActivity extends BaseTempActivity {

    @ViewInject(R.id.image_cart_code)
    ImageView cartCodeImg;

    @Override
    protected void initView(Bundle savedInstanceState) {
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        final String order = getIntent().getStringExtra("order");
        final String logoPath = null;
        if (order != null && !order.equals("")) {
            new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected void onPreExecute() {
                    setTitle("正在生成");
                }

                @Override
                protected Bitmap doInBackground(Void... params) {
                    Bitmap image;
                    if (logoPath != null) {
                        Bitmap logoBitmap = BitmapFactory.decodeFile(logoPath);
                        image = QRCodeEncoder.syncEncodeQRCode(order,
                                BGAQRCodeUtil.dp2px(OrderCodeActivity.this, 150), Color.BLACK, logoBitmap);
                    } else {
                        image = QRCodeEncoder.syncEncodeQRCode(order,
                                BGAQRCodeUtil.dp2px(OrderCodeActivity.this, 150));
                    }
                    return image;
                }

                @Override
                protected void onPostExecute(Bitmap result) {
                    cartCodeImg.setImageBitmap(result);
                    setTitle("生成成功");
                }
            }.execute();
        }
    }
}
