# About
可以动态的让某个View跟踪目标View显示，而不需要在xml中事先指定位置，内部的显示原理：<br>
![](http://thumbsnap.com/i/nR9WRCSi.png?0530)

Poper会监听Activity中id为android.R.id.content布局的OnPreDrawListener和popview父布局的onLayout来更新popview相对于目标view的位置

# Gradle
`implementation 'com.fanwe.android:poper:1.0.50'`

# 效果图
![](http://thumbsnap.com/i/GGwOSruz.gif?1103)

# 使用方法
```java
Poper poper = new FPoper(this)
        .setDebug(true)
//      .setContainer(fl_container) // 设置popview可以显示的容器范围，默认是Activity中id为android.R.id.content的容器
        .setPopView(R.layout.view_pop) // 设置要popview，可以是布局id或者View对象
        .setPosition(Poper.Position.TopLeft) //左上角对齐
//      .setMarginX(10) // 设置对齐后x轴方向的偏移量，大于0往右，小于0往左
//      .setMarginY(10) // 设置对齐后y轴方向的偏移量，大于0往下，小于0往上
        .setTarget(tv_target) // 设置要跟踪的目标View
        .attach(true); // //true-依附目标view，false-移除依附
```

# 关于popview的内容超过父布局
Poper内部在每次触发位置刷新的时候，为了提高效率只对popview进行了平移，但是提供了回调接口Layouter，开发者可以为Poper设置Layouter来实现刷新位置的时候执行更多的逻辑<br>
<br>
目前库内部提供的实现类有：
1. FixBoundLayouter 可用于popview的边界超出父布局的时候修正popview的大小
2. ViewBoundLayouter 可用于让popview跟踪某个view的大小，比如让popview的宽度和目标view的宽度保持一致
<br> <br>
详细请参考demo中的AutoActivity

# Poper接口
```java
public interface Poper
{
    /**
     * 设置是否调试模式，过滤日志tag:Poper
     *
     * @param debug
     * @return
     */
    Poper setDebug(boolean debug);

    /**
     * 设置要Pop的view
     *
     * @param layoutId 布局id
     * @return
     */
    Poper setPopView(int layoutId);

    /**
     * 设置要Pop的view
     *
     * @param popView
     * @return
     */
    Poper setPopView(View popView);

    /**
     * 设置目标view
     *
     * @param target
     */
    Poper setTarget(View target);

    /**
     * 设置显示的位置
     *
     * @param position
     */
    Poper setPosition(Position position);

    /**
     * 设置对齐后x轴方向的偏移量，大于0往右，小于0往左
     *
     * @param marginX
     */
    Poper setMarginX(int marginX);

    /**
     * 设置对齐后y轴方向的偏移量，大于0往下，小于0往上
     *
     * @param marginY
     */
    Poper setMarginY(int marginY);

    /**
     * 设置popview可以显示的容器范围<br>
     * 默认是Activity中id为android.R.id.content的容器
     *
     * @param container
     * @return
     */
    Poper setContainer(ViewGroup container);

    /**
     * 设置popview的父布局，必须实现{@link PoperParent}接口
     *
     * @param parent
     * @return
     */
    Poper setPoperParent(ViewGroup parent);

    /**
     * 添加{@link Layouter}
     *
     * @param layouter
     * @return
     */
    Poper addLayouter(Layouter layouter);

    /**
     * 移除{@link Layouter}
     *
     * @param layouter
     * @return
     */
    Poper removeLayouter(Layouter layouter);

    /**
     * 返回popview
     *
     * @return
     */
    View getPopView();

    /**
     * 返回Target
     *
     * @return
     */
    View getTarget();

    /**
     * 当前PopView是否已经被添加到Parent
     *
     * @return
     */
    boolean isAttached();

    /**
     * 把PopView添加到Parent
     *
     * @param attach
     * @return
     */
    Poper attach(boolean attach);

    /**
     * poper会被以下对象强引用：<br>
     * 1. Activity中id为android.R.id.content容器的ViewTreeObserver对象<br>
     * 2. popview父布局对象<br>
     * <p>
     * 调用此方法会断开上面指向poper的引用，并清空通过以下方法设置的view：<br>
     * {@link #setPopView(View)} <br>
     * {@link #setTarget(View)} <br>
     * {@link #setPoperParent(ViewGroup)} <br>
     * {@link #setContainer(ViewGroup)}
     */
    void release();

    enum Position
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
        TopOutsideLeft,
        /**
         * 在target的顶部外侧左右居中
         */
        TopOutsideCenter,
        /**
         * 在target的顶部外侧靠右对齐
         */
        TopOutsideRight,

        /**
         * 在target的底部外侧靠左对齐
         */
        BottomOutsideLeft,
        /**
         * 在target的底部外侧左右居中
         */
        BottomOutsideCenter,
        /**
         * 在target的底部外侧靠右对齐
         */
        BottomOutsideRight,

        /**
         * 在target的左边外侧靠顶部对齐
         */
        LeftOutsideTop,
        /**
         * 在target的左边外侧上下居中
         */
        LeftOutsideCenter,
        /**
         * 在target的左边外侧靠底部对齐
         */
        LeftOutsideBottom,

        /**
         * 在target的右边外侧靠顶部对齐
         */
        RightOutsideTop,
        /**
         * 在target的右边外侧上下居中
         */
        RightOutsideCenter,
        /**
         * 在target的右边外侧靠底部对齐
         */
        RightOutsideBottom,
    }

    /**
     * Poper绘制接口
     * <p>
     * 可用于修正popview的宽高
     */
    interface Layouter
    {
        /**
         * 绘制回调
         *
         * @param popView
         * @param popViewParent popview父布局
         * @param poper
         */
        void layout(View popView, View popViewParent, FPoper poper);
    }
}
```

# PoperParent接口
```java
/**
 * popview父布局
 */
public interface PoperParent
{
    /**
     * 给当前布局设置一个layout回调，当前布局应该在{@link View#onLayout(boolean, int, int, int, int)}里面通知回调对象
     *
     * @param onLayoutCallback
     */
    void setOnLayoutCallback(OnLayoutCallback onLayoutCallback);

    /**
     * 把当前布局添加到容器
     *
     * @param container
     */
    void addToContainer(ViewGroup container);

    /**
     * 把popView添加到当前布局
     *
     * @param popView
     */
    void addPopView(View popView);

    /**
     * 同步target的可见状态到当前布局
     *
     * @param isShown true-target可见，false-target不可见
     */
    void synchronizeVisibilityWithTarget(boolean isShown);

    /**
     * 更新popView的位置
     *
     * @param x       popview相对父布局的x
     * @param y       popview相对父布局的y
     * @param popView
     */
    void layoutPopView(int x, int y, View popView);

    /**
     * 把当前布局从父布局移除
     */
    void remove();

    interface OnLayoutCallback
    {
        void onLayout();
    }
}
```
