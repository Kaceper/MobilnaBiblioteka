package pl.umk.mat.kacp3r.mobilnabiblioteka.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.umk.mat.kacp3r.mobilnabiblioteka.R;

public class MainActivity extends AppCompatActivity
{
    @BindView(R.id.scan_button) Button scanButton;

    @OnClick(R.id.scan_button)
    public void onScanButtonClick()
    {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        ButterKnife.setDebug(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null)
        {
            if (result.getContents() == null)
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
        }
        else
            super.onActivityResult(requestCode, resultCode, data);
    }
}
