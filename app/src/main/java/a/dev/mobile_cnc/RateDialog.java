package a.dev.mobile_cnc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

public class RateDialog {
    public static void show(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Оценить ЧПУ калькулятор v2")
                .setMessage("Вы желаете оценить приложение в магазине Google Play?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
                        } catch (android.content.ActivityNotFoundException e) {
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName())));
                        }
                    }
                })
                .setNegativeButton("Нет, спасибо", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .create().show();
    }
}
