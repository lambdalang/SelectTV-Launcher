package com.selecttvlauncher.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.selecttvlauncher.R;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.selecttvlauncher.ui.views.DynamicImageView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RadioService extends Service implements OnPreparedListener,
		OnErrorListener {

	WindowManager windowManager;
	View mainView;
	RelativeLayout linearTopParent;
	DynamicImageView imageThumgnail;
	TextView txtChannelName;
	ImageView imageClose;
	Button btnPlayStop;
	int adViewHeight = 0;
	boolean bAdd = false;
	private Point szWindow = new Point();

	public MediaPlayer player;
	String url, chnlName, image;
	Bundle bundle;
	PhoneStateListener psl;
	TelephonyManager tManager;
	Intent intent;

	IBinder mBinder = new LocalBinder();


	public static final String ACTION_RESUME = "service.action.resume";
	public static final String ACTION_PAUSE = "service.action.pause";
	public static final String ACTION_SETUP_AND_PLAY = "service.action.setupandplay";
	public static final String RECIEVER_ACTION_PLAYING = "playing";
	public static final String RECIEVER_ACTION_STOPPED = "paused";
	public static final String RECIEVER_ACTION_PREPARING = "preparing";
	public static final String RECIEVER_ACTION_CLOSE = "close";
	public static final String RECIEVER_ACTION_PREPARE_ERROR = "prepareerror";

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		initializemediaplayer();
	}

	private void initializemediaplayer() {
		try {
			player = new MediaPlayer();
			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
			handlePhoneCall();
			player.setOnPreparedListener(this);
			player.setOnErrorListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static float convertDpToPixel(float dp, Context context){
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		try {
			bundle = intent.getExtras();
			url = bundle.getString("url");//"http://streema.com/radios/play/1980";//bundle.getString("url");
			chnlName = bundle.getString("name");
			image = bundle.getString("image");
			adViewHeight = bundle.getInt("height");
			this.intent = intent;

		/*if( url.endsWith(".asx") ){
			//new ASXParser(url).execute();
			new PlsParser(url).execute();
		}else if( url.contains(".pls")||url.endsWith(".m3u")) {
			new PlsParser(url).execute();

		}else{
				initService();
		}*/
			if( url.endsWith(".asx") ){
                new ASXParser(url).execute();
            }else if( url.endsWith(".pls")) {
                List<String> urls = new PlsParser(url).getUrls();
                if( urls != null && urls.size() > 0 )
                    url = urls.get(0);
                initService();
            }else{
                initService();
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return START_NOT_STICKY;
	}
	private void initService(){
		try {
			if (intent.getAction() == ACTION_SETUP_AND_PLAY) {

                windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
                mainView = (View)inflater.inflate(R.layout.radio_thumbview, null);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    windowManager.getDefaultDisplay().getSize(szWindow);
                } else {
                    int w = windowManager.getDefaultDisplay().getWidth();
                    int h = windowManager.getDefaultDisplay().getHeight();
                    szWindow.set(w, h);
                }

                linearTopParent = (RelativeLayout)mainView.findViewById(R.id.linearTopParent);
                btnPlayStop = (Button)mainView.findViewById(R.id.btnRadioPlayStop);
                txtChannelName = (TextView)mainView.findViewById(R.id.txtChannelName);
                imageClose = (ImageView)mainView.findViewById(R.id.imageClose);
                imageThumgnail = (DynamicImageView)mainView.findViewById(R.id.imageThumbnail);

                imageThumgnail.loadImage(image);
                txtChannelName.setText(chnlName);

                LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) linearTopParent.getLayoutParams();
                param.width = szWindow.x;
                param.height = (int) convertDpToPixel(70, getApplicationContext());

                btnPlayStop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if( btnPlayStop.getText().toString().equals("Play") ){
                            playerResume();
                        }else{
                            playerPause();
                        }
                    }
                });

                imageClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RadioService.this.stopSelf();
                    }
                });

                playerSetUpAndPlay();
            } else if (intent.getAction() == ACTION_RESUME) {
                playerResume();
            } else if (intent.getAction() == ACTION_PAUSE) {
                playerPause();
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handlePhoneCall() {
		try {
			psl = new PhoneStateListener() {

                @Override
                public void onCallStateChanged(int state, String incomingNumber) {

                    Log.e("PhonState", "Changed");
                    if (state == TelephonyManager.CALL_STATE_RINGING) {
                        // Incoming call: Pause music
                        Log.e("PhonState", "Ringing");
                        if (player != null) {
                            if (player.isPlaying()) {

                                player.pause();

                            }
                        }
                    } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                        // Not in call: Play music
                        Log.e("PhonState", "Idle");
                        if (player != null) {
                            if (!player.isPlaying()) {
                                player.start();

                            }
                        }

                    } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                        // A call is dialing, active or on hold
                        Log.e("PhonState", "Dialing");
                        if (player != null) {
                            if (player.isPlaying()) {

                                player.pause();

                            }
                        }
                    }

                    // TODO Auto-generated method stub
                    super.onCallStateChanged(state, incomingNumber);
                }

            };
			tManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			if (tManager != null) {
                tManager.listen(psl, PhoneStateListener.LISTEN_CALL_STATE);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		playerStopANdRealease();
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		try {
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
                @Override
                public void run() {
					try {
						if(player!=null){
                            player.start();
                            if (player.isPlaying()) {
                                pushServicetoForeground();
                                adViewHeight = (int) convertDpToPixel(150,getApplicationContext());
                                addWindow(mainView, 0, szWindow.y - adViewHeight - (int) convertDpToPixel(70, getApplicationContext()));
                                btnPlayStop.setText("Stop");
                                broadCast(RECIEVER_ACTION_PLAYING);

                            }
                        }
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
            }, 500);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void addWindow(View view, int x, int y){
		try {
			WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    PixelFormat.TRANSLUCENT);
			params.gravity = Gravity.BOTTOM | Gravity.LEFT;

			windowManager.addView(view, params);
			bAdd = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void pushServicetoForeground() {
		try {
			final int notiId = 1234;
			Notification notice;
			NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    this).setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText("Playing: " + chnlName).setAutoCancel(true);
			Intent notificationIntent = new Intent(this, HomeActivity.class);
			notificationIntent.putExtra("BUNDLE", bundle);
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			builder.setContentIntent(contentIntent);
			notice = builder.build();
			startForeground(notiId, notice);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		Log.d("player:::", "::" + "media player error");
		broadCast(RECIEVER_ACTION_PREPARE_ERROR);
		return true;
	}
	public void playerPause() {
		try {
			if (player != null) {
                if (player.isPlaying()) {
                    player.pause();
                    broadCast(RECIEVER_ACTION_STOPPED);
                    btnPlayStop.setText("Play");
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void playerResume() {
		try {
			if (player != null) {
                if (!player.isPlaying()) {
                    player.start();
                    broadCast(RECIEVER_ACTION_PLAYING);
                    btnPlayStop.setText("Stop");
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void broadCast(String recieverAction) {
		try {
			Log.d("Sending bradcast::::", "::::" + recieverAction);
			Intent broadcastIntent = new Intent();
			broadcastIntent.setAction(recieverAction);
			sendBroadcast(broadcastIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void playerSetUpAndPlay() {
		try {
			if (player != null) {
				if (!player.isPlaying()) {
					Log.d("URL::::", "::::" + url);
					URI uu=new URI(""+url);
					String host = uu.getHost();
					int port = uu.getPort();
					Log.d("URL::::URI", "::::" + host+"::"+port);

					/*if(!url.contains(".mp3")){
						if(port!=-1){
							Log.d("URL::::", "::::" + "is an IP");
							String last_char=url.substring(url.length() - 1);
							char charr=url.charAt(url.length() - 1);
							Log.d("lastcgaracter::","::"+last_char);
							if(last_char.equals(";")){
								Log.d("stream::","::"+"ok");
							}else if(last_char.equals("/")){
								Log.d("stream::","::" + "add ;");
								url=url+";";
							}else if(Character.isDigit(charr)){
								Log.d("stream::","::"+"add /;");
								url=url+"/;";
							}else{
								Log.d("stream::","::"+"cant predict");
							}
						}else if(isIP(url)){
							Log.d("URL::::", "::::" + "is an IP");
							String last_char=url.substring(url.length() - 1);
							char charr=url.charAt(url.length() - 1);
							Log.d("lastcgaracter::","::"+last_char);
							if(last_char.equals(";")){
								Log.d("stream::","::"+"ok");
							}else if(last_char.equals("/")){
								Log.d("stream::","::" + "add ;");
								url=url+";";
							}else if(Character.isDigit(charr)){
								Log.d("stream::","::"+"add /;");
								url=url+"/;";
							}else{
								Log.d("stream::","::"+"cant predict");
							}
						}
					}*/



					Log.d("URL::::", "::::" + url);
					try {
						player.setDataSource(url);
						player.prepareAsync();
						broadCast(RECIEVER_ACTION_PREPARING);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void playerStopANdRealease() {
		try {
			if (player != null) {
                if (player.isPlaying()) {
                    player.stop();
                }
                player.release();
                player = null;
                broadCast(RECIEVER_ACTION_CLOSE);
                if( bAdd )
                    windowManager.removeView(mainView);
                bAdd = false;
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void pplayerPause() {
	}


	public class ASXParser extends AsyncTask {

		URL asxUrl;
		String pathUrl;
		String streamUrl;
		XmlPullParser xmlpullparser;
		ASXParser(String pathUrl){
			this.pathUrl = pathUrl;
			this.streamUrl = pathUrl;
		}
		void parseTag(int event){

			try {
				switch (event) {

                    case XmlPullParser.START_DOCUMENT:
                        Log.i("XmlPullParser","START_DOCUMENT");
                        break;

                    case XmlPullParser.END_DOCUMENT:
                        Log.i("XmlPullParser","END_DOCUMENT");
                        break;
                    case XmlPullParser.START_TAG:
                        Log.i("XmlPullParser","START_TAG");
                        if( xmlpullparser.getName().toLowerCase().equals("ref") ){
                            String temp = xmlpullparser.getAttributeValue(null, "href");
                            if( temp != null )
                                streamUrl = temp;
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        Log.i("XmlPullParser","END_TAG : "+xmlpullparser.getName());
                        break;

                    case XmlPullParser.TEXT:
                        Log.i("XmlPullParser","TEXT");
                        String output = xmlpullparser.getText();
                        Log.i("valuee : ",""+output);
                        break;
                }
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		public InputStream getInputStream(URL url) {
			try {
				return url.openConnection().getInputStream();
			} catch (IOException e) {
				return null;
			}
		}

		@Override
		protected Object doInBackground(Object[] objects) {
			try {
				asxUrl = new URL(pathUrl);
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				factory.setNamespaceAware(true);
				xmlpullparser = factory.newPullParser();
				xmlpullparser.setInput(new InputStreamReader(getInputStream(asxUrl)));
				Log.d(":::xmlpullparser","::;"+xmlpullparser);
				int eventType = 0;
				try {
					eventType = xmlpullparser.getEventType();
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				}
				while (eventType != XmlPullParser.END_DOCUMENT) {

					parseTag(eventType);
					try {
						eventType = xmlpullparser.next();
					} catch (XmlPullParserException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}catch (Exception e1){
						e1.printStackTrace();
					}
				}
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}catch (Exception e1){
				e1.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Object params) {
			try {
				url = this.streamUrl;
				Log.d(".asx parsed::","::"+url);
				initService();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public class PlsParser extends AsyncTask {
		private BufferedReader reader = null;
		private String nUrl="";

		public PlsParser(final String url) {

			try {
				nUrl=url;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public List<String> getUrls() {
			LinkedList<String> urls = new LinkedList<String>();
			while (true) {
				try {
					if(reader!=null){
						String line = reader.readLine();
						Log.d("plsparsinglines:::","::"+line);
						if (line == null) {
							break;
						}
						String url = parseLine(line);
						if (url != null && !url.equals("")) {
							urls.add(url);
						}
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return urls;
		}

		private String parseLine(String line) {
			if (line == null) {
				return null;
			}
			String trimmed = line.trim();
			if (trimmed.indexOf("http") >= 0) {
				return trimmed.substring(trimmed.indexOf("http"));
			}
			return "";
		}

		@Override
		protected Object doInBackground(Object[] params) {
			try {
				URLConnection urlConnection = new URL(url).openConnection();
				reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

				List<String> urls = getUrls();
				if( urls != null && urls.size() > 0 )
					url = urls.get(0);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object o) {
			Log.d("pls parsed::","::"+url);
			initService();
			super.onPostExecute(o);
		}
	}

	public static boolean isIP(String urll) {
		final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile( "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)");
		try {
			Matcher matcher = EMAIL_ADDRESS_PATTERN.matcher(urll);
			if (matcher.find()) {
				Log.d("url::", "::" + "ip mached");
				return true;
			}
			else{
				Log.d("url::","::"+"ip not mached");
				return false;
			}
		} catch(Exception exception ) {
			return false;
		}
	}

	public class LocalBinder extends Binder {
		public RadioService getServerInstance() {
			return RadioService.this;
		}
	}

}
