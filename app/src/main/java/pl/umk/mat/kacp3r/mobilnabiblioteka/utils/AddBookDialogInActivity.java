package pl.umk.mat.kacp3r.mobilnabiblioteka.utils;

import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import pl.umk.mat.kacp3r.mobilnabiblioteka.R;
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.book.AboutBookActivity;

public class AddBookDialogInActivity
{
    public void showDialog(final AboutBookActivity aboutBookActivity,
                           String msg,
                           final String googleBookId)
    {

        final Dialog dialog = new Dialog(aboutBookActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_book_to_library_custom_alert);

        TextView dialogText = (TextView) dialog.findViewById(R.id.text_dialog);
        dialogText.setText(msg);

        Button toReadAddButton = (Button) dialog.findViewById(R.id.to_read_add_button);
        toReadAddButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                aboutBookActivity.bookGoogleIdRequestWithRetrofit(googleBookId, true, 1);

                dialog.dismiss();
            }
        });

        Button progressAddButton = (Button) dialog.findViewById(R.id.progress_add_button);
        progressAddButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                aboutBookActivity.bookGoogleIdRequestWithRetrofit(googleBookId, true, 2);

                dialog.dismiss();
            }
        });

        Button finishAddButton = (Button) dialog.findViewById(R.id.finish_add_button);
        finishAddButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                aboutBookActivity.bookGoogleIdRequestWithRetrofit(googleBookId, true, 3);

                dialog.dismiss();
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
