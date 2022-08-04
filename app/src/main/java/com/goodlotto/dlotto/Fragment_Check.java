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
        //자바 스크립트 사용을 할 수 있도록 합니다
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            // 페이지 로딩이 끝나면 호출됩니다
            @Override
            public void onPageFinished(WebView view, String url) {
                Toast.makeText(((MainActivity) MainActivity.mContext), "로딩 끝", Toast.LENGTH_SHORT).show();
            }
        });

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // mButton의 onClick을 실행
                    mButton.callOnClick();
                    // 키보드 숨기기
                    InputMethodManager imm = (InputMethodManager) ((MainActivity)MainActivity.mContext).getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        mIntegrator = new IntentIntegrator(((MainActivity)MainActivity.mContext)); //바코드 안의 텍스트

        mIntegrator.setPrompt("바코드를 사각형 안에 비춰주세요");

        // 바코드 인식시 소리 여부
        mIntegrator.setBeepEnabled(false);
        mIntegrator.setBarcodeImageEnabled(true);
        mIntegrator.setCaptureActivity(CaptureActivity.class);

        // 바코드 스캐너 시작
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
                // qr코드를 읽어서 EditText에 입력해줍니다
                mEditText.setText(result.getContents());
                // Button의 onclick 호출
                mButton.callOnClick();
                Toast.makeText(((MainActivity)MainActivity.mContext), "Scanned: " + result.getContents(), Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}