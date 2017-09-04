# About
可以动态的让某个View跟踪目标View显示，而不需要在xml中事先指定位置

## Gradle
`compile 'com.fanwe.android:poper:1.0.4'`

## 效果图
![](http://thumbsnap.com/i/69xnDyRq.gif?0814)

## 实现步骤
1. xml中放置需要跟踪的目标View
```xml
<TextView
    android:id="@+id/tv_target"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="center"
    android:background="@android:color/darker_gray"
    android:gravity="center"
    android:minHeight="100dp"
    android:minWidth="100dp"
    android:text="target"/>
```
2. java代码
```java
TextView popView = new TextView(this); //创建一个需要Pop的TextView
popView.setGravity(Gravity.CENTER);
popView.setText("PopView");
popView.setBackgroundColor(Color.RED);

mPoper = new SDPoper(this);
mPoper.setPopView(popView) //设置要Pop的View
        .setDynamicUpdate(true) //设置是否需要动态更新PopView的位置，true-当target大小或者位置发生变化的时候会动态更新popview的位置
        //.setRootLayout((FrameLayout) findViewById(R.id.fl_custom)) //设置PopView要被添加的Parent，不设置的话默认是android.R.id.content的FrameLayout
        //.setMarginX(10) //设置x轴需要偏移的值，大于0往右，小于0往左
        //.setMarginY(10) //设置y轴方向的偏移量，大于0往下，小于0往上
        .setTarget(findViewById(R.id.tv_target)); //设置要跟踪的目标View，不设置的话默认跟踪RootLayout

mPoper.setPosition(SDPoper.Position.TopLeft) //左上角对齐
        .attach(true); //true-依附目标view，false-移除依附
```
