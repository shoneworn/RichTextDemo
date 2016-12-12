package com.cb.example.richtextdemo;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by cb on 2016/12/10.
 */

public class ResourceUtils {
    private static ResourceUtils resourceUtils;
    private static Context context;
    private Resources resources;
    private String packageName;

    public static ResourceUtils getInstance(Context context) {
        if (resourceUtils == null) {
            synchronized (ResourceUtils.class) {
                resourceUtils = new ResourceUtils(context);
            }
        }

        return resourceUtils;
    }

    public ResourceUtils(Context context) {
        this.context = context;
        resources = context.getResources();
        packageName = context.getPackageName();
    }

    public int getStringId(String name) {
        try {
            //对应values：strings.xml文件
            return this.resources.getIdentifier(name, "string", packageName);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String getString(String name) {
        try {
            return this.resources.getString(getStringId(name));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public int getDrawableId(String name) {
        try {
            ////对应drawable-***文件夹中的图片
            return this.resources.getIdentifier(name, "drawable", packageName);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
