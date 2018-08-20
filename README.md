# About
可以动态的让某个View跟踪目标View显示，而不需要在xml中事先指定位置，内部的显示原理：<br>
![](http://thumbsnap.com/i/nR9WRCSi.png?0530)

# Gradle
[![](https://jitpack.io/v/zj565061763/poper.svg)](https://jitpack.io/#zj565061763/poper)

# 效果图
![](http://thumbsnap.com/i/BjBMtha4.gif?0608)

# 使用方法
```java
Poper poper = new FPoper(this)
        // 设置要popview，可以是布局id或者View对象
        .setPopView(R.layout.view_pop)
        // 设置左上角对齐
        .setPosition(Poper.Position.TopLeft)
        // 设置要跟踪的目标View
        .setTarget(view_target)
        // true-依附目标view，false-移除依附
        .attach(true);
```

# Layouter接口

Poper内部是调用Layouter对象的方法来更新popview的位置

```java
/**
 * 绘制接口
 */
interface Layouter
{
    /**
     * 绘制回调
     *
     * @param x             按照指定位置让popview和目标view对齐后，popview相对于父布局在x方向需要是什么值
     * @param y             按照指定位置让popview和目标view对齐后，popview相对于父布局在y方向需要是什么值
     * @param popView       popview
     * @param target        目标view
     */
    void layout(int x, int y, View popView, View target);
}
```

<br>
已经实现Layouter接口的实现类：

* DefaultLayouter 默认的layouter，只对popview的位置进行平移
* FixBoundaryLayouter 当popview边界超出父布局边界的时候， 修改popview的大小，让popview在父布局边界之内，支持宽和高
* CombineLayouter 组合的layouter，构造方法传入多个layouter组合

# Poper接口
```java
public interface Poper
{
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
     * {@link #setMarginX(Margin)}
     *
     * @param margin
     * @return
     */
    Poper setMarginX(int margin);

    /**
     * {@link #setMarginY(Margin)}
     *
     * @param margin
     * @return
     */
    Poper setMarginY(int margin);

    /**
     * 设置追踪到指定位置后，x值的偏移量，大于0往右，小于0往左
     *
     * @param margin
     * @return
     */
    Poper setMarginX(Margin margin);

    /**
     * 设置追踪到指定位置后，y值的偏移量，大于0往下，小于0往上
     *
     * @param margin
     * @return
     */
    Poper setMarginY(Margin margin);

    /**
     * 设置popview可以显示的容器范围<br>
     * 默认是Activity中id为android.R.id.content的容器
     *
     * @param container
     * @return
     */
    Poper setContainer(ViewGroup container);

    /**
     * 设置位置绘制对象
     *
     * @param layouter
     * @return
     */
    Poper setLayouter(Layouter layouter);

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
     * poper依附的时候会被以下对象强引用:
     * <p>
     * 1.目标view的ViewTreeObserver
     * <br>
     * 2.PopView
     * <p>
     * 调用此方法会释放掉以上强引用
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
         * 与target左边对齐
         */
        Left,
        /**
         * 与target顶部对齐
         */
        Top,
        /**
         * 与target右边对齐
         */
        Right,
        /**
         * 与target底部对齐
         */
        Bottom,
    }

    /**
     * 绘制接口
     */
    interface Layouter
    {
        /**
         * 绘制回调
         *
         * @param x       按照指定位置让popview和目标view对齐后，popview相对于父布局在x方向需要是什么值
         * @param y       按照指定位置让popview和目标view对齐后，popview相对于父布局在y方向需要是什么值
         * @param popView popview
         * @param target  目标view
         */
        void layout(int x, int y, View popView, View target);
    }

    interface Margin
    {
        int getMargin();
    }
}
```
