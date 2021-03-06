package com.xmx.androidframeworkbase.module.qr;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.base.activity.BaseTempActivity;
import com.xmx.androidframeworkbase.utils.ExceptionUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

@ContentView(R.layout.activity_create_qrcode)
public class CreateQRCodeActivity extends BaseTempActivity {
    private static final int REQUEST_CODE_CHOOSE_LOGO_FROM_GALLERY = 666;
    private static final int BORDER_WIDTH = 50;


    @ViewInject(R.id.edit_qr)
    EditText contentEdit;

    @ViewInject(R.id.image_qr)
    ImageView qrImage;

    String logoPath;
    @ViewInject(R.id.logo_path)
    TextView logoPathView;

    @Event(value = R.id.btn_choose_logo)
    private void onClickChooseLogo(View view) {
        startActivityForResult(BGAPhotoPickerActivity.newIntent(this, null, 1, null),
                REQUEST_CODE_CHOOSE_LOGO_FROM_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_LOGO_FROM_GALLERY) {
            logoPath = BGAPhotoPickerActivity.getSelectedImages(data).get(0);
            logoPathView.setText(logoPath);
        }
    }

    @Event(value = R.id.btn_qr)
    private void onClickCreateQR(View view) {
        final String content = contentEdit.getText().toString();

        if (!content.equals("")) {
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
                        image = QRCodeEncoder.syncEncodeQRCode(content,
                                BGAQRCodeUtil.dp2px(CreateQRCodeActivity.this, 150), Color.BLACK, logoBitmap);
                    } else {
                        image = QRCodeEncoder.syncEncodeQRCode(content,
                                BGAQRCodeUtil.dp2px(CreateQRCodeActivity.this, 150));
                    }
                    return image;
                }

                @Override
                protected void onPostExecute(Bitmap result) {
                    qrImage.setImageBitmap(result);
                    setTitle("生成成功");
                }
            }.execute();
        } else {
            showToast("内容不能为空");
        }
    }

    @Event(value = R.id.btn_save_qr)
    private void onClickSaveQR(View view) {
        final String content = contentEdit.getText().toString();

        if (!content.equals("")) {
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
                        image = QRCodeEncoder.syncEncodeQRCode(content,
                                BGAQRCodeUtil.dp2px(CreateQRCodeActivity.this, 150), Color.BLACK, logoBitmap);
                    } else {
                        image = QRCodeEncoder.syncEncodeQRCode(content,
                                BGAQRCodeUtil.dp2px(CreateQRCodeActivity.this, 150));
                    }

                    return image;
                }

                @Override
                protected void onPostExecute(final Bitmap image) {
                    if (image == null) {
                        showToast("生成二维码失败");
                        setTitle("生成失败");
                        return;
                    }
                    qrImage.setImageBitmap(image);
                    setTitle("生成成功");

                    new AsyncTask<Void, Boolean, String>() {
                        @Override
                        protected void onPreExecute() {
                            setTitle("正在保存");
                        }

                        @Override
                        protected String doInBackground(Void... voids) {
                            int width = image.getWidth() + BORDER_WIDTH * 2;
                            int height = image.getHeight() + BORDER_WIDTH * 2;
                            Bitmap file = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(file);
                            canvas.drawColor(Color.WHITE);
                            canvas.drawBitmap(image, BORDER_WIDTH, BORDER_WIDTH, null);

                            Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
                            int quality = 100;
                            OutputStream stream;
                            try {
                                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES), "Camera");

                                if (!mediaStorageDir.exists()) {
                                    if (!mediaStorageDir.mkdirs()) {
                                        publishProgress(false);
                                        return "创建目录失败";
                                    }
                                }

                                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                                String filepath = mediaStorageDir.getPath() + File.separator +
                                        "IMG_" + timeStamp + ".jpg";

                                stream = new FileOutputStream(filepath);

                                file.compress(format, quality, stream);

                                publishProgress(true);
                                return "图片保存至" + filepath;
                            } catch (FileNotFoundException e) {
                                ExceptionUtil.normalException(e, getBaseContext());
                            }
                            return null;
                        }

                        @Override
                        protected void onProgressUpdate(Boolean... values) {
                            if (values[0]) {
                                setTitle("保存成功");
                            } else {
                                setTitle("保存失败");
                            }
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            showToast(s);
                        }
                    }.execute();
                }
            }.execute();
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
