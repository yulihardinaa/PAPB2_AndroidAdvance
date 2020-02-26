package sv.ugm.komsi.broadcastreceiverb;

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
import java.time.LocalDate;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget1 extends AppWidgetProvider {
    private static final String SHARED_PREF_FILE=
            "sv.ugm.komsi.broadcastrecieverb.PREF";
    private static final String COUNT_KEY="count";


    @RequiresApi(api = Build.VERSION_CODES.O)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        SharedPreferences prefs=
                context.getSharedPreferences(SHARED_PREF_FILE,0);
        int count=prefs.getInt(COUNT_KEY+appWidgetId,0);
        count++;
        String timeString= DateFormat.getTimeInstance(DateFormat.LONG).format(new Date());
        String dateString= DateFormat.getDateInstance(DateFormat.LONG).format(new Date());
        String day = LocalDate.now().getDayOfWeek().name();
//        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget1);
        views.setTextViewText(R.id.appwidget_id, appWidgetId+"");
        views.setTextViewText(R.id.appwidget_update,count+"@"+timeString+"-"+dateString);
        views.setTextViewText(R.id.appwidget_date,day+","+dateString);

        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt(COUNT_KEY + appWidgetId,count);
        prefEditor.apply();
        Intent intentUpdate = new Intent(context, AppWidget1.class);
        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] idArray = new int[]{appWidgetId};
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,idArray);

        PendingIntent pendingUpdate =PendingIntent.getBroadcast(context,
                appWidgetId,intentUpdate, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,0);

        views.setOnClickPendingIntent(R.id.button_home,pendingIntent);

        views.setOnClickPendingIntent(R.id.button_update, pendingUpdate);
        // Instruct the widget manager to update the widget

        //dynamic calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.MINUTE,calendar.get(Calendar.MINUTE)+1);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC,
                calendar.getTimeInMillis(),
                60*1000,pendingUpdate);


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

