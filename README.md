
> 布局优化是性能优化的一个方向点，包括了根据需求应该选用哪种布局容器、ViewStub懒加载，如何减少布局层级等，今天我们要探讨的就是如何使用ConstraintLayout来优化我们的布局层级。

![概述.png](https://upload-images.jianshu.io/upload_images/6921358-68b6d5a7158a8e65.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 提出问题
1. 为什么要用这个布局？
2. 怎么用这个布局？
3. 不足在哪里？

### 配置
引入最新版本constraint layout库
```
implementation 'com.android.support.constraint:constraint-layout:1.1.3'
```

### 旧界面转换为ConstraintLayout
首先，AS支持一键将已有的布局文件转成ConstraintLayout，是不是很贴心，要做就做全套的。

![QQ20190129-211921@2x.png](https://upload-images.jianshu.io/upload_images/6921358-bca1f5ec7ecfdf97.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 优势
ConstraintLayout就是为性能而生，目标就是减少布局嵌套，提高measure+layout性能，来看看官方给出的数据。

![性能比较结果.jpeg](https://upload-images.jianshu.io/upload_images/6921358-80797cd01095f97b.jpeg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

ConstraintLayout 在测量/布局阶段的性能比 RelativeLayout大约高40%！而它使用的性能检测工具是Android 7.0（API 级别 24）中引入的 OnFrameMetricsAvailableListener。通过该类，你可以收集有关应用界面渲染的逐帧时间信息，进而比较分析不同布局每次测量和布局操作所花费的时间。

另外使用AS的图形化界面可以非常方便的完成布局操作，这里不赘述了文末有参考文章。

### 用法
#### 1. 属性layout_constraintXXX_toYYYOf
xxx表示该控件在哪个方向的约束，YYY表示该约束指向的控件的方向(我已经尝试说人话了...)，它们的可以是 left/right/top/bottom/start/end的任意一种，包含了水平方向和垂直方向的约束，属性的值为目标控件的id，看个例子item1_constraint.xml。
```
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

  <TextView
      android:id="@+id/titleTextView"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:text="Hello World1!"
      app:layout_constraintLeft_toLeftOf="parent"
      app:layout_constraintRight_toRightOf="parent"
      app:layout_constraintTop_toTopOf="parent" />

  <TextView
      android:id="@+id/subtitleTextView"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:text="Hello World2!"
      app:layout_constraintLeft_toLeftOf="parent"
      app:layout_constraintRight_toRightOf="parent"
      app:layout_constraintTop_toBottomOf="@id/titleTextView" />
</android.support.constraint.ConstraintLayout>
```

![效果图.png](https://upload-images.jianshu.io/upload_images/6921358-ff6a3fc6cc12fc99.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

可以看到两个控件水平方向的约束都指向parent也就是父控件ConstraintLayout，由于两边拉力作用就成了居中的样式，垂直方向上由于第二个textview使用了约束```app:layout_constraintTop_toBottomOf="@id/titleTextView"```，即它的顶部约束为在第一个textview的下面，也就形成了效果图的样子。

#### 2. match_constraint
由于ConstraintLayout的设计就是不想出现层次嵌套，这导致传统布局里使用的match_parent也就不适用了。那如何实现match_parent的效果呢？来看一个例子test_match_constraint.xml。

```
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <Button
        android:id="@+id/bt1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="button1" />
    <Button
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:text="button1"
        app:layout_constraintLeft_toRightOf="@id/bt1"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="@id/bt1"/>
</android.support.constraint.ConstraintLayout>
```
![效果图2.png](https://upload-images.jianshu.io/upload_images/6921358-f1540ae5c7ad4ebb.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

可以看到将第二个button的layout_width设置为0dp后，它并没有不显示出来而是占据的水平方向的剩余空间。没错，在ConstraintLayout中宽高指定为0dp表示的是在满足约束条件下的最大值，也即match_constraint。

#### 3. 宽高比layout_constraintDimensionRatio
不多BB直接看示例test_dimension_ratio.xml
```
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <TextView
        android:id="@+id/banner"
        android:layout_width="300dp"
        android:layout_height="0dp"
        android:text="banner"
        android:gravity="center"
        android:textSize="30sp"
        android:background="@android:color/holo_blue_light"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintDimensionRatio="2:1"/>
</android.support.constraint.ConstraintLayout>
```
![效果图3.png](https://upload-images.jianshu.io/upload_images/6921358-8869caa32f56746a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

可以看到设置```app:layout_constraintDimensionRatio="2:1```属性表示宽高比为2:1，在已经限定控件宽度为300dp时，高度指定为0dp则可自己算出实际高度。

#### 4. radius角度约束
秒懂的属性，直接来看例子test_radius.xml
```
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <Button
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="按钮"
        app:layout_constraintCircle="@id/fab_menu"
        app:layout_constraintCircleRadius="100dp"
        app:layout_constraintCircleAngle="45"/>
    <Button
        android:id="@+id/fab_menu"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:tint="#fff"
        android:text="btn1"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
</android.support.constraint.ConstraintLayout>
```
![效果图4.png](https://upload-images.jianshu.io/upload_images/6921358-c4bf39a5716c54c1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 5. bias偏向拉力比例
示例test_bias.xml
```
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <Button
        android:id="@+id/button"
        android:layout_width="50dp"
        android:layout_height="wrap_content"
        app:layout_constraintHorizontal_bias="0.8"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent" />
</android.support.constraint.ConstraintLayout>
```
![效果图5.png](https://upload-images.jianshu.io/upload_images/6921358-d079f0d5412ada24.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

可以看到虽然水平方向都有拉力，但设置了```layout_constraintHorizontal_bias```属性后产生了偏移，属性值从0到1，0为最左侧1为最右侧，需要注意的是该属性只有水平方向上都有约束才会生效。

#### 6. 链chain
水平或垂直方向上相互约束的两个或更多view组成一个约束链，通常可用于实现底部导航样式，示例test_chain.xml
```
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <Button
        android:id="@+id/bt1"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        app:layout_constraintHorizontal_weight="2"
        android:text="第一个"
        android:textSize="30sp"
        app:layout_constraintHorizontal_chainStyle="spread_inside"
        app:layout_constraintRight_toLeftOf="@id/bt2"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent" />
    <Button
        android:id="@+id/bt2"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:text="第二个"
        android:textSize="30sp"
        app:layout_constraintHorizontal_weight="1"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toRightOf="@id/bt1"
        app:layout_constraintRight_toLeftOf="@id/bt3"/>
    <Button
        android:id="@+id/bt3"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="第三个"
        android:textSize="30sp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintLeft_toRightOf="@id/bt2" />
</android.support.constraint.ConstraintLayout>
```

效果图


![效果图6.png](https://upload-images.jianshu.io/upload_images/6921358-f05dc47dc66b332c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

其中```layout_constraintHorizontal_chainStyle```属性可设置一个约束链中view的分布情况。
![chain.jpeg](https://upload-images.jianshu.io/upload_images/6921358-914f5c56e0630bf4.jpeg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
本例结合```layout_constraintHorizontal_weight```属性实现了weight chain效果。

#### 7. GuideLine参照线
为方便的指定约束的参照对象，ConstraintLayout内部可通过GuideLine来实现，它不会显示在界面中。
示例test_guideline.xml
```
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <android.support.constraint.Guideline
        android:id="@+id/guideline"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        app:layout_constraintGuide_begin="100dp"/>
    <Button
        android:id="@+id/btn1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="btn1"
        android:textSize="30sp"
        app:layout_constraintLeft_toRightOf="@+id/guideline"
        app:layout_constraintTop_toTopOf="parent"/>
</android.support.constraint.ConstraintLayout>
```

![效果图7.png](https://upload-images.jianshu.io/upload_images/6921358-aac06716334faa0d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

可以看到通过```layout_constraintGuide_begin```指定具体偏移值，而通过```layout_constraintGuide_percent```可指定偏移百分比。

#### 8. 属性goneMargin

当约束对象可见性设置为gone后生效，示例test_gone_margin.xml。

```
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <Button
        android:id="@+id/bnt1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="30sp"
        app:layout_constraintLeft_toLeftOf="parent"
        android:visibility="gone"
        android:text="bnt1"/>

    <Button
        android:id="@+id/bnt2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="30sp"
        app:layout_constraintLeft_toRightOf="@id/bnt1"
        app:layout_goneMarginLeft="200dp"
        android:visibility="visible"
        android:text="bnt2"/>
</android.support.constraint.ConstraintLayout>
```
![效果图8.png](https://upload-images.jianshu.io/upload_images/6921358-c7bd0ec35ab34bd1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 9. 其他
- layout_constrainedWidth
针对view宽高为wrap_content时，控件的实际宽高超过了约束条件时应该如何显示，默认是false，示例test_constraint_width.xml。
- 控件Barrier-- 限定范围，示例test_barrier.xml。
- 控件Group--引用的组件可批量设置属性，示例test_group.xml。
- 控件PlaceHolder--占位符，示例test_place_holder.xml。
- 其他综合练习参照test_common.xml、test_video_constraint.xml。

### 注意事项
- 没有约束的layout_marginTop等属性无效。

### 代码布局
实际开发过程中难免需要使用代码动态布局，整体思路为下面的代码，具体示例参照ProgramActivity.class。
```
//1.创建约束条件
ConstraintSet set = new ConstraintSet();
//2.从一个ConstraintLayout中克隆出所有的约束条件。
set.clone(layout)
//3.约束条件和constraintLayout绑定
set.applyTo(constraintLayout);
```
### 缺点
- xml文件没有布局层次，可读性差
- 代码布局复杂性高
- 约束条件复杂时排查难度大

### 性能对比
下面是最重要的一趴，ConstraintLayout真的如官方宣传的具备高性能吗？我们使用简单布局和复杂布局两种场景模拟实际开发情况，使用的单元测试为LayoutTest.class，得到如下结果。

![简单布局性能比较.png](https://upload-images.jianshu.io/upload_images/6921358-40e91551dc3a31ba.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

简单布局表示布局中仅有一层嵌套，上图横坐标表示一次测量和布局的时间，单位为ms，纵轴表示直观上这个简单布局使用哪种传统布局更方便实现，比如我们要实现一个纵向的简单列表，显然线性布局更易实现。通过结果可以看到ConstraintLayout的性能比传统布局的性能差至少一倍。

![复杂布局性能比较.png](https://upload-images.jianshu.io/upload_images/6921358-e7fb7d14d3a14b56.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

如果说简单布局耗时本来就极少参考意义不大，那复杂布局呢？复杂布局情况下中使用传统布局会导致3-4层的嵌套，符合常见的开发场景，但性能方面仍优于ConstraintLayout，其中FrameLayout的性能表现最好。

### 结语
关于性能的评测国外的文章也很多，都有得出类似的结论，他们大都归咎于随着ConstrainLayout版本的升级，引入更多的功能而导致性能下降，但博主使用许多更低版本的库测试，结果差别也不大，这就是为什么文章要起这个标题。如果性能没跟上，那由于ConstrainLayout许多缺点的存在，我们完全没有理由选择使用它，期待官方会做进一步优化工作。当然博主后续也会使用其他工具(systrace等)进一步验证此结论。

### 参考文章：
- [官方文档](https://developer.android.com/reference/android/support/constraint/ConstraintLayout)
- [Android新特性介绍，ConstraintLayout完全解析](https://blog.csdn.net/guolin_blog/article/details/53122387)
- [ConstraintLayout 完全解析 快来优化你的布局吧](https://blog.csdn.net/lmj623565791/article/details/78011599)
- [实战篇ConstraintLayout的崛起之路](https://www.jianshu.com/p/a74557359882)
- [ConstraintLayout 采用代码方式布局用法简介](http://xgfe.github.io/2017/09/17/ivanchou/layout-with-constraintlayout-by-programming/)
- [解析ConstraintLayout的性能优势](https://mp.weixin.qq.com/s/gGR2itbY7hh9fo61SxaMQQ)--[英文版](https://android-developers.googleblog.com/2017/08/understanding-performance-benefits-of.html)
- [ConstraintLayout UI性能分析](https://www.cnblogs.com/liujingg/p/7161319.html)
- [Constraint Layout performance](https://android.jlelse.eu/constraint-layout-performance-870e5f238100)