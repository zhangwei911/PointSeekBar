### PointSeekBar(可打点进度条)[![Download](https://api.bintray.com/packages/viz/VCommon/pointSeekBar/images/download.svg)](https://bintray.com/viz/VCommon/pointSeekBar/_latestVersion)

gradle引用
```
implementation 'viz.commonlib:pointSeekBar:1.0.0'
```

![img](https://github.com/zhangwei911/PointSeekBar/blob/master/capture/point.gif?raw=true)

1. xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <viz.commonlib.pointseekbar.PointSeekBar
        android:id="@+id/pointSeekBar"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="20dp"
        android:layout_marginEnd="20dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

2. 代码

```kotlin
pointSeekBar.apply{
    //打点信息
    pointList = mutableListOf(
            PointInfo(10, desc = "进度10"),
            PointInfo(20, desc = "进度20"),
            PointInfo(35, desc = "进度35"),
            PointInfo(55, desc = "进度55"),
            PointInfo(83, desc = "进度83")
        )
    //默认只需要设置pointList,以下属性根据实际情况配置
    //最大进度(默认100)
    //max = 100
    //默认进度背景
    //defaultColor = Color.WHITE
    //当前进度颜色
    //defaultColor = Color.BLACK
    //当前位置圆形颜色
    //seekBarColor = Color.BLACK
    //当前位置圆形半径
    //seekBarRadius = 25
    //打点圆形半径
    //pointRadius = 10
    //进度条高度
    //progressHeight = 10
    //当前位置圆形点击范围与半径的比例
    //seekBarClickRangeRatio = 1.5
    //打点位置圆形点击范围与半径的比例
    //pointClickRangeRatio = 3
    //当前位置大于打点位置时是否改变打点圆形颜色为进度条颜色
    //isChangePointColor = true
    //进度变化事件
    progressChangedListener = { progress ->
        //TODO
    }
    //打点点击事件
    pointClickListener = { index, pointInfo ->
        //TODO
    }
    //开始拖拽事件
    dragStartListener = { progress ->
        //TODO
    }
    //正在拖拽事件
    draggingListener = { progress ->
        //TODO
    }
    //拖拽结束事件
    draggedListener = { progress ->
        //TODO
    }
}
```