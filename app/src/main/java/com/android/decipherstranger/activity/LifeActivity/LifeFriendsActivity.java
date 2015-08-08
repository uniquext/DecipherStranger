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
import android.widget.SimpleAdapter;

import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;
import com.android.decipherstranger.activity.Base.MyApplication;
import com.android.decipherstranger.activity.SubpageActivity.NearbyInfoActivity;
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
 * @Date 2015/8/8 13:43
 * @e-mail penghaitao918@163.com
 */
public class LifeFriendsActivity extends BaseActivity {

    private int Id = -1;
    private ListView listView = null;
    private SimpleAdapter simpleAdapter = null;
    private ArrayList<Map<String, Object>> dataList = null;
    private LifeFriendsBroadcast receiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_main);
        this.init();
        this.initData();
        this.getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.unregisterReceiver(LifeFriendsActivity.this.receiver);
    }

    private void init() {
        this.Id = getIntent().getIntExtra(MyStatic.LIFE_ID, 0);
        this.dataList = new ArrayList< Map<String, Object> >();
        this.listView = (ListView) super.findViewById(R.id.listView);
        this.listView.setOnItemClickListener(new OnItemClickListenerImpl());
    }

    private void initData() {
        this.simpleAdapter = new SimpleAdapter(this,
                this.dataList,
                R.layout.list_item_life_friends,
                new String[] {MyStatic.USER_PORTRAIT, MyStatic.USER_NAME, MyStatic.SHARE_SEX},
                new int[] {R.id.life_friends_portrait, R.id.life_friends_name, R.id.life_friends_sex}
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

    private void getData() {
        //  send Id
    }

    private void setData(String account, Bitmap portrait, String name, int sex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(MyStatic.USER_ACCOUNT, account);
        map.put(MyStatic.USER_PORTRAIT, portrait);
        map.put(MyStatic.USER_NAME, name);
        map.put(MyStatic.USER_SEX, sex);
        if (sex == 0) {
            map.put(MyStatic.SHARE_SEX, BitmapFactory.decodeResource(LifeFriendsActivity.this.getResources(),R.drawable.ic_sex_female));
        } else {
            map.put(MyStatic.SHARE_SEX, BitmapFactory.decodeResource(LifeFriendsActivity.this.getResources(),R.drawable.ic_sex_male));
        }
        dataList.add(map);
    }

    private class OnItemClickListenerImpl implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(LifeFriendsActivity.this, NearbyInfoActivity.class);
            intent.putExtra("account", (String) dataList.get(position).get(MyStatic.USER_ACCOUNT));
            intent.putExtra("photo", (String) dataList.get(position).get(MyStatic.USER_PORTRAIT));
            intent.putExtra("name", (String) dataList.get(position).get(MyStatic.USER_NAME));
            intent.putExtra("sex", (int) dataList.get(position).get(MyStatic.USER_SEX));
            startActivity(intent);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            simpleAdapter.notifyDataSetChanged();
            listView.setAdapter(simpleAdapter);
        }
    };

    private void LifeFriendsBroadcas() {
        //动态方式注册广播接收者
        this.receiver = new LifeFriendsBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyStatic.LIFE_LIFE_FRIENDS);
        this.registerReceiver(receiver, filter);
    }

    public class LifeFriendsBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MyStatic.LIFE_LIFE_FRIENDS)) {
                // TODO 将获取的数据赋值到本地
                if (intent.getBooleanExtra("reResult", true)){
                    String account = intent.getStringExtra("");
                    Bitmap portrait = intent.getParcelableExtra("");
                    String name = intent.getStringExtra("");
                    int sex = intent.getIntExtra("", 0);
                    setData(account, portrait, name, sex);
                }else{
                    System.out.println("### 哎哟我去");
                    Message message = new Message();
                    handler.sendMessage(message);
                }
            }
        }
    }

}