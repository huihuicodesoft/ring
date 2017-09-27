package cn.com.wh.photo;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Created by Hui on 2017/9/27.
 */

@GlideModule
public final class MainAppGlideModule extends AppGlideModule {
    private static final String TAG = MainAppGlideModule.class.getName();

    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context).build();
        Log.v(TAG, "glide内存大小 = " + calculator.getMemoryCacheSize());
        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));
    }
}
