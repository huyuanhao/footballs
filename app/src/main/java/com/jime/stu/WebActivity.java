package com.jime.stu;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jime.stu.utils.AndroidXunJS;


/***
 * 文件名：WebActivity
 * 描述：便民服务加载网页的界面
 * 作者：chenhao
 * 时间：2017/11/30
 * 版权：v1.0
 */
public class WebActivity extends AppCompatActivity implements OnClickListener {
    private WebView webView;
    private TextView tvTitle;
    private ProgressBar pb;
    private WebSettings mWebSettings;
    private RelativeLayout error;
    private RelativeLayout toolBarBack,toolBarFresh,toolBarOff;
    private String title;
    private Toolbar toorBar;

    //    String referer,
    public  static  void startTo(Context context, String url, String toTitle){
        Intent intent = new Intent(context,WebActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("title",toTitle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        String url = intent.getStringExtra("url");
//        initTitleBar(title, this, null);

        toorBar = findViewById(R.id.toolbar);
        toorBar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        webView = findViewById(R.id.webview);
        tvTitle = findViewById(R.id.tv_title);
        pb = findViewById(R.id.progressBar);
        error =  findViewById(R.id.error);
        //加载需要显示的网页
        webView.loadUrl(url);

        tvTitle.setText(title);
        setUpView();

    }

    private void setUpView() {
        webView.addJavascriptInterface(new AndroidXunJS(this), "App");

        mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);    //允许加载javascript
        mWebSettings.setDomStorageEnabled(true);
        if ("微信网页版".equals(title)) {
            mWebSettings.setUserAgentString("Mozilla/5.0 (Windows; U; Windows NT 5.2; en-US) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.108 Safari/537.36 UCBrowser/12.0.4.984");
        } else {
            mWebSettings.setAppCacheEnabled(true);// 设置缓存
        }
        mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 设置缓存模式,一共有四种模式
        mWebSettings.setSupportZoom(true);          //允许缩放
        mWebSettings.setBuiltInZoomControls(true);  //原网页基础上缩放
        mWebSettings.setDisplayZoomControls(false);
        mWebSettings.setUseWideViewPort(true);      //任意比例缩放
        mWebSettings.setLoadWithOverviewMode(true);
        webView.setWebViewClient(webClient);  //设置Web视图
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                if(newProgress==100){
                    pb.setVisibility(View.GONE);//加载完网页进度条消失
                }else{
                    pb.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    pb.setProgress(newProgress);//设置进度值
                }

            }
        });
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                downloadByBrowser(url);
//                downloadBySystem(url,contentDisposition,mimeType);
            }
        });
    }
    /***
     * 设置Web视图的方法
     */
    WebViewClient webClient = new WebViewClient(){//处理网页加载失败时

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            if (!url.startsWith("http")) {
                // 前往第三方app
                try {
                    // 以下固定写法,表示跳转到第三方应用
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return super.shouldOverrideUrlLoading(view,request);
//            try {
//                /*|| url.startsWith("http:") || url.startsWith("https:")*/
//                if (url.startsWith("weixin://") || url.startsWith("alipays://") || url.startsWith("tel://")) {
//                    //类型我目前用到的是微信、支付宝、拨号 三种跳转方式，其他类型自加
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(intent);
//                    return true;
//                }
//            } catch (Exception e) {
//                return false;
//            }
//            webView.loadUrl(url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            ShowErrorPage();
        }
        @Override
        public void onPageFinished(WebView view, String url) {//处理网页加载成功时
            if (!NetworkUtils.isConnected()){
                ShowErrorPage();
                return;
            }
            error.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
        }
    };
    /***
     * 显示加载失败时自定义的网页
     */
    protected void ShowErrorPage() {
        webView.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);
        error.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.reload();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK) {
            if(webView.canGoBack()) {//当webview不是处于第一页面时，返回上一个页面
                webView.goBack();
                return true;
            } else {//当webview处于第一页面时,直接退出程序
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                if (null == webView) {
                    return;
                }
                if(webView.canGoBack()) {//当webview不是处于第一页面时，返回上一个页面
                    webView.goBack();
                } else {
                    ToastUtils.showShort("不能再回退了");
                }
                break;
            case R.id.off:
                finish();
                break;
            case R.id.fresh:
                if (null != webView) {
                    webView.reload();
                }
                break;
        }
    }

    private void downloadByBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void downloadBySystem(String url, String contentDisposition, String mimeType) {
        // 指定下载地址
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        // 允许媒体扫描，根据下载的文件类型被加入相册、音乐等媒体库
        request.allowScanningByMediaScanner();
        // 设置通知的显示类型，下载进行时和完成后显示通知
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        // 设置通知栏的标题，如果不设置，默认使用文件名
//        request.setTitle("This is title");
        // 设置通知栏的描述
//        request.setDescription("This is description");
        // 允许在计费流量下下载
        request.setAllowedOverMetered(false);
        // 允许该记录在下载管理界面可见
        request.setVisibleInDownloadsUi(false);
        // 允许漫游时下载
        request.setAllowedOverRoaming(true);
        // 允许下载的网路类型
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        // 设置下载文件保存的路径和文件名
        String fileName  = URLUtil.guessFileName(url, contentDisposition, mimeType);
        LogUtils.d("fileName:{}", fileName);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
//        另外可选一下方法，自定义下载路径
//        request.setDestinationUri()
//        request.setDestinationInExternalFilesDir()
        final DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        // 添加一个下载任务
        long downloadId = downloadManager.enqueue(request);
        LogUtils.d("downloadId:{}", downloadId);
    }
}
