package com.oushangfeng.ounews.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.GifTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.oushangfeng.ounews.BuildConfig;
import com.oushangfeng.ounews.R;

import java.io.File;

/**
 * Created by Oubowu on 2016/10/15 22:17.
 */
public class GlideUtils {

    /**
     * 加载图片，默认设置了加载图片，出错图片，不转换，没有加载动画
     *
     * @param loadObj           加载对象
     * @param imageView         图片
     * @param overrideW         重设宽度
     * @param overrideH         重设高度
     * @param asGif             是否指定图片为Gif类型，null不指定，true指定为Gif，false指定为Bitmap
     * @param format            图片质量，只有设置图片为bitmap类型时才有效
     * @param diskcacheStrategy 硬盘缓存策略
     */
    public static void loadDefaultOverrideNoAnim(Object loadObj, ImageView imageView, int overrideW, int overrideH, Boolean asGif, DecodeFormat format, DiskCacheStrategy diskcacheStrategy) {
        load(imageView.getContext(), loadObj, imageView, overrideW, overrideH, R.drawable.ic_loading, R.drawable.ic_fail, 0, asGif, format, null, diskcacheStrategy);
    }

    /**
     * 加载图片，默认设置了加载图片，出错图片，加载动画，不重设宽高
     *
     * @param loadObj           加载对象
     * @param imageView         图片
     * @param asGif             是否指定图片为Gif类型，null不指定，true指定为Gif，false指定为Bitmap
     * @param format            图片质量，只有设置图片为bitmap类型时才有效
     * @param transformation    图片转换
     * @param diskcacheStrategy 硬盘缓存策略
     */
    public static void loadDefaultTransformation(Object loadObj, ImageView imageView, Boolean asGif, DecodeFormat format, Transformation transformation, DiskCacheStrategy diskcacheStrategy) {
        load(imageView.getContext(), loadObj, imageView, 0, 0, R.drawable.ic_loading, R.drawable.ic_fail, R.anim.image_load, asGif, format, transformation,
                diskcacheStrategy);
    }

    /**
     * 加载图片，默认设置了加载图片，出错图片，加载动画，不重设宽高，不转换
     *
     * @param loadObj           加载对象
     * @param imageView         图片
     * @param asGif             是否指定图片为Gif类型，null不指定，true指定为Gif，false指定为Bitmap
     * @param format            图片质量，只有设置图片为bitmap类型时才有效
     * @param diskcacheStrategy 硬盘缓存策略
     */
    public static void loadDefault(Object loadObj, ImageView imageView, Boolean asGif, DecodeFormat format, DiskCacheStrategy diskcacheStrategy) {
        load(imageView.getContext(), loadObj, imageView, 0, 0, R.drawable.ic_loading, R.drawable.ic_fail, R.anim.image_load, asGif, format, null, diskcacheStrategy);
    }

    /**
     * 加载图片，默认设置了加载图片，出错图片，不重设宽高，不转换，没有加载动画
     *
     * @param loadObj           加载对象
     * @param imageView         图片
     * @param asGif             是否指定图片为Gif类型，null不指定，true指定为Gif，false指定为Bitmap
     * @param format            图片质量，只有设置图片为bitmap类型时才有效
     * @param diskcacheStrategy 硬盘缓存策略
     */
    public static void loadDefaultNoAnim(Object loadObj, ImageView imageView, Boolean asGif, DecodeFormat format, DiskCacheStrategy diskcacheStrategy) {
        load(imageView.getContext(), loadObj, imageView, 0, 0, R.drawable.ic_loading, R.drawable.ic_fail, 0, asGif, format, null, diskcacheStrategy);
    }

    /**
     * 加载图片
     *
     * @param context           上下文
     * @param loadObj           加载对象
     * @param imageView         图片
     * @param overrideW         重设宽度
     * @param overrideH         重设高度
     * @param holderId          加载中图片Id
     * @param errorId           错误图片Id
     * @param animId            动画Id
     * @param asGif             是否指定图片为Gif类型，null不指定，true指定为Gif，false指定为Bitmap
     * @param format            图片质量，只有设置图片为bitmap类型时才有效
     * @param transformation    图片转换
     * @param diskcacheStrategy 硬盘缓存策略
     */
    private static void load(Context context, Object loadObj, ImageView imageView, int overrideW, int overrideH, int holderId, int errorId, int animId, Boolean asGif, DecodeFormat format, Transformation transformation, DiskCacheStrategy diskcacheStrategy) {
        final RequestManager manager = Glide.with(context);

        DrawableTypeRequest request = null;
        if (loadObj instanceof Integer) {
            request = manager.load((Integer) loadObj);
        } else if (loadObj instanceof String) {
            request = manager.load((String) loadObj);
        } else if (loadObj instanceof Uri) {
            request = manager.load((Uri) loadObj);
        } else if (loadObj instanceof File) {
            request = manager.load((File) loadObj);
        }

        if (request == null) {
            return;
        }

        if (asGif != null) {
            if (asGif) {
                final GifTypeRequest gifTypeRequest = request.asGif();
                load(gifTypeRequest, imageView, overrideW, overrideH, holderId, errorId, animId, true, format, transformation, diskcacheStrategy);
            } else {
                final BitmapTypeRequest bitmapTypeRequest = request.asBitmap();
                load(bitmapTypeRequest, imageView, overrideW, overrideH, holderId, errorId, animId, false, format, transformation, diskcacheStrategy);
            }
        } else {
            load(request, imageView, overrideW, overrideH, holderId, errorId, animId, null, format, transformation, diskcacheStrategy);
        }
    }

    /**
     * 因为是链式调用，需要RequestBuilder层层调用才能全部生效，所以这里需要特殊处理
     *
     * @param request           请求
     * @param imageView         图片
     * @param overrideW         重设宽度
     * @param overrideH         重设高度
     * @param holderId          加载中图片Id
     * @param errorId           错误图片Id
     * @param animId            动画Id
     * @param asGif             是否指定图片为Gif类型，null不指定，true指定为Gif，false指定为Bitmap
     * @param format            图片质量，只有设置图片为bitmap类型时才有效
     * @param transformation    图片转换
     * @param diskcacheStrategy 硬盘缓存策略
     */
    @SuppressWarnings("unchecked")
    private static void load(GenericRequestBuilder request, ImageView imageView, int overrideW, int overrideH, int holderId, int errorId, int animId, Boolean asGif, DecodeFormat format, Transformation transformation, DiskCacheStrategy diskcacheStrategy) {
        // 通过builder一步一步构建，最后调用into才能设置生效；如果只是request调into不行
        GenericRequestBuilder requestBuilder = null;
        if (holderId != 0) {
            requestBuilder = request.placeholder(holderId);
        }
        if (errorId != 0) {
            if (requestBuilder != null) {
                requestBuilder.error(errorId);
            } else {
                requestBuilder = request.error(errorId);
            }
        }
        if (transformation != null) {
            if (requestBuilder != null) {
                requestBuilder.transform(transformation);
            } else {
                requestBuilder = request.transform(transformation);
            }
        }
        if (animId != 0) {
            if (requestBuilder != null) {
                requestBuilder.animate(animId);
            } else {
                requestBuilder = request.animate(animId);
            }
        }
        if (diskcacheStrategy != null) {
            if (requestBuilder != null) {
                requestBuilder.diskCacheStrategy(diskcacheStrategy);
            } else {
                requestBuilder = request.diskCacheStrategy(diskcacheStrategy);
            }
        }
        if (overrideW != 0 && overrideH != 0) {
            if (requestBuilder != null) {
                requestBuilder.override(overrideW, overrideH);
            } else {
                requestBuilder = request.override(overrideW, overrideH);
            }
        }
        if (asGif != null && !asGif) {
            if (requestBuilder != null && requestBuilder instanceof BitmapRequestBuilder) {
                // bitmap格式的特殊处理图片质量
                final BitmapRequestBuilder bitmapRequestBuilder = ((BitmapRequestBuilder) requestBuilder).format(format);
                bitmapRequestBuilder.into(imageView);
                return;
            } else if (requestBuilder == null && request instanceof BitmapTypeRequest) {
                // bitmap格式的特殊处理图片质量
                final BitmapRequestBuilder bitmapRequestBuilder = ((BitmapTypeRequest) request).format(format);
                bitmapRequestBuilder.into(imageView);
                return;
            }
        }
        if (requestBuilder != null) {
            if (BuildConfig.DEBUG) {
                requestBuilder.listener(new RequestListener() {
                    @Override
                    public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                        Log.e("TAG", "GlideUtils-205行-onException(): " + e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                });
            }
            requestBuilder.into(imageView);
        } else {
            request.into(imageView);
        }
    }


}
