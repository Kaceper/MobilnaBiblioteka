package pl.umk.mat.kacp3r.mobilnabiblioteka.utils;

import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import pl.umk.mat.kacp3r.mobilnabiblioteka.R;
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.book.AboutBookActivity;

public class MarkAsReadDialog
{
    public void showDialog(final AboutBookActivity aboutBookActivity,
                           String msg,
                           final String googleBookId)
    {

        final Dialog dialog = new Dialog(aboutBookActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.mark_as_read_custom_alert);

        TextView dialogText = (TextView) dialog.findViewById(R.id.text_dialog);
        dialogText.setText(msg);

        Button cancelButton = (Button) dialog.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                aboutBookActivity.moveBackSeekBar();
                dialog.dismiss();
            }
        });

        Button markAsReadButton = (Button) dialog.findViewById(R.id.mark_as_read_button);
        markAsReadButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                aboutBookActivity.moveBookToFinishedShelf(googleBookId);

                aboutBookActivity.makeToast("Książka została oznaczona jako przeczytana");
                dialog.dismiss();
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
