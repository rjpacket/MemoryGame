package com.rjp.memorygame;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RedPacketService extends AccessibilityService {
    private final static String MM_PNAME = "com.tencent.mm";
    boolean hasRedPacket = false;
    boolean locked = false;
    boolean background = false;
    private String name;
    private String scontent;
    private List<AccessibilityNodeInfo> itemNodeinfos = new ArrayList<>();
    private KeyguardManager.KeyguardLock kl;
    private Handler handler = new Handler();


    /**
     * 必须重写的方法，响应各种事件。
     *
     * @param event
     */
    @Override
    public void onAccessibilityEvent(final AccessibilityEvent event) {
        int eventType = event.getEventType();
        Log.d("maptrix", "get event = " + eventType);
        switch (eventType) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:// 通知栏事件
                Log.d("maptrix", "get notification event");
                List<CharSequence> texts = event.getText();
                if (!texts.isEmpty()) {
                    for (CharSequence text : texts) {
                        String content = text.toString();
                        if (!TextUtils.isEmpty(content)) {
                            if (isScreenLocked()) {
                                locked = true;
                                wakeAndUnlock();
                                Log.d("maptrix", "the screen is locked");
                                if (isAppForeground(MM_PNAME)) {
                                    background = false;
                                    Log.d("maptrix", "is mm in foreground");
                                    openReplyNotifacation(event);
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            openReplyNotifacation(event);
                                            if (findRedPacket()) {
                                            }
                                        }
                                    }, 1000);
                                } else {
                                    background = true;
                                    Log.d("maptrix", "is mm in background");
                                    openReplyNotifacation(event);
                                }
                            } else {
                                locked = false;
                                Log.d("maptrix", "the screen is unlocked");
                                // 监听到微信红包的notification，打开通知
                                if (isAppForeground(MM_PNAME)) {
                                    background = false;
                                    Log.d("maptrix", "is mm in foreground");
                                    openReplyNotifacation(event);
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (findRedPacket()) {
                                            }
                                        }
                                    }, 1000);
                                } else {
                                    background = true;
                                    Log.d("maptrix", "is mm in background");
                                    openReplyNotifacation(event);
                                }
                            }
                        }
                    }
                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
//                Log.d("rjp------->", "手机屏幕内容变化了");
//                openReplyNotifacation(event);
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:  // 打开一个新的页面
                Log.d("maptrix", "get type window down event");
                String className = event.getClassName().toString();
                Log.d("rjp------->", "className = " + className);

                if (className.equals("com.tencent.mm.ui.LauncherUI")) {
                    if(hasRedPacket) {
                        AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
                        clickRedPacket(rootInActiveWindow);
                    }
                }else if(className.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI")){
                    AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
                    openLuckyMoney(rootInActiveWindow);
                }

                //bring2Front();
//                back2Home();
                release();
                hasRedPacket = false;
                break;
        }
    }

    /**
     * 打开红包
     */
    private boolean openLuckyMoney(AccessibilityNodeInfo rootNode) {
        List<AccessibilityNodeInfo> nodes = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/c2i");
        if(nodes != null && nodes.size() > 0){
            AccessibilityNodeInfo nodeInfo = nodes.get(0);
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
        return false;
    }

    /**
     * 寻找窗体中红包，并点击
     */
    @SuppressLint("NewApi")
    private void clickRedPacket(AccessibilityNodeInfo rootNode) {
        if(rootNode != null) {
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
     * 点击 对方的消息通知popup
     *
     * @param event
     */
    private void openReplyNotifacation(AccessibilityEvent event) {
        hasRedPacket = true;
        if (event.getParcelableData() != null && event.getParcelableData() instanceof Notification) {
            Notification notification = (Notification) event.getParcelableData();
            String content = notification.tickerText.toString();

            Pattern compile = Pattern.compile("\\[微信红包\\].*");
            Matcher matcher = compile.matcher(content);
            if (matcher.find()) {
                Log.d("rjp------->", "有一个红包通知过来了");
                PendingIntent pendingIntent = notification.contentIntent;
                try {
                    pendingIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressLint("NewApi")
    private boolean findRedPacket() {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode != null) {
            return findCanOpenRedPacket(rootNode);
        }
        return false;
    }

    private boolean findCanOpenRedPacket(AccessibilityNodeInfo rootNode) {
        int count = rootNode.getChildCount();
        Log.d("rjp------->", "root class=" + rootNode.getClassName() + "," + rootNode.getText() + "," + count);
        for (int i = 0; i < count; i++) {
            AccessibilityNodeInfo nodeInfo = rootNode.getChild(i);
            if (nodeInfo == null) {
                Log.d("rjp------->", "nodeinfo = null");
                continue;
            }

            Log.d("rjp------->", "class=" + nodeInfo.getClassName());
            Log.e("rjp------->", "ds=" + nodeInfo.getContentDescription());
            if (nodeInfo.getContentDescription() != null) {
                int nindex = nodeInfo.getContentDescription().toString().indexOf("查看红包");
//                int cindex = nodeInfo.getContentDescription().toString().indexOf(scontent);
                Log.e("rjp------->", "nindex=" + nindex);
                if (nindex != -1) {
                    itemNodeinfos.add(nodeInfo);
                    Log.i("rjp-------->", "找到" + itemNodeinfos.size() + "个未开启的红包");
                }
            }

            findCanOpenRedPacket(nodeInfo);
        }
        return itemNodeinfos.size() > 0;
    }

    @Override
    public void onInterrupt() {

    }

    /**
     * 判断指定的应用是否在前台运行
     *
     * @param packageName
     * @return
     */
    private boolean isAppForeground(String packageName) {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(packageName)) {
            return true;
        }

        return false;
    }


    /**
     * 将当前应用运行到前台
     */
    private void bring2Front() {
        ActivityManager activtyManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activtyManager.getRunningTasks(3);
        for (ActivityManager.RunningTaskInfo runningTaskInfo : runningTaskInfos) {
            if (this.getPackageName().equals(runningTaskInfo.topActivity.getPackageName())) {
                activtyManager.moveTaskToFront(runningTaskInfo.id, ActivityManager.MOVE_TASK_WITH_HOME);
                return;
            }
        }
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


    /**
     * 系统是否在锁屏状态
     *
     * @return
     */
    private boolean isScreenLocked() {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        return keyguardManager.inKeyguardRestrictedInputMode();
    }

    private void wakeAndUnlock() {
        //获取电源管理器对象
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        //获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");

        //点亮屏幕
        wl.acquire(1000);

        //得到键盘锁管理器对象
        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        kl = km.newKeyguardLock("unLock");

        //解锁
        kl.disableKeyguard();

    }

    private void release() {

        if (locked && kl != null) {
            Log.d("maptrix", "release the lock");
            //得到键盘锁管理器对象
            kl.reenableKeyguard();
            locked = false;
        }
    }
}