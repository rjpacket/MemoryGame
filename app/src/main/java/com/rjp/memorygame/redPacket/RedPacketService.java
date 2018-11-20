package com.rjp.memorygame.redPacket;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.rjp.memorygame.App;
import com.rjp.memorygame.SPUtil;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RedPacketService extends AccessibilityService {

    public static final int RED_PACKET_PRICE = 5;

    boolean hasRedPacket = false;
    private String pageClassName;
    private boolean canAddFriend = true;
    private KeyguardManager.KeyguardLock kl;
    private Handler handler = new Handler();

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        startForeground(10, getNotification());
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        stopForeground(true);
//    }
//
//    private Notification getNotification(){
//        Notification.Builder mBuilder = new Notification.Builder(this);
//        mBuilder.setShowWhen(false);
//        mBuilder.setAutoCancel(false);
//        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mBuilder.setLargeIcon(((BitmapDrawable)getDrawable(R.mipmap.ic_launcher)).getBitmap());
//        }
//        mBuilder.setContentText("this is content");
//        mBuilder.setContentTitle("this is title");
//        return mBuilder.build();
//    }

    /**
     * 必须重写的方法，响应各种事件。
     *
     * @param event
     */
    @Override
    public void onAccessibilityEvent(final AccessibilityEvent event) {
        boolean isOpen = (Boolean) SPUtil.getData(App.getInstance().getApplicationContext(), "auto-get-red-packet", false);
        if(!isOpen){
            return;
        }

        int eventType = event.getEventType();
        String className = event.getClassName().toString();
        Log.d("rjp------->", "get event = " + eventType);
        switch (eventType) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:// 通知栏事件
                Log.d("rjp------->", "get notification event");
                List<CharSequence> texts = event.getText();
                if (!texts.isEmpty()) {
                    for (CharSequence text : texts) {
                        String content = text.toString();
                        if (!TextUtils.isEmpty(content)) {
                            openNotifacation(event);
                        }
                    }
                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                Log.d("rjp------->", "window内容变化 | className = " + className);

                if ("com.tencent.mm.ui.LauncherUI".equals(pageClassName)) {
                    clickRedPacket(getRootInActiveWindow());
                }

                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:  // 打开一个新的页面
                pageClassName = event.getClassName().toString();
                Log.d("rjp------->", "window切换页面 | className = " + pageClassName);

                if (className.equals("com.tencent.mm.ui.LauncherUI")) {
                    if (hasRedPacket) {
                        AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
                        clickRedPacket(rootInActiveWindow);
                    }
                    hasRedPacket = false;
                } else if (className.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI")) {
                    AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
                    if(rootInActiveWindow == null){
                        Log.d("rjp------->", "》开《按钮没有找到");
                        return;
                    }
                    openLuckyMoney(rootInActiveWindow);

//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                        List<AccessibilityWindowInfo> windows = getWindows();
//                        for (AccessibilityWindowInfo window : windows) {
//                            openLuckyMoney(window.getRoot());
//                        }
//                    }


                } else if (className.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI")) {

                }
                break;
        }
    }

    private void wakeAndUnlock() {
        //获取电源管理器对象
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        //获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");

        //点亮屏幕
        wl.acquire(1000);

        //得到键盘锁管理器对象
        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        kl = km.newKeyguardLock("unLock");

        //解锁
        kl.disableKeyguard();

    }

    /**
     * 回到系统桌面
     */
    private void back2Home() {
        Intent home = new Intent(Intent.ACTION_MAIN);

        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        home.addCategory(Intent.CATEGORY_HOME);

        startActivity(home);
    }

    private void send() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("发送");
            if (list != null && list.size() > 0) {
                for (AccessibilityNodeInfo n : list) {
                    if (n.getClassName().equals("android.widget.Button") && n.isEnabled()) {
                        n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }

            } else {
                List<AccessibilityNodeInfo> liste = nodeInfo.findAccessibilityNodeInfosByText("Send");
                if (liste != null && liste.size() > 0) {
                    for (AccessibilityNodeInfo n : liste) {
                        if (n.getClassName().equals("android.widget.Button") && n.isEnabled()) {
                            n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }
                }
            }
        }
    }

    /**
     * 循环遍历找到EditText 并粘贴内容
     * @param rootNode
     * @param content
     * @return
     */
    private boolean pasteMessageInEditText(AccessibilityNodeInfo rootNode, String content) {
        int count = rootNode.getChildCount();
        for (int i = 0; i < count; i++) {
            AccessibilityNodeInfo nodeInfo = rootNode.getChild(i);
            if (nodeInfo == null) {
                continue;
            }

            if ("android.widget.EditText".equals(nodeInfo.getClassName())) {
                Bundle arguments = new Bundle();
                arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT, AccessibilityNodeInfo.MOVEMENT_GRANULARITY_WORD);
                arguments.putBoolean(AccessibilityNodeInfo.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN, true);
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY, arguments);
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                ClipData clip = ClipData.newPlainText("label", content);
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(clip);
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE);
                return true;
            }

            if (pasteMessageInEditText(nodeInfo, content)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 模拟back按键
     */
    private void pressBackButton() {
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给EditText节点粘贴上内容
     * @param nodeInfo
     * @param content
     */
    private void pasteContentToEditText(AccessibilityNodeInfo nodeInfo, String content){
        if(nodeInfo == null){
            return;
        }
        ClipData clip = ClipData.newPlainText("label", content);
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(clip);
        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE);
    }

    /**
     * 点击红包详情页面左上角返回
     *
     * @param rootInActiveWindow
     */
    private void clickLuckyMoneyDetailBack(AccessibilityNodeInfo rootInActiveWindow) {
        List<AccessibilityNodeInfo> nodeInfos = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ck_");
        if (nodeInfos != null && nodeInfos.size() > 0) {
            AccessibilityNodeInfo nodeInfo = nodeInfos.get(0);
            CharSequence text = nodeInfo.getText();
        }
        List<AccessibilityNodeInfo> nodes = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/jb");
        if (nodes != null && nodes.size() > 0) {
            AccessibilityNodeInfo nodeInfo = nodes.get(0);
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    /**
     * 打开红包
     */
    private boolean openLuckyMoney(AccessibilityNodeInfo rootNode) {
        try {
            List<AccessibilityNodeInfo> nodes = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/cnu");
            if (nodes != null && nodes.size() > 0) {
                AccessibilityNodeInfo nodeInfo = nodes.get(0);
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                return true;
            }
        }catch (Exception e){

        }
        return false;
    }

    /**
     * 寻找窗体中红包，并点击
     */
    @SuppressLint("NewApi")
    private void clickRedPacket(AccessibilityNodeInfo rootNode) {
        if (rootNode != null) {
            int count = rootNode.getChildCount();
            for (int i = count - 1; i >= 0; i--) {  // 倒序查找最新的红包
                AccessibilityNodeInfo node = rootNode.getChild(i);
                if (node == null)
                    continue;

                CharSequence text = node.getText();
                if (text != null && text.toString().equals("领取红包")) {
                    AccessibilityNodeInfo parent = node.getParent();
                    while (parent != null) {
                        if (parent.isClickable()) {
                            parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            Log.d("rjp------->", "点击了红包控件");
                            break;
                        }
                        parent = parent.getParent();
                    }
                }

                clickRedPacket(node);
            }
        }
    }

    /**
     * 点击 对方的消息通知popup
     *
     * @param event
     */
    private void openNotifacation(AccessibilityEvent event) {
        if (event.getParcelableData() != null && event.getParcelableData() instanceof Notification) {
            Notification notification = (Notification) event.getParcelableData();
            String content = notification.tickerText.toString();

            Pattern compile = Pattern.compile("\\[微信红包\\].*");
            Matcher matcher = compile.matcher(content);

            if (matcher.find()) {
                Log.d("rjp------->", "有一个红包通知过来了");
                hasRedPacket = true;
                PendingIntent pendingIntent = notification.contentIntent;
                try {
                    pendingIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onInterrupt() {

    }

    /**
     * 通过文字找到点击事件
     * @param text
     */
    private void clickViewByText(String text) {
        AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
        List<AccessibilityNodeInfo> nodeInfos = rootInActiveWindow.findAccessibilityNodeInfosByText(text);
        if(nodeInfos != null && nodeInfos.size() > 0){
            AccessibilityNodeInfo nodeInfo = nodeInfos.get(0);
            while (nodeInfo != null){
                if(nodeInfo.isClickable()){
                    nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    break;
                }else{
                    nodeInfo = nodeInfo.getParent();
                }
            }
        }
    }

    private void clickAreaById(String id) {
        try {
            AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
            List<AccessibilityNodeInfo> nodeInfos = rootInActiveWindow.findAccessibilityNodeInfosByViewId(id);
            if (nodeInfos != null && nodeInfos.size() > 0) {
                AccessibilityNodeInfo nodeInfo = nodeInfos.get(0);
                while (nodeInfo != null) {
                    if (nodeInfo.isClickable()) {
                        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        break;
                    } else {
                        nodeInfo = nodeInfo.getParent();
                    }
                }
            }
        }catch (Exception e){

        }
    }

    private void clickViewById(String id){
        AccessibilityNodeInfo nodeById = getNodeById(id);
        if(nodeById != null){
            nodeById.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    /**
     * 获取一个节点
     * @param id
     * @return
     */
    private AccessibilityNodeInfo getNodeById(String id){
        AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
        List<AccessibilityNodeInfo> nodeInfos = rootInActiveWindow.findAccessibilityNodeInfosByViewId(id);
        if(nodeInfos != null && nodeInfos.size() > 0){
            return nodeInfos.get(0);
        }
        return null;
    }
}