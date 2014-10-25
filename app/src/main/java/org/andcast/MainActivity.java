package org.andcast;

import org.andcast.casting.CastConfiguration;
import org.andcast.casting.CastingService;
import org.andcast.casting.CastingService.CastingBinder;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ArrayAdapter;

public class MainActivity extends Activity {
	
	private CastingBinder binder;
	private MediaProjectionManager mgr;

	private ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			binder = (CastingBinder) service;
			
			System.out.println("Starting capture intent");
			MainActivity.this.startActivityForResult(mgr.createScreenCaptureIntent(), 1);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			
		}
		
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)  
	{  
		super.onActivityResult(requestCode, resultCode, data);  

		System.out.println("Got result: "+resultCode);
		
		CastConfiguration config = new CastConfiguration();
		config.width = 1280;
		config.height = 720;
		config.dpi = 100;
		config.bitrate = 10000;
		config.frameRate = 30;
		config.iFrameIntervalSecs = 1;
		
		binder.start(mgr.getMediaProjection(resultCode, data), config);
		
		System.out.println("Started");
	}  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		mgr = (MediaProjectionManager) MainActivity.this.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
		
		bindService(new Intent(this, CastingService.class), serviceConnection,
				Service.BIND_AUTO_CREATE);
	}
}