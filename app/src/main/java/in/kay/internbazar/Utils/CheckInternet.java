package in.kay.internbazar.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;

import com.androidstudy.networkmanager.Monitor;
import com.androidstudy.networkmanager.Tovuti;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.sdsmdg.tastytoast.TastyToast;

import in.kay.internbazar.R;

public class CheckInternet {
    public void Check(final Context context) {
        Tovuti.from(context).monitor(new Monitor.ConnectivityListener(){
            @Override
            public void onConnectivityChanged(int connectionType, boolean isConnected, boolean isFast){
                final ProgressDialog pd = new ProgressDialog(context);
                pd.setMax(100);
                pd.setMessage("Checking internet connection...");
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                if (!isConnected)
                {
                    ShowDiag(context);
                }
            }
        });
    }

    private void ShowDiag(final Context context) {
        new iOSDialogBuilder(context)
                .setTitle("Oh shucks!")
                .setSubtitle("Slow or no internet connection.\nPlease check your internet settings")
                .setCancelable(false)
                .setPositiveListener(context.getString(R.string.retry), new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        Check(context);
                        dialog.dismiss();
                    }
                })
                .build().show();
    }
}
