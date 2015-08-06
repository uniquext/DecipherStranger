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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;
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
        this.MyLifeBroadcas();
    }

    private void init() {
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

/*    private ArrayList<Map<String, Object>> selectAll (){
        Bitmap  bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.life_class_food);
        ArrayList<Map<String, Object>> all = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 10; ++ i) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(MyStatic.LIFE_CLASS, bitmap);
            map.put(MyStatic.LIFE_NAME, "一起去吃饭啊");
            map.put(MyStatic.LIFE_TIME, "2015/07/28 9:16");
            map.put(MyStatic.LIFE_SPACE, "长春工业大学");
            all.add(map);
        }
        return all;
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
            Intent intent = new Intent(MyLifeActivity.this, DetailsActivity.class);
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
            case R.id.sendLife:
                break;
            /*参团*/
            case R.id.partakeLife:
                break;
            /*分享*/
            case R.id.share:
                break;
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
            if (intent.getAction().equals(MyStatic.LIFE_MAIN)) {
                // TODO 将获取的数据赋值到本地
                if (intent.getBooleanExtra("reResult", true)){
                    int lifeId = intent.getIntExtra("reId", 0);
                    int type = intent.getIntExtra("reType", 3);
                    String name = intent.getStringExtra("reName");
                    String time = intent.getStringExtra("reTime");
                    String place = intent.getStringExtra("rePlace");
                    setData(lifeId, type, name, time, place);
                }else{
                    System.out.println("### 哎哟我去");
                    Message message = new Message();
                    handler.sendMessage(message);
                }
            }
        }
    }
}
