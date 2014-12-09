package kr.ac.hyu.kangdaecheol.calendar.eventbus;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.BasicBus;

@EBean(scope = Scope.Singleton)
public class MyBus extends BasicBus {
	private final Handler handler = new Handler(Looper.getMainLooper());

    @Override public void post(final Object event) {
       if (Looper.myLooper() == Looper.getMainLooper()) {
           super.post(event);
       } else {
           handler.post(new Runnable() {
               @Override
               public void run() {
            	   MyBus.super.post(event);
               }
           });
       }
   }
}
