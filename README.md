# About
可以动态的让某个View跟踪目标View显示，而不需要在xml中事先指定位置

## Gradle
[![](https://jitpack.io/v/zj565061763/poper.svg)](https://jitpack.io/#zj565061763/poper)

## 效果图
![](http://thumbsnap.com/i/GGwOSruz.gif?1103)

## 使用方法
```java

mPoper = new SDPoper(this)
        .setContainer(fl_container) // 设置popview可以显示的容器范围，默认是Activity中id为android.R.id.content的容器
        //.setMarginX(10) //设置x轴需要偏移的值，大于0往右，小于0往左
        //.setMarginY(10) //设置y轴方向的偏移量，大于0往下，小于0往上
        .setPopView(R.layout.view_pop) // 设置要popview，可以是布局id或者View对象
        .setTarget(findViewById(R.id.tv_target)) //设置要跟踪的目标View
        .setPosition(SDPoper.Position.TopLeft) //左上角对其
        .attach(true); //true-依附目标view，false-移除依附
```

## 支持指定的位置
```java
public enum Position
{
    /**
     * 与target左上角对齐
     */
    TopLeft,
    /**
     * 与target顶部中间对齐
     */
    TopCenter,
    /**
     * 与target右上角对齐
     */
    TopRight,

    /**
     * 与target左边中间对齐
     */
    LeftCenter,
    /**
     * 中间对齐
     */
    Center,
    /**
     * 与target右边中间对齐
     */
    RightCenter,

    /**
     * 与target左下角对齐
     */
    BottomLeft,
    /**
     * 与target底部中间对齐
     */
    BottomCenter,
    /**
     * 与target右下角对齐
     */
    BottomRight,

    /**
     * 在target的顶部外侧靠左对齐
     */
    TopLeftOutside,
    /**
     * 在target的顶部外侧左右居中
     */
    TopCenterOutside,
    /**
     * 在target的顶部外侧靠右对齐
     */
    TopRightOutside,

    /**
     * 在target的底部外侧靠左对齐
     */
    BottomLeftOutside,
    /**
     * 在target的底部外侧左右居中
     */
    BottomCenterOutside,
    /**
     * 在target的底部外侧靠右对齐
     */
    BottomRightOutside,

    /**
     * 在target的左边外侧靠顶部对齐
     */
    LeftTopOutside,
    /**
     * 在target的左边外侧上下居中
     */
    LeftCenterOutside,
    /**
     * 在target的左边外侧靠底部对齐
     */
    LeftBottomOutside,

    /**
     * 在target的右边外侧靠顶部对齐
     */
    RightTopOutside,
    /**
     * 在target的右边外侧上下居中
     */
    RightCenterOutside,
    /**
     * 在target的右边外侧靠底部对齐
     */
    RightBottomOutside,
}
```
