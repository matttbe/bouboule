using System;
using System.Collections.Generic;
using System.Linq;

using MonoTouch.Foundation;
using MonoTouch.UIKit;
using MonoTouch.GameKit;

using com.badlogic.gdx.backends.ios;
using be.ac.ucl.lfsab1509.bouboule.game; 
using GameCenterIOS;

/*
 * This file is part of Bouboule.
 * 
 * Copyright 2013 UCLouvain
 * 
 * Authors:
 *  * Group 7 - Course: http://www.uclouvain.be/en-cours-2013-lfsab1509.html
 *    Matthieu Baerts <matthieu.baerts@student.uclouvain.be>
 *    Baptiste Remy <baptiste.remy@student.uclouvain.be>
 *    Nicolas Van Wallendael <nicolas.vanwallendael@student.uclouvain.be>
 *    Hélène Verhaeghe <helene.verhaeghe@student.uclouvain.be>
 * 
 * Bouboule is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

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
				//config.displayScaleLargeScreenIfRetina = 1f;
				//config.displayScaleLargeScreenIfNonRetina = 1f;
				//config.displayScaleSmallScreenIfNonRetina = 1f;
				//config.displayScaleSmallScreenIfRetina = 0.5f;

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

				//Set the GameManager Connection, can be dismissed by user.
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
