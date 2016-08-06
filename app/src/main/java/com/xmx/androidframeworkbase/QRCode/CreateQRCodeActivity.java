package com.xmx.androidframeworkbase.QRCode;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.ActivityBase.BaseTempActivity;
import com.xmx.androidframeworkbase.Tools.NewThread;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

@ContentView(R.layout.activity_create_qrcode)
public class CreateQRCodeActivity extends BaseTempActivity {

    @ViewInject(R.id.edit_qr)
    EditText contentEdit;

    @ViewInject(R.id.image_qr)
    ImageView qrImage;

    @Event(value = R.id.btn_qr)
    private void onClickCreateQR(View view) {
        final String content = contentEdit.getText().toString();

        Bitmap image = QRCodeEncoder.syncEncodeQRCode(content, 500);
        qrImage.setImageBitmap(image);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
