package com.android.decipherstranger.activity.LifeActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.decipherstranger.Network.NetworkService;
import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;
import com.android.decipherstranger.activity.Base.MyApplication;
import com.android.decipherstranger.util.MyStatic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * へ　　　　　／|
 * 　　/＼7　　　 ∠＿/
 * 　 /　│　　 ／　／
 * 　│　Z ＿,＜　／　　 /`ヽ
 * 　│　　　　　ヽ　　 /　　〉
 * 　 Y　　　　　`　 /　　/
 * 　ｲ●　､　●　　⊂⊃〈　　/
 * 　()　 へ　　　　|　＼〈
 * 　　>ｰ ､_　 ィ　 │ ／／      去吧！
 * 　 / へ　　 /　ﾉ＜| ＼＼        比卡丘~
 * 　 ヽ_ﾉ　　(_／　 │／／           消灭代码BUG
 * 　　7　　　　　　　|／
 * 　　＞―r￣￣`ｰ―＿
 *
 * @author penghaitao
 * @version V1.1
 * @Date 2015/7/30 13:40
 * @e-mail penghaitao918@163.com
 */
public class MyLifeActivity extends BaseActivity {

    private TextView textView0 = null;
    private TextView textView1 = null;
    private RelativeLayout topLayout = null;
    private ListView listView = null;
    private SimpleAdapter simpleAdapter = null;
    private ArrayList<Map<String, Object>> dataList = null;
    private MyLifeBroadcastReceiver receiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_life_my);
        this.init();
        this.initData();
        this.getData("send");
        this.MyLifeBroadcas();
    }

    private void init() {
        this.textView0 = (TextView) super.findViewById(R.id.sendText);
        this.textView1 = (TextView) super.findViewById(R.id.partakeText);
        this.dataList = new ArrayList<Map<String, Object>>();
        this.listView = (ListView) super.findViewById(R.id.listView);
        this.listView.setOnItemClickListener(new OnItemClickListenerImpl());

        /*锁定聚焦到顶部*/
        topLayout = (RelativeLayout) super.findViewById(R.id.top);
        topLayout.setFocusable(true);
        topLayout.setFocusableInTouchMode(true);
        topLayout.requestFocus();
    }

    private void initData() {
    //    this.dataList.addAll(this.selectAll());
        this.simpleAdapter = new SimpleAdapter(this,
                this.dataList,
                R.layout.list_item_life,
                new String[] {MyStatic.LIFE_CLASS, MyStatic.LIFE_NAME, MyStatic.LIFE_TIME, MyStatic.LIFE_SPACE},
                new int[] {R.id.life_class, R.id.life_name, R.id.life_time, R.id.life_space}
        );
        /*实现ViewBinder()这个接口*/
        simpleAdapter.setViewBinder(new ViewBinderImpl());
        this.listView.setAdapter(simpleAdapter);
        /*动态跟新ListView*/
        simpleAdapter.notifyDataSetChanged();
    }

    private class ViewBinderImpl implements SimpleAdapter.ViewBinder {
        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {
            // TODO Auto-generated method stub
            if(view instanceof ImageView && data instanceof Bitmap){
                ImageView i = (ImageView)view;
                i.setImageBitmap((Bitmap) data);
                return true;
            }
            return false;
        }
    }

/*    private void fixListViewHeight(ListView listView) {
        // 如果没有设置数据适配器，则ListView没有子项，返回。
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        if (listAdapter == null) {
            return;
        }
        for (int index = 0, len = listAdapter.getCount(); index < len; ++index) {
            View listViewItem = listAdapter.getView(index, null, listView);
            // 计算子项View 的宽高
            listViewItem.measure(0, 0);
            // 计算所有子项的高度和
            totalHeight += listViewItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // listView.getDividerHeight()获取子项间分隔符的高度
        // params.height设置ListView完全显示需要的高度
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }*/

    private void setData (int lifeId, int type, String name, String time, String place){
        Bitmap bitmap = null;
        switch (type) {
            // 美食
            case 1:bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.life_class_food);
                break;
            // 旅游
            case 2:bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.life_class_travel);
                break;
            // 休闲娱乐
            case 3:bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.life_class_other);
                break;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(MyStatic.LIFE_ID, lifeId);
        map.put(MyStatic.LIFE_CLASSINT, type);
        map.put(MyStatic.LIFE_CLASS, bitmap);
        map.put(MyStatic.LIFE_NAME, name);
        map.put(MyStatic.LIFE_TIME, time);
        map.put(MyStatic.LIFE_SPACE, place);
        dataList.add(map);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            simpleAdapter.notifyDataSetChanged();
            listView.setAdapter(simpleAdapter);
        }
    };

    private class OnItemClickListenerImpl implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int lifeId = (int) dataList.get(position).get(MyStatic.LIFE_ID);
            int lifeType = (int) dataList.get(position).get(MyStatic.LIFE_CLASSINT);
            Intent intent = new Intent(MyLifeActivity.this, DetailsActivity.class);
            intent.putExtra(MyStatic.LIFE_ID, lifeId);
            intent.putExtra(MyStatic.LIFE_CLASSINT, lifeType);
            startActivity(intent);
        }
    }

    public void MyLifeOnClick(View view) {
        switch (view.getId()) {
            /*返回*/
            case R.id.life_back_button:
                onBackPressed();
                break;
            /*发起*/
            case R.id.mySend:
                getData("send");
                textView0.setTextColor(getResources().getColor(R.color.red));
                textView1.setTextColor(getResources().getColor(R.color.black_grey));
                break;
            /*参团*/
            case R.id.myPartake:
                getData("attend");
                textView0.setTextColor(getResources().getColor(R.color.black_grey));
                textView1.setTextColor(getResources().getColor(R.color.red));
                break;
            /*分享*/
            case R.id.share:
                break;
        }
    }

    private void getData(String  flag) {
        if (NetworkService.getInstance().getIsConnected()){
            String Msg = "type"+":"+"26"+":"+"re_matter"+":"+flag+":"
                    +"account"+":"+ MyApplication.getInstance().getAccount();
            Log.v("aaaaa", Msg);
            NetworkService.getInstance().sendUpload(Msg);
        }else {
            NetworkService.getInstance().closeConnection();
            Log.v("晒图", "服务器连接失败");
        }
    }

    private void MyLifeBroadcas() {
        //动态方式注册广播接收者
        this.receiver = new MyLifeBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyStatic.LIFE_MAIN);
        this.registerReceiver(receiver, filter);
    }

    public class MyLifeBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MyStatic.LIFE_MY_LIFE)) {
                // TODO 将获取的数据赋值到本地
                if (intent.getBooleanExtra("reResult", true)){
                    int lifeId = intent.getIntExtra("reId", 0);
                    int type = intent.getIntExtra("reType", 3);
                    String name = intent.getStringExtra("reName");
                    String time = intent.getStringExtra("reTime");
                    String place = intent.getStringExtra("rePlace");
                    setData(lifeId, type, name, time, place);
/*                    if (intent.getStringExtra("reMatter").equals("send")){
                        //TODO 发起活动赋值
                    }else {
                        //TODO 参加活动赋值
                    }*/
                }else if (intent.getStringExtra("reResult").equals("finish")){
                    System.out.println("### 哎哟我去");
                    Message message = new Message();
                    handler.sendMessage(message);
/*                    if (intent.getStringExtra("reMatter").equals("send")){
                        //TODO 显示发起活动
                    }else {
                        //TODO 显示参加活动
                    }*/
                }else {
                    if (intent.getStringExtra("reMatter").equals("send")){
                        //TODO 提示还未发起活动
                        Toast.makeText(MyLifeActivity.this,"您当前没有发布的活动",Toast.LENGTH_SHORT).show();
                    }else {
                        //TODO 提示还未参加活动
                        Toast.makeText(MyLifeActivity.this,"您当前没有参与的活动",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
