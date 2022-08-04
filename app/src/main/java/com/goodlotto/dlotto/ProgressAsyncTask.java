package com.goodlotto.dlotto;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

// AsyncTask class 정의 - 인터넷과 같은 기다리는 Task가 필요하면 필요하다...
public class ProgressAsyncTask extends AsyncTask<Void, Void, Void> {
    private ProgressDialog mProgressDialog;
    public Document mDoc;
    public String mRound;
    public String mDrawDate;
    public String[] mNumber = new String[6];
    public String mBnumber;
    public Boolean mParseFlag = false;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        mProgressDialog = new ProgressDialog(MainActivity.mContext);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("결과를 가져오고 있습니다.\r\n잠시만 기다려주세요.");
        mProgressDialog.show();
    }

    private final static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36";

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Connection conn = Jsoup.connect("https://dhlottery.co.kr/gameResult.do?method=byWin").header("Content-Type", "text/html;charset=UTF-8").userAgent(USER_AGENT).method(Connection.Method.GET).ignoreContentType(true);
            mDoc = conn.timeout(5000).get();
            //mDoc = Jsoup.connect("http://google.com").post();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        mParseFlag = false;

        /* 결과를 파싱하는 부분 */
        /*
        <div class="win_result">
         <h4><strong>940회</strong> 당첨결과</h4>
         <p class="desc">(2020년 12월 05일 추첨)</p>
         <div class="nums">
          <div class="num win"> <strong>당첨번호</strong>
           <p> <span class="ball_645 lrg ball1">3</span> <span class="ball_645 lrg ball2">15</span> <span class="ball_645 lrg ball2">20</span> <span class="ball_645 lrg ball3">22</span> <span class="ball_645 lrg ball3">24</span> <span class="ball_645 lrg ball5">41</span> </p>
          </div>
          <div class="num bonus"> <strong>보너스</strong>
           <p><span class="ball_645 lrg ball2">11</span></p>
          </div>
         </div>
        </div>
         */
        if (mDoc != null) {
            Elements win_result = mDoc.getElementsByClass("win_result"); // <div class="win_result">
            if (win_result.size() != 0) {
                Element source = win_result.get(0);
                Elements result_title = source.getElementsByTag("h4"); // <h4><strong>940회</strong> 당첨결과</h4>
                if (result_title.size() != 0) {
                    Element title = result_title.get(0);
                    Elements result_round = title.getElementsByTag("strong"); // <strong>940회</strong>
                    if (result_round.size() != 0) {
                        Element round = result_round.get(0);
                        mRound = round.ownText(); // 940회

                        Elements drawDates = source.getElementsByClass("desc"); // <p class="desc">(2020년 12월 05일 추첨)</p>
                        if (drawDates.size() != 0) {
                            Element drawDate = drawDates.get(0);
                            mDrawDate = drawDate.ownText(); // (2020년 12월 05일 추첨)

                            Elements result_numbers = source.getElementsByClass("num win"); // <div class="num win"> <strong>당첨번호</strong><p> <span class="ball_645 lrg ball1">3</span> ... </div>
                            if (result_numbers.size() != 0) {
                                Element numbers = result_numbers.get(0);
                                Elements num6s = numbers.getElementsByTag("span"); // <span class="ball_645 lrg ball1">3</span> ...
                                if (num6s.size() == 6) {
                                    int i;
                                    for (i = 0; i < 6; i++) {
                                        Element num6 = num6s.get(i);
                                        mNumber[i] = num6.ownText(); // 3 ...
                                    }

                                    Elements result_bnumber = source.getElementsByClass("num bonus"); // <div class="num bonus"> <strong>보너스</strong> <p><span class="ball_645 lrg ball2">11</span></p></div>
                                    if (result_bnumber.size() != 0) {
                                        Element bnumber = result_bnumber.get(0);
                                        Elements bnums = bnumber.getElementsByTag("span"); // <span class="ball_645 lrg ball2">11</span>
                                        if (bnums.size() != 0) {
                                            Element bnum = bnums.get(0);
                                            mBnumber = bnum.ownText(); // 11

                                            mParseFlag = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //
        if (mDoc != null) {
            ((MainActivity) MainActivity.mContext).SetRound(mRound);
            ((MainActivity) MainActivity.mContext).SetDrawDate(mDrawDate);
            ((MainActivity) MainActivity.mContext).SetBalls(mNumber, mBnumber);
        }

        mProgressDialog.dismiss();
    }
}
