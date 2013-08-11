using System;
using System.Collections.Generic;
using System.Linq;

using MonoTouch.Foundation;
using MonoTouch.UIKit;
using MonoTouch.GameKit;

using com.badlogic.gdx.backends.ios;
using be.ac.ucl.lfsab1509.bouboule.game; 
using GameCenterIOS;

namespace be.ac.ucl.lfsab1509.bouboule.game
{		
	public class Application
	{
		[Register ("AppDelegate")]
		public partial class AppDelegate : IOSApplication {

			public GKNotificationHandler authenticatedHandler;
			private static GameCenterManager _gameCenterManager = null;


			public AppDelegate(): base(new MyGame(getGameCenterManager()), getConfig()) {

			}

			internal static GameCenterManager getGameCenterManager() 
			{

				if ( _gameCenterManager == null )
					_gameCenterManager = new GameCenterManager();

				return _gameCenterManager;
			}

			internal static IOSApplicationConfiguration getConfig() {
				IOSApplicationConfiguration config = new IOSApplicationConfiguration();
				config.orientationLandscape = false;
				config.orientationPortrait = true;
				config.useAccelerometer = true;
				config.useMonotouchOpenTK = true;
				config.useObjectAL = true;
				config.displayScaleLargeScreenIfRetina = 1f;
				//config.displayScaleLargeScreenIfNonRetina = 1f;
				//config.displayScaleSmallScreenIfNonRetina = 1f;
				//config.displayScaleSmallScreenIfRetina = 1f;

				return config;
			}

			//
			// This method is invoked when the application has loaded and is ready to run. In this 
			// method you should instantiate the window, load the UI into it and then make the window
			// visible.
			//
			// You have 17 seconds to return from this method, or iOS will terminate your application.
			//
			public override bool FinishedLaunching (UIApplication app, NSDictionary options)
			{
				base.FinishedLaunching(app,options);

				//Set the GameManager Obligatory Connection
				_gameCenterManager.Authenticate ();

				return true;
			}


		}

		static void Main (string[] args)
		{
			UIApplication.Main (args, null, "AppDelegate");
		}
	}
}
