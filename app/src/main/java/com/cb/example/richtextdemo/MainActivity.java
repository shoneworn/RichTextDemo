package com.cb.example.richtextdemo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.widget.TextView;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private TextView textView;

    private HashMap<String, Integer> localIconMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text_view);
        initData(MainActivity.this);
        setRichText(MainActivity.this);
    }

    private void initData(Context context) {
        ResourceUtils utils = ResourceUtils.getInstance(context);
        localIconMap = new HashMap<>();
        //获取本地图片标示对应的图片ID（例如[哈哈]对应的R.drawable.haha）
        localIconMap.put(utils.getString("haha"), utils.getDrawableId("haha"));
        localIconMap.put(utils.getString("lei"), utils.getDrawableId("lei"));
        localIconMap.put(utils.getString("duorou"), utils.getDrawableId("duorou"));
        localIconMap.put(utils.getString("duorou2"), utils.getDrawableId("duorou2"));
    }

    private void setRichText(Context context) {
        float imageSize = textView.getTextSize();

        //图文混排的text，这里用"[]"标示图片
        String text = "这是图文混排的例子：\n网络图片："
                + "[http://hao.qudao.com/upload/article/20160120/82935299371453253610.jpg][http://b.hiphotos.baidu.com/zhidao/pic/item/d6ca7bcb0a46f21f27c5c194f7246b600d33ae00.jpg]"
                + "\n本地图片：" + "[哈哈][泪][多肉][多肉2]";

        SpannableString spannableString = new SpannableString(text);
        //匹配"[(除了']'任意内容)]"的正则表达式，获取网络图片和本地图片替换位置
        Pattern pattern = Pattern.compile("\\[[^\\]]+\\]");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            ImageSpan imageSpan;
            //匹配的内容，例如[http://hao.qudao.com/upload/article/20160120/82935299371453253610.jpg]或[哈哈]
            String group = matcher.group();
            if (group.contains("http")) {
                //网络图片
                //获取图片url（去掉'['和']'）
                String url = group.substring(1, group.length() - 1);
                //异步获取网络图片
                Drawable drawableFromNet = new URLImageParser(textView, context, (int) imageSize).getDrawable(url);
                imageSpan = new ImageSpan(drawableFromNet, ImageSpan.ALIGN_BASELINE);
                //设置网络图片
                spannableString.setSpan(imageSpan, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            } else {
                //本地图片
                if (localIconMap.get(group) != null) {
                    //获取本地图片Drawable
                    Drawable drawableFromLocal = context.getResources().getDrawable(localIconMap.get(group));
                    //获取图片宽高比
                    float ratio = drawableFromLocal.getIntrinsicWidth() * 1.0f / drawableFromLocal.getIntrinsicHeight();
                    //设置图片宽高
                    drawableFromLocal.setBounds(0, 0, (int) (imageSize * ratio), (int)(imageSize));
                    imageSpan = new ImageSpan(drawableFromLocal, ImageSpan.ALIGN_BASELINE);
                    //设置本地图片
                    spannableString.setSpan(imageSpan, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }

        textView.setText(spannableString);
    }
}
