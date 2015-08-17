package teamvoy.com.task.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class InternetUtil {
	private Context context;
	
	 
	public InternetUtil(Context context) {

		this.context=context;
	}
	
	
	public boolean isConnected(){
		  ConnectivityManager connec = 
                  (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
    
      // Check for network connections
       if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
            connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
            connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
            connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
           
           // if connected with internet
            
   
           return true;
            
       } else if (
         connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
         connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
          
           
           return false;
       }
     return false;
		
	}
}
