package sv.ugm.komsi.broadcastreceiverb;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class DateWidgetB extends AppWidgetProvider {
    private static final String SHARED_PREF_FILE=
            "sv.ugm.komsi.broadcastrecieverb.PREF";
    private static final String COUNT_KEY="count";

    @RequiresApi(api = Build.VERSION_CODES.O)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        @SuppressLint("StringFormatMatches")
        SharedPreferences prefs=
                context.getSharedPreferences(SHARED_PREF_FILE,0);
        int count=prefs.getInt(COUNT_KEY+appWidgetId,0);
        count++;
        String timePattern = "HH:mm";
        String datePattern = "EEE,dd MMMM yyyy";

        SimpleDateFormat timeFormat = new SimpleDateFormat(timePattern);
        String timeString = timeFormat.format(new Date());
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
        String dateString = dateFormat.format(new Date());

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.date_widget_b);
        views.setTextViewText(R.id.appwidget_date1, dateString);
        views.setTextViewText(R.id.appwidget_time1, timeString);



        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt(COUNT_KEY + appWidgetId,count);
        prefEditor.apply();
        Intent intentUpdate = new Intent(context, AppWidget1.class);
        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] idArray = new int[]{appWidgetId};
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,idArray);

        PendingIntent pendingUpdate =PendingIntent.getBroadcast(context,
                appWidgetId,intentUpdate, PendingIntent.FLAG_UPDATE_CURRENT);


        views.setOnClickPendingIntent(R.id.button_update, pendingUpdate);

        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(System.currentTimeMillis());
        time.set(Calendar.MINUTE,time.get(Calendar.MINUTE)+1);
        time.set(Calendar.SECOND,0);
        time.set(Calendar.MILLISECOND,0);

        AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC,
                time.getTimeInMillis(),
                60*1000,pendingUpdate);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}