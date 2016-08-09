package com.xmx.androidframeworkbase.QRCode;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.ActivityBase.BaseTempActivity;
import com.xmx.androidframeworkbase.Tools.NewThread;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

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

        if (!content.equals("")) {
            Bitmap image = QRCodeEncoder.syncEncodeQRCode(content, 500);
            qrImage.setImageBitmap(image);
        } else {
            showToast("内容不能为空");
        }
    }

    @Event(value = R.id.btn_save_qr)
    private void onClickSaveQR(View view) {
        final String content = contentEdit.getText().toString();

        if (!content.equals("")) {
            Bitmap image = QRCodeEncoder.syncEncodeQRCode(content, 500);
            qrImage.setImageBitmap(image);

            if (image == null) {
                showToast("生成二维码失败");
                return;
            }
            Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
            int quality = 100;
            OutputStream stream;
            try {
                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "Camera");

                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        showToast("创建目录失败");
                        return;
                    }
                }

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                String filepath = mediaStorageDir.getPath() + File.separator +
                        "IMG_" + timeStamp + ".jpg";

                stream = new FileOutputStream(filepath);

                image.compress(format, quality, stream);

                showToast("图片保存至" + filepath);
            } catch (FileNotFoundException e) {
                filterException(e);
            }
        } else {
            showToast("内容不能为空");
        }
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
