// Copyright 2019 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package dev.fluttercommunity.plus.androidalarmmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.content.pm.PackageManager;
import android.view.WindowManager;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
  private static PowerManager.WakeLock wakeLock;

  @Override
  public void onReceive(Context context, Intent intent) {
    PowerManager powerManager = (PowerManager)
            context.getSystemService(Context.POWER_SERVICE);
    wakeLock = powerManager.newWakeLock(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
            PowerManager.ACQUIRE_CAUSES_WAKEUP |
            PowerManager.ON_AFTER_RELEASE, "My wakelock");

    Intent startIntent = context
            .getPackageManager()
            .getLaunchIntentForPackage(context.getPackageName());

    startIntent.setFlags(
            Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
    );

    startIntent.putExtra("fromAlarm","true");

    wakeLock.acquire();
    context.startActivity(startIntent);
    AlarmService.enqueueAlarmProcessing(context, intent);
    wakeLock.release();
  }
}
