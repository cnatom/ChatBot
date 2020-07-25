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