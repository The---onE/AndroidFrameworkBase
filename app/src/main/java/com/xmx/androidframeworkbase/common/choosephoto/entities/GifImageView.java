package com.xmx.androidframeworkbase.common.choosephoto.entities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

public class GifImageView extends ImageView {

    protected static final int DEFAULT_MOVIE_DURATION = 1000;

    protected float customScale = 5f;
    protected String mPath;
    protected Movie mMovie;
    protected Bitmap mBitmap;
    protected long mMovieStart;
    protected int mCurrentAnimationTime = 0;
    protected long mOffset = 0;
    protected int mDuration;
    protected long mLatestTime;
    protected float mLeft;
    protected float mTop;
    protected float mScale;

    static final int PLAY = 0;
    static final int PAUSE = 1;
    static final int UPEND = -1;
    protected int mStatus = PLAY;

    public GifImageView(Context context) {
        this(context, null);
    }

    public GifImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GifImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "WeNote/Cache");
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 4 ;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(5)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheFileCount(100)
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize(cacheSize)
                .build();
        ImageLoader.getInstance().init(config);
    }

    public void setImageMovie(Movie movie) {
        if (movie != null) {
            mMovie = movie;
            mBitmap = null;

            mDuration = mMovie.duration();
            if (mDuration == 0) {
                mDuration = DEFAULT_MOVIE_DURATION;
            }
            mLatestTime = android.os.SystemClock.uptimeMillis();
            setupMovie();
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        if (bm != null) {
            mBitmap = bm;
            mMovie = null;
            setupBitmap();
        }
    }

    public void setImageByPath(String path) {
        mPath = path;
        Movie movie = Movie.decodeFile(path);

        if (movie != null) {
            setImageMovie(movie);
        } else {
            setImageBitmap(BitmapFactory.decodeFile(path));
        }
    }

    public boolean setImageByPathLoader(String path) {
        return setImageByPathLoader(path, GifLoader.Type.LIFO);
    }

    public boolean setImageByPathLoader(String path, GifLoader.Type type) {
        setPath(path);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);
        if (opts.outMimeType == null) {
            return false;
        }
        if (opts.outMimeType.equals("image/gif")) {
            GifLoader.getInstance(5, type).loadImage(path, this);
            return true;
        } else {
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoader.getInstance().displayImage("file://" + path, this, options);
            return false;
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mMovie = null;
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        mMovie = null;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public String getPath() {
        return mPath;
    }

    private int resolveActualSize(int desiredSize, int measureSpec) {
        int result;
        int maxSize = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = maxSize;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(desiredSize, maxSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                result = desiredSize;
                break;
            default:
                result = 1;
                break;
        }
        return result;
    }

    private int resolveAdjustedSize(int desiredSize, int maxSize, int measureSpec) {
        int result = desiredSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = Math.min(desiredSize, maxSize);
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(Math.min(desiredSize, specSize), maxSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mMovie != null) {
            int movieWidth = mMovie.width();
            int movieHeight = mMovie.height();
            int defaultWidth = MeasureSpec.getSize(widthMeasureSpec);
            int defaultHeight = MeasureSpec.getSize(heightMeasureSpec);

            boolean widthFlag = ((float) movieWidth / defaultWidth) > ((float) movieHeight / defaultHeight);

            int actualWidth = resolveActualSize((int) (movieWidth * customScale), widthMeasureSpec);
            int actualHeight = resolveActualSize((int) (movieHeight * customScale), heightMeasureSpec);

            if (widthFlag) {
                mScale = (float) actualWidth / (float) movieWidth;
            } else {
                mScale = (float) actualHeight / (float) movieHeight;
            }

            int w = (int) (movieWidth * mScale);
            int h = (int) (movieHeight * mScale);

            boolean resizeWidth = MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY;
            boolean resizeHeight = MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;
            float desiredAspect = (float) w / (float) h;

            int widthSize;
            int heightSize;

            if (resizeWidth || resizeHeight) {
                widthSize = resolveAdjustedSize(w, Integer.MAX_VALUE, widthMeasureSpec);
                heightSize = resolveAdjustedSize(h, Integer.MAX_VALUE, heightMeasureSpec);

                if (desiredAspect != 0.0f) {
                    float actualAspect = (float) (widthSize) / (heightSize);
                    if (Math.abs(actualAspect - desiredAspect) > 0.0000001) {
                        boolean done = false;
                        if (resizeWidth) {
                            int newWidth = (int) (desiredAspect * (heightSize));
                            if (!resizeHeight) {
                                widthSize = resolveAdjustedSize(newWidth, Integer.MAX_VALUE, widthMeasureSpec);
                            }
                            if (newWidth <= widthSize) {
                                widthSize = newWidth;
                                done = true;
                            }
                        }
                        if (!done && resizeHeight) {
                            int newHeight = (int) ((widthSize) / desiredAspect);
                            if (!resizeWidth) {
                                heightSize = resolveAdjustedSize(newHeight, Integer.MAX_VALUE, heightMeasureSpec);
                            }
                            if (newHeight <= heightSize) {
                                heightSize = newHeight;
                            }
                        }
                    }
                }
            } else {
                w = Math.max(w, getSuggestedMinimumWidth());
                h = Math.max(h, getSuggestedMinimumHeight());

                widthSize = resolveSizeAndState(w, widthMeasureSpec, 0);
                heightSize = resolveSizeAndState(h, heightMeasureSpec, 0);
            }

            mLeft = (widthSize - w) / 2f;
            mTop = (heightSize - h) / 2f;
            setMeasuredDimension(widthSize, heightSize);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mMovie != null) {
            updateAnimationTime();
            drawMovieFrame(canvas);
            invalidateView();
        } else {
            super.onDraw(canvas);
        }
    }

    @SuppressLint("NewApi")
    private void invalidateView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            postInvalidateOnAnimation();
        } else {
            invalidate();
        }
    }

    private void updateAnimationTime() {
        if (mMovie != null) {
            long now = android.os.SystemClock.uptimeMillis();
            // 如果第一帧，记录起始时间
            if (mMovieStart == 0) {
                mMovieStart = now;
            }

            switch (mStatus) {
                case PAUSE:
                    mOffset += now - mLatestTime;
                    break;

                case UPEND:
                    mOffset += (now - mLatestTime) * 2;
                    break;

                default:
                    break;
            }
            mLatestTime = now;
            long time = now - mMovieStart - mOffset;
            mCurrentAnimationTime = (int) (time % mDuration);
            if (time < 0) {
                mCurrentAnimationTime += mDuration;
            }
        }
    }

    private void drawMovieFrame(Canvas canvas) {
        if (mMovie != null) {
            // 设置要显示的帧，绘制即可
            mMovie.setTime(mCurrentAnimationTime);
            canvas.save(Canvas.MATRIX_SAVE_FLAG);
            canvas.scale(mScale, mScale);
            mMovie.draw(canvas, mLeft / mScale, mTop / mScale);
            canvas.restore();
        }
    }

    protected void setupMovie() {
        requestLayout();
        postInvalidate();
    }

    protected void setupBitmap() {
        requestLayout();
        postInvalidate();
    }

    public void play() {
        mStatus = PLAY;
    }

    public void pause() {
        mStatus = PAUSE;
    }

    public void upend() {
        mStatus = UPEND;
    }
}