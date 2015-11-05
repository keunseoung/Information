//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qualcomm.ftccommon;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import com.qualcomm.ftccommon.UpdateUI.Callback;
import com.qualcomm.robotcore.eventloop.EventLoop;
import com.qualcomm.robotcore.eventloop.EventLoopManager.EventLoopMonitor;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.factory.RobotFactory;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.robot.RobotState;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant.Event;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant.WifiDirectAssistantCallback;
import java.lang.Thread.State;
import java.net.InetAddress;

public class FtcRobotControllerService extends Service implements WifiDirectAssistantCallback {
    private final IBinder a = new FtcRobotControllerService.FtcRobotControllerBinder();
    private WifiDirectAssistant wifiDirectAssistant;
    private Robot robot;
    private EventLoop eventLoop;
    private Event event;
    private String f;
    private Callback callback;
    private final FtcRobotEventLoopMonitor eventLoopMonitor;
    private final ElapsedTime elapsed;
    private Thread j;

    public FtcRobotControllerService() {
        this.event = Event.DISCONNECTED;
        this.f = "Robot Status: null";
        this.callback = null;
        this.eventLoopMonitor = new FtcRobotEventLoopMonitor((FtcRobotControllerService.SyntheticClass_1)null);
        this.elapsed = new ElapsedTime();
        this.j = null;
    }

    public WifiDirectAssistant getWifiDirectAssistant() {
        return this.wifiDirectAssistant;
    }

    public Event getWifiDirectStatus() {
        return this.event;
    }

    public String getRobotStatus() {
        return this.f;
    }

    public IBinder onBind(Intent intent) {
        DbgLog.msg("Starting FTC Controller Service");
        DbgLog.msg("Android device is " + Build.MANUFACTURER + ", " + Build.MODEL);
        this.wifiDirectAssistant = WifiDirectAssistant.getWifiDirectAssistant(this);
        this.wifiDirectAssistant.setCallback(this);
        this.wifiDirectAssistant.enable();
        if(Build.MODEL.equals("FL7007")) {
            this.wifiDirectAssistant.discoverPeers();
        } else {
            this.wifiDirectAssistant.createGroup();
        }

        return this.a;
    }

    public boolean onUnbind(Intent intent) {
        DbgLog.msg("Stopping FTC Controller Service");
        this.wifiDirectAssistant.disable();
        this.shutdownRobot();
        return false;
    }

    public synchronized void setCallback(Callback callback) {
        this.callback = callback;
    }

    public synchronized void setupRobot(EventLoop eventLoop) {
        if(this.j != null && this.j.isAlive()) {
            DbgLog.msg("FtcRobotControllerService.setupRobot() is currently running, stopping old setup");
            this.j.interrupt();

            while(this.j.isAlive()) {
                Thread.yield();
            }

            DbgLog.msg("Old setup stopped; restarting setup");
        }

        RobotLog.clearGlobalErrorMsg();
        DbgLog.msg("Processing robot setup");
        this.eventLoop = eventLoop;
        this.j = new Thread(new FtcRobotControllerService.b((FtcRobotControllerService.SyntheticClass_1)null), "Robot Setup");
        this.j.start();

        while(this.j.getState() == State.NEW) {
            Thread.yield();
        }

    }

    public synchronized void shutdownRobot() {
        if(this.j != null && this.j.isAlive()) {
            this.j.interrupt();
        }

        if(this.robot != null) {
            this.robot.shutdown();
        }

        this.robot = null;
        this.a("Robot Status: null");
    }

    public void onWifiDirectEvent(Event event) {
        switch(FtcRobotControllerService.SyntheticClass_1.b[event.ordinal()]) {
        case 1:
            DbgLog.msg("Wifi Direct - Group Owner");
            this.wifiDirectAssistant.cancelDiscoverPeers();
            break;
        case 2:
            DbgLog.error("Wifi Direct - connected as peer, was expecting Group Owner");
            break;
        case 3:
            DbgLog.msg("Wifi Direct Passphrase: " + this.wifiDirectAssistant.getPassphrase());
            break;
        case 4:
            DbgLog.error("Wifi Direct Error: " + this.wifiDirectAssistant.getFailureReason());
        }

        this.a(event);
    }

