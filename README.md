> 目前还只能自言自语,日后会考虑使用图灵机器人的API写一个完整的AI聊天程序
> 参考资料：《第一行代码》

### 效果
![](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doL2NuYXRvbS9pbWFnZXMvaW1hZ2VzLzIwMjAwNzI1MDkwNDIxLnBuZw?x-oss-process=image/format,png)
![](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doL2NuYXRvbS9pbWFnZXMvaW1hZ2VzLzIwMjAwNzI1MDkwNTMwLmdpZg)


### 布局源码
---
#### 整体布局`activity_main.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">
    <LinearLayout
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_gravity="center"
            android:layout_margin="16dp"
            android:text="只会说一句话的聊天机器人"
            android:textAlignment="center"
            android:textColor="#000000"
            android:textSize="18sp" />o

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/recyclerView"
            android:layout_width="match_parent"
            android:layout_height="0dp"
            android:layout_weight="1"/>
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:background="@drawable/bottom_background">

                <EditText
                    android:id="@+id/editText"
                    style="@style/Widget.AppCompat.AutoCompleteTextView"
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_margin="10dp"
                    android:layout_weight="1"
                    android:background="@null"
                    android:layout_gravity="center"
                    android:hint="@string/hint_text"
                    android:maxLines="2" />

                <Button
                    android:id="@+id/send_button"
                    style="@style/Widget.AppCompat.Button.Borderless.Colored"
                    android:textColor="#42D363"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="@string/send" />
            </LinearLayout>


    </LinearLayout>

</androidx.constraintlayout.widget.ConstraintLayout>
```
#### 左聊天气泡布局`msg_left_layout.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginRight="100dp"
    android:padding="10dp">

    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="left"
        android:layout_margin="5dp"
        android:background="@drawable/left_chat">

        <TextView
            android:id="@+id/leftText"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center"
            android:layout_marginLeft="10dp"
            android:layout_marginRight="10dp"
            android:textColor="#191919"
            android:textSize="16dp" />
    </LinearLayout>
</FrameLayout>
```


#### 右聊天气泡布局`msg_right_layout.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginLeft="100dp"
    android:padding="10dp">

    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="right"
        android:layout_margin="5dp"
        android:background="@drawable/right_chat">

        <TextView
            android:id="@+id/rightText"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center"
            android:layout_marginLeft="10dp"
            android:layout_marginRight="10dp"
            android:textColor="#fff"
            android:textSize="16dp" />
    </LinearLayout>


</FrameLayout>

```
#### 逻辑实现`MainActivity.kt`
```kotlin
package com.example.chatui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text
import java.lang.IllegalArgumentException
//定义消息的实体类
class Msg(val content:String,val type:Int){
    //定义静态成员
    companion object{
        const val RIGHT = 0
        const val LEFT = 1
    }
};
class MainActivity : AppCompatActivity() {
    //建立消息数据列表
    private val msgList = ArrayList<Msg>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()  //隐藏顶部状态栏
        initMsg()   //初始化聊天机器人的见面语
        recyclerView.layoutManager = LinearLayoutManager(this)  //布局为线性垂直
        val adapter = MsgAdapter(msgList)   //建立适配器实例
        recyclerView.adapter = adapter  //传入适配器
        send_button.setOnClickListener{
            val content:String = editText.text.toString()   //获取输入框的文本
            if(content.isNotEmpty()){
                msgList.add(Msg(content,Msg.RIGHT))     //将输入的消息及其类型添加进消息数据列表中
                adapter.notifyItemInserted(msgList.size-1)   //为RecyclerView添加末尾子项
                recyclerView.scrollToPosition(msgList.size-1) //跳转到当前位置
                editText.setText("")    //清空输入框文本
            }

        }

    }

    fun initMsg(){
        msgList.add(Msg("Hello",Msg.LEFT))
    }
}
class MsgAdapter(val msgList:List<Msg>):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    //载入右聊天框布局控件
    inner class RightViewHolder(val view: View):RecyclerView.ViewHolder(view){
        val rightMsg: TextView = view.findViewById(R.id.rightText)
    }
    //载入左聊天框布局控件
    inner class LeftViewHolder(val view:View):RecyclerView.ViewHolder(view){
        val leftMsg:TextView = view.findViewById(R.id.leftText)
    }
    //获取消息类型(左或者右),返回到onCreateViewHolder()方法的viewType参数里面
    override fun getItemViewType(position: Int): Int {
        val msg = msgList[position] //根据当前数据源的元素类型
        return msg.type
    }
    //根据viewType消息类型的不同,构建不同的消息布局(左&右)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType==Msg.LEFT){
            val leftView = LayoutInflater.from(parent.context).inflate(R.layout.msg_left_layout,parent,false)
            return LeftViewHolder(leftView) //返回控件+布局
        }else{
            val rightView = LayoutInflater.from(parent.context).inflate(R.layout.msg_right_layout,parent,false)
            return RightViewHolder(rightView)
        }

    }
    //对聊天控件的消息文本进行赋值
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = msgList[position]
        when(holder){
            is LeftViewHolder -> holder.leftMsg.text = msg.content
            is RightViewHolder -> holder.rightMsg.text = msg.content
            else -> throw IllegalArgumentException()
        }
    }
    //返回项数
    override fun getItemCount(): Int {
        return msgList.size
    }


}
```

