package com.wjf.self_library.http;






import com.coding.poemway.common.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class HttpMethods {
    private static final String TAG = Constants.INFO_TAG;//用于debug的标志，随便写
    private static final String BASE_URL = Constants.BASE_URL;//定义基址
    private static final long DEFAULT_TIMEOUT = 5;//定义默认请求超时时间
    private static HttpMethods sInstance;//定义单例
    //服务层的创建和定义
//    protected static UserService userService;


    public HttpMethods() {
        if (sInstance == null) {//如果单例不存在，就新建
            //基于okHttp的一些封装
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .build();
            //基于retrofit的一些封装
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
            //服务层的创建，请写好了服务层再来这里创建
//            userService = retrofit.create(UserService.class);
        }
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static HttpMethods getInstance() {
        if (sInstance == null) {
            synchronized (HttpMethods.class) {
                if (sInstance == null) {
                    sInstance = new HttpMethods();
                }
            }
        }
        return sInstance;
    }

    /**
     * 请求回调封装，这里的HttpResult就是之前发过的，因为需要根据code的结果来判断数据是否存在，所以这样写
     * 当然把Func1<HttpResult<T>,HttpResult>中的第二个HttpResult改为泛型T，然后return result.getData()这样也是可以的，推荐试一下
     *
     * @param <T>
     */
    public static class HttpResultFunc<T> implements Func1<HttpResult<T>, HttpResult> {
        @Override
        public HttpResult call(HttpResult<T> result) {
//            Log.i(TAG, "resultCode:" + result.getResultCode() + "；msg:" + result.getMessage());
            return result;
        }
    }


    /**
     * 观察者和订阅者的封装，这个了解一下就可以了
     *
     * @param observable
     * @param subscriber
     * @param <T>
     */
    protected static <T> void toSubscribe(Observable<T> observable, Subscriber<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}

