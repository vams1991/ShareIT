package mobilecomputing.ShareARide;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by vams1991 on 10/24/2015.
 */
public class ApplicationController extends Application {

    public void onCreate(){
        super.onCreate();
        System.out.println("I am here");
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "4ssryv5GJ2BcCm5bmbReG9rMyv7uUD2QIzJ882IM", "rdoxTZWS9ZZK8AXBfTdPeSQgPhhDjpozfA08vxHA");

    }
}
