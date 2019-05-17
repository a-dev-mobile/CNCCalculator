package a.dev.mobile_cnc;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

public class RateDialog {
    public static void show(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.title_rate)
                .setMessage(R.string.dialog_Message_rate)
                .setPositiveButton(R.string.dialog_positive_rate, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
                        } catch (ActivityNotFoundException e) {
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName())));
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_negative_rate, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .create().show();
    }
}
