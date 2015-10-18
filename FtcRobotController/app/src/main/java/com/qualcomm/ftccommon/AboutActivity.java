package com.qualcomm.ftccommon;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.Version;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant;

public class AboutActivity extends Activity {
   WifiDirectAssistant wifiDirectAssistant = null;

   protected void onStart() {
      super.onStart();
      this.setContentView(R.layout.activity_about);
      ListView var1 = (ListView)this.findViewById(R.id.aboutList);

      try {
         this.wifiDirectAssistant = WifiDirectAssistant.getWifiDirectAssistant((Context)null);
         this.wifiDirectAssistant.enable();
      } catch (NullPointerException var3) {
         RobotLog.i("Cannot start Wifi Direct Assistant");
         this.wifiDirectAssistant = null;
      }

      var1.setAdapter(new ArrayAdapter(this, 17367044, 16908308) {
         private AboutActivity.Item a() {
            AboutActivity.Item var1 = new AboutActivity.Item();
            var1.title = "App Version";

            try {
               var1.info = AboutActivity.this.getPackageManager().getPackageInfo(AboutActivity.this.getPackageName(), 0).versionName;
               return var1;
            } catch (NameNotFoundException var3) {
               var1.info = var3.getMessage();
               return var1;
            }
         }

         private AboutActivity.Item b() {
            AboutActivity.Item var1 = new AboutActivity.Item();
            var1.title = "Library Version";
            var1.info = Version.getLibraryVersion();
            return var1;
         }

         private AboutActivity.Item c() {
            AboutActivity.Item var1 = new AboutActivity.Item();
            var1.title = "Wifi Direct Information";
            var1.info = "unavailable";
            StringBuilder var2 = new StringBuilder();
            if(AboutActivity.this.wifiDirectAssistant != null && AboutActivity.this.wifiDirectAssistant.isEnabled()) {
               var2.append("Name: ").append(AboutActivity.this.wifiDirectAssistant.getDeviceName());
               if(AboutActivity.this.wifiDirectAssistant.isGroupOwner()) {
                  var2.append("\nIP Address").append(AboutActivity.this.wifiDirectAssistant.getGroupOwnerAddress().getHostAddress());
                  var2.append("\nPassphrase: ").append(AboutActivity.this.wifiDirectAssistant.getPassphrase());
                  var2.append("\nGroup Owner");
               } else if(AboutActivity.this.wifiDirectAssistant.isConnected()) {
                  var2.append("\nGroup Owner: ").append(AboutActivity.this.wifiDirectAssistant.getGroupOwnerName());
                  var2.append("\nConnected");
               } else {
                  var2.append("\nNo connection information");
               }

               var1.info = var2.toString();
            }

            return var1;
         }

         public AboutActivity.Item a(int var1) {
            switch(var1) {
            case 0:
               return this.a();
            case 1:
               return this.b();
            case 2:
               return this.c();
            default:
               return new AboutActivity.Item();
            }
         }

         public int getCount() {
            return 3;
         }

         // $FF: synthetic method
         public Object getItem(int var1) {
            return this.a(var1);
         }

         public View getView(int var1, View var2, ViewGroup var3) {
            View var4 = super.getView(var1, var2, var3);
            TextView var5 = (TextView)var4.findViewById(16908308);
            TextView var6 = (TextView)var4.findViewById(16908309);
            AboutActivity.Item var7 = this.a(var1);
            var5.setText(var7.title);
            var6.setText(var7.info);
            return var4;
         }
      });
   }

   protected void onStop() {
      super.onStop();
      if(this.wifiDirectAssistant != null) {
         this.wifiDirectAssistant.disable();
      }

   }

   public static class Item {
      public String info = "";
      public String title = "";
   }
}
