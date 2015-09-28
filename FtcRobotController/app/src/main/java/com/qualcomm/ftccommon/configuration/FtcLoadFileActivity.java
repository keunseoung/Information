package com.qualcomm.ftccommon.configuration;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.ftccommon.R;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class FtcLoadFileActivity extends Activity {
   public static final String CONFIGURE_FILENAME = "CONFIGURE_FILENAME";
   OnClickListener a = new OnClickListener() {
      public void onClick(DialogInterface var1, int var2) {
      }
   };
   private ArrayList<String> b = new ArrayList();
   private Context context;
   private Utility utility;

   private String getFileName(View view, boolean addDotXml) {
      String var3 = ((TextView)((LinearLayout)((LinearLayout)view.getParent()).getParent()).findViewById(R.id.filename_editText)).getText().toString();
      if(addDotXml) {
         var3 = var3 + ".xml";
      }

      return var3;
   }

   private void a() {
      ((Button)this.findViewById(R.id.files_holder).findViewById(R.id.info_btn)).setOnClickListener(new android.view.View.OnClickListener() {
         public void onClick(View var1) {
            Builder var2 = FtcLoadFileActivity.this.utility.buildBuilder("Available files", "These are the files the Hardware Wizard was able to find. You can edit each file by clicking the edit button. The \'Activate\' button will set that file as the current configuration file, which will be used to start the robot.");
            var2.setPositiveButton("Ok", FtcLoadFileActivity.this.a);
            AlertDialog var4 = var2.create();
            var4.show();
            ((TextView)var4.findViewById(16908299)).setTextSize(14.0F);
         }
      });
      ((Button)this.findViewById(R.id.autoconfigure_holder).findViewById(R.id.info_btn)).setOnClickListener(new android.view.View.OnClickListener() {
         public void onClick(View var1) {
            Builder var2 = FtcLoadFileActivity.this.utility.buildBuilder("AutoConfigure", "This is the fastest way to get a new machine up and running. The AutoConfigure tool will automatically enter some default names for devices. AutoConfigure expects certain devices.  If there are other devices attached, the AutoConfigure tool will not name them.");
            var2.setPositiveButton("Ok", FtcLoadFileActivity.this.a);
            AlertDialog var4 = var2.create();
            var4.show();
            ((TextView)var4.findViewById(16908299)).setTextSize(14.0F);
         }
      });
   }

   private void b() {
      if(this.b.size() == 0) {
         this.utility.setOrangeText("No files found!", "In order to proceed, you must create a new file", R.id.empty_filelist, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
      } else {
         ViewGroup var1 = (ViewGroup)this.findViewById(R.id.empty_filelist);
         var1.removeAllViews();
         var1.setVisibility(8);
      }
   }

   private void c() {
      ViewGroup var1 = (ViewGroup)this.findViewById(R.id.inclusionlayout);
      var1.removeAllViews();
      Iterator var2 = this.b.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         View var4 = LayoutInflater.from(this).inflate(R.layout.file_info, (ViewGroup)null);
         var1.addView(var4);
         ((TextView)var4.findViewById(R.id.filename_editText)).setText(var3);
      }

   }

   public void file_activate_button(View view) {
      String fileNameNoDotXml = this.getFileName(view, false);
      this.utility.saveToPreferences(fileNameNoDotXml, R.string.pref_hardware_config_filename);
      this.utility.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
   }

   public void file_delete_button(View var1) {
      String var2 = this.getFileName(var1, true);
      File var3 = new File(Utility.CONFIG_FILES_DIR + var2);
      if(var3.exists()) {
         var3.delete();
      } else {
         this.utility.complainToast("That file does not exist: " + var2, this.context);
         DbgLog.error("Tried to delete a file that does not exist: " + var2);
      }

      this.b = this.utility.getXMLFiles();
      this.utility.saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
      this.utility.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
      this.c();
   }

   public void file_edit_button(View var1) {
      String var2 = this.getFileName(var1, true);
      this.utility.saveToPreferences(var2, R.string.pref_hardware_config_filename);
      this.startActivity(new Intent(this.context, FtcConfigurationActivity.class));
   }

   public void launch_autoConfigure(View var1) {
      this.startActivity(new Intent(this.getBaseContext(), AutoConfigureActivity.class));
   }

   public void new_button(View var1) {
      this.utility.saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
      this.startActivity(new Intent(this.context, FtcConfigurationActivity.class));
   }

   public void onBackPressed() {
      String selectedConfigurationFile = this.utility.getFilenameFromPrefs(R.string.pref_hardware_config_filename, "No current file!");
      Intent intent = new Intent();
      intent.putExtra("CONFIGURE_FILENAME", selectedConfigurationFile);
      this.setResult(-1, intent);
      this.finish();
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(R.layout.activity_load);
      this.context = this;
      this.utility = new Utility(this);
      this.utility.createConfigFolder();
      this.a();
   }

   protected void onStart() {
      super.onStart();
      this.b = this.utility.getXMLFiles();
      this.b();
      this.utility.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
      this.c();
   }
}