    private void a(Event var1) {
        this.event = var1;
        if(this.callback != null) {
            this.callback.wifiDirectUpdate(this.event);
        }

    }

    private void a(String var1) {
        this.f = var1;
        if(this.callback != null) {
            this.callback.robotUpdate(var1);
        }

    }

    private class b implements Runnable {
        private b() {
        }

        public void run() {
            try {
                if(FtcRobotControllerService.this.robot != null) {
                    FtcRobotControllerService.this.robot.shutdown();
                    FtcRobotControllerService.this.robot = null;
                }

                FtcRobotControllerService.this.a("Robot Status: scanning for USB devices");

                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException var4) {
                    FtcRobotControllerService.this.a("Robot Status: abort due to interrupt");
                    return;
                }

                FtcRobotControllerService.this.robot = RobotFactory.createRobot();
                FtcRobotControllerService.this.a("Robot Status: waiting on network");
                FtcRobotControllerService.this.elapsed.reset();

                while(!FtcRobotControllerService.this.wifiDirectAssistant.isConnected()) {
                    try {
                        Thread.sleep(1000L);
                        if(FtcRobotControllerService.this.elapsed.time() > 120.0D) {
                            FtcRobotControllerService.this.a("Robot Status: network timed out");
                            return;
                        }
                    } catch (InterruptedException var3) {
                        DbgLog.msg("interrupt waiting for network; aborting setup");
                        return;
                    }
                }

                FtcRobotControllerService.this.a("Robot Status: starting robot");

                try {
                    FtcRobotControllerService.this.robot.eventLoopManager.setMonitor(FtcRobotControllerService.this.eventLoopMonitor);
                    InetAddress var1 = FtcRobotControllerService.this.wifiDirectAssistant.getGroupOwnerAddress();
                    FtcRobotControllerService.this.robot.start(var1, FtcRobotControllerService.this.eventLoop);
                } catch (RobotCoreException var2) {
                    FtcRobotControllerService.this.a("Robot Status: failed to start robot");
                    RobotLog.setGlobalErrorMsg(var2.getMessage());
                }
            } catch (RobotCoreException var5) {
                FtcRobotControllerService.this.a("Robot Status: Unable to create robot!");
                RobotLog.setGlobalErrorMsg(var5.getMessage());
            }

        }
    }

    private class FtcRobotEventLoopMonitor implements EventLoopMonitor {
        private FtcRobotEventLoopMonitor() {
        }

        public void onStateChange(RobotState state) {
            if(FtcRobotControllerService.this.callback != null) {
                switch(FtcRobotControllerService.SyntheticClass_1.a[state.ordinal()]) {
                case 1:
                    FtcRobotControllerService.this.callback.robotUpdate("Robot Status: init");
                    break;
                case 2:
                    FtcRobotControllerService.this.callback.robotUpdate("Robot Status: not started");
                    break;
                case 3:
                    FtcRobotControllerService.this.callback.robotUpdate("Robot Status: running");
                    break;
                case 4:
                    FtcRobotControllerService.this.callback.robotUpdate("Robot Status: stopped");
                    break;
                case 5:
                    FtcRobotControllerService.this.callback.robotUpdate("Robot Status: EMERGENCY STOP");
                    break;
                case 6:
                    FtcRobotControllerService.this.callback.robotUpdate("Robot Status: dropped connection");
                }

            }
        }
    }

    public class FtcRobotControllerBinder extends Binder {
        public FtcRobotControllerBinder() {
        }

        public FtcRobotControllerService getService() {
            return FtcRobotControllerService.this;
        }
    }
}
