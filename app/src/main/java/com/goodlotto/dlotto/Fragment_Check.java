package com.goodlotto.dlotto;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Check#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Check extends Fragment {
    private View mView;
    private WebView mWebView;
    private EditText mEditText;
    private Button mButton;
    private Button mQRCode;
    private IntentIntegrator mIntegrator;

    public Fragment_Check() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment__check, container, false);

        mEditText = mView.findViewById(R.id.et);
        mWebView = mView.findViewById(R.id.wv);
        mButton = mView.findViewById(R.id.bt);
        mButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = mEditText.getText().toString();
                if (!address.startsWith("http://")) {
                    address = "http://" + address;
                }

                mWebView.loadUrl(address);
            }
        }) ;

        mQRCode = mView.findViewById(R.id.btnQRCode);
        mQRCode.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScanQRCode();
            }
        }) ;

        return mView;
    }

    public void startScanQRCode() {
        WebSettings webSettings = mWebView.getSettings();
        //?????? ???????????? ????????? ??? ??? ????????? ?????????
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            // ????????? ????????? ????????? ???????????????
            @Override
            public void onPageFinished(WebView view, String url) {
                Toast.makeText(((MainActivity) MainActivity.mContext), "?????? ???", Toast.LENGTH_SHORT).show();
            }
        });

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // mButton??? onClick??? ??????
                    mButton.callOnClick();
                    // ????????? ?????????
                    InputMethodManager imm = (InputMethodManager) ((MainActivity)MainActivity.mContext).getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        mIntegrator = new IntentIntegrator(((MainActivity)MainActivity.mContext)); //????????? ?????? ?????????

        mIntegrator.setPrompt("???????????? ????????? ?????? ???????????????");

        // ????????? ????????? ?????? ??????
        mIntegrator.setBeepEnabled(false);
        mIntegrator.setBarcodeImageEnabled(true);
        mIntegrator.setCaptureActivity(CaptureActivity.class);

        // ????????? ????????? ??????
        mIntegrator.initiateScan();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {

            } else {
                // qr????????? ????????? EditText??? ??????????????????
                mEditText.setText(result.getContents());
                // Button??? onclick ??????
                mButton.callOnClick();
                Toast.makeText(((MainActivity)MainActivity.mContext), "Scanned: " + result.getContents(), Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}