# About
可以动态的让某个View跟踪目标View显示，而不需要在xml中事先指定位置

## Gradle
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
        compile 'com.github.zj565061763:poper:1.0.8'
}

```

## 效果图
![](http://thumbsnap.com/i/ro6ry9kb.gif?0905)

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
        //.setMarginX(10) //设置x轴需要偏移的值，大于0往右，小于0往左
        //.setMarginY(10) //设置y轴方向的偏移量，大于0往下，小于0往上
        .setTarget(findViewById(R.id.tv_target)); //设置要跟踪的目标View

mPoper.setPosition(SDPoper.Position.TopLeft) //左上角对齐
        .attach(true); //true-依附目标view，false-移除依附
```
