using System;

using MonoTouch.GameKit;
using MonoTouch.UIKit;
using MonoTouch.Foundation;

using com.badlogic.gdx.backends.ios;
using com.badlogic.gdx;
using be.ac.ucl.lfsab1509.bouboule.game.profile;
using be.ac.ucl.lfsab1509.bouboule.game.gameManager;

//Based on Xamarin Documentation and Trasc0 example from libGDX forum

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

namespace GameCenterIOS
{
	public class GameCenterManager : GameCenter
	{
		private static GameCenterManager _instance = null;

		public GKNotificationHandler authenticatedHandler;

		public PlayerModel player;
		string currentPlayerID;

		public GameCenterManager ()
		{
			// For devices lower than 6.0
			if ( !UIDevice.CurrentDevice.CheckSystemVersion (6, 0) )

				authenticatedHandler = new GKNotificationHandler (delegate(NSError error) {
					if (GKLocalPlayer.LocalPlayer.Authenticated) {
						//Switching Users

						if(currentPlayerID == null || currentPlayerID != GKLocalPlayer.LocalPlayer.PlayerID)
							switchUserInfo ();


					} else {

						Console.WriteLine(GlobalSettings.PROFILE_MGR.getProfileGlobal().isGameCenterDisable());

						//By verifying the availability here (error) and before the call, it ensure
						//that if the user activate later the game center, it is set on.
						if ( ! GlobalSettings.PROFILE_MGR.getProfileGlobal().isGameCenterDisable() ) {

							showGameCenterAlert ();

						} else { 

							//The game Center must be disabled !
							GlobalSettings.GAMECENTER = null;

							//Set the default user
							setDefaultUser ();
						}

					}
				});

		}

		//Set the default Bouboule User.
		private void setDefaultUser () {

			currentPlayerID = null;
			//EndGameListener.resetGame ();
			GlobalSettings.PROFILE_MGR.switchUser(GlobalSettings.DEFAULT_PROFILE_NAME);
		}

		public static GameCenterManager getInstance(){
			if (_instance == null) {
				_instance = new GameCenterManager();
			}

			return _instance;
		}

		public bool isGameCenterAPIAvailble(){
			return UIDevice.CurrentDevice.CheckSystemVersion (4, 1);
		}

		public string getPlayerName ()
		{
			if (player != null)
				return player.playerName;
			else
				return GlobalSettings.DEFAULT_PROFILE_NAME;
		}

		public string getPlayerID ()
		{
			Console.WriteLine (currentPlayerID);
			return currentPlayerID;
		}


		public void getAchivements(){
			GKCompletionHandler handler = new GKCompletionHandler((achivements, error) => {
				if(error==null){
					if(achivements!=null){
						Console.Out.WriteLine("there is " + achivements.Length + "achievements");
						Console.Out.WriteLine(achivements[0].LastReportedDate.ToString());
					}
				}else{
					//Error
				}

			});
			GKAchievement.LoadAchievements (handler);
		}

		//Authentificate the player for Game Center user
		public void Authenticate ()
		{

			//Tentative de connexion

			// For devices lower than 6.0
			if ( ! UIDevice.CurrentDevice.CheckSystemVersion (6, 0)) {
				GKLocalPlayer.LocalPlayer.Authenticate (authenticatedHandler);
				return;
			}

			GKLocalPlayer.LocalPlayer.AuthenticateHandler = (ui, err) => {

				// If ui is null, that means the user is already authenticated,
				// for example, if the user used Game Center directly to log in

				if (ui != null) {

					// Not authentificated
					// Show the login UI view for log in.

					Console.WriteLine("Will show the login screen");
					UIViewController controller = UIApplication.SharedApplication.Windows[0].RootViewController;
					controller.PresentViewController(ui,true,null);

					return;
				
				} else {

					//Console.WriteLine ("Authentication result: {0}", err);

					// Check if you are authenticated:
					if (GKLocalPlayer.LocalPlayer.Authenticated) {
						// Switching Users

						if(currentPlayerID == null || currentPlayerID != GKLocalPlayer.LocalPlayer.PlayerID)
							switchUserInfo();

					} else {

						Console.WriteLine("Is the game center disabled : " + GlobalSettings.PROFILE_MGR.getProfileGlobal().isGameCenterDisable());

						// By verifying the availability here (error) and before the call, it ensure
						// that if the user activate later the game center, it is set on.
						if ( ! GlobalSettings.PROFILE_MGR.getProfileGlobal().isGameCenterDisable() ) {

							showGameCenterAlert ();

						} else { 

							// The game Center is disabled !
							GlobalSettings.GAMECENTER = null;

							// Set the default user
							setDefaultUser ();
						}

					}

				}
			};

		}

		private void switchUserInfo () {

			// Load the player settings
			currentPlayerID = GKLocalPlayer.LocalPlayer.PlayerID;
			player = new PlayerModel();
			player.loadStoredScores();
			player.loadStoredAchievements();

			// If the user log in after  the lauchn of the game.
			if (GlobalSettings.GAMECENTER == null)
				GlobalSettings.GAMECENTER = this;

			if (GlobalSettings.PROFILE_MGR != null)
				GlobalSettings.PROFILE_MGR.switchUser(currentPlayerID);
		
		}

		private void showGameCenterAlert () {

			UIAlertView alert;

			// For devices lower than 6.0
			if (!UIDevice.CurrentDevice.CheckSystemVersion (6, 0)) {

				alert = new UIAlertView () { 
					Title = "Game Center Login", Message = "The Game Center improves the game experience but you can play without it."
				};
				alert.AddButton("Login");
				alert.AddButton("Dismiss");
				alert.Clicked += (sender, e) => {

					//If the user don't want to use the game center -> choice 2
					if ( e.ButtonIndex == 0) //Retry login
						GKLocalPlayer.LocalPlayer.Authenticate (authenticatedHandler);

					if ( e.ButtonIndex == 1) { //Disable gamecenter
						//Console.WriteLine("Game Center Disabled");
						GlobalSettings.GAMECENTER = null;
						GlobalSettings.PROFILE_MGR.getProfileGlobal().toggleGameCenter();

						//Set the default user
						setDefaultUser ();

					}
				};

			} else {

				alert = new UIAlertView () { 
					Title = "Game Center Disabled", Message = "Login from the Game Center App, if you want to use it later."
				};
				alert.AddButton ("Ok");
				alert.Clicked += (sender, e) => {

					/* Depreciated in iOS 6+ - You can't relauch the login window */

					if (e.ButtonIndex == 0) { //Disable gamecenter
						Console.WriteLine ("Game Center Disabled");

						// The game Center must be disabled !
						GlobalSettings.GAMECENTER = null;
						GlobalSettings.PROFILE_MGR.getProfileGlobal ().toggleGameCenter ();

						// Set the default user
						setDefaultUser ();

					}
				};
			}

			alert.Show ();


		}


		public void resetAchievements() 
		{
			if (!GKLocalPlayer.LocalPlayer.Authenticated) {
				new UIAlertView ("Error", "Need sign in Game Center to reset the achievement", null, "OK", null).Show();
				Authenticate ();
				return;
			}
			player.resetAchievements ();
		
		}

		//Submit the achivement or store if no connection
		//"be.ac.ucl.lfsab1509.bouboule."+achievementsName and percentageValue is the completion rate.
		//Completion banner are defaulted activated
		public void submitAchievement(String achievementsName, int percentageValue)
		{
			if (!GKLocalPlayer.LocalPlayer.Authenticated) {
				//new UIAlertView ("Error", "Need sign in Game Center to submit the achievement", null, "OK", null).Show();
				Console.WriteLine ("Need sign in Game Center to submit the achievement");
				Authenticate ();
				return;
			}

			//Create the achievement we want to submit.
			NSString identifier = new NSString ("be.ac.ucl.lfsab1509.bouboule."+achievementsName);

			GKAchievement achievement = new GKAchievement (identifier);

			achievement.PercentComplete = percentageValue;
			achievement.ShowsCompletionBanner = true;

			Console.WriteLine (achievement.ShowsCompletionBanner);

			player.submitAchievement (achievement);
		}

		public void submitScore(int scoreValue) {

			//Submit and server check Best Score.
			submitScore(scoreValue, "be.ac.ucl.lfsab1509.bouboule.leaderboardscorebest");
			//Submit Last Score.
			submitScore(scoreValue, "be.ac.ucl.lfsab1509.bouboule.leaderboardscore");

		}

		//submit score to default leaderboard
		public void submitScore(int scoreValue, String leaderboardName)
		{
			if (!GKLocalPlayer.LocalPlayer.Authenticated) {
				//new UIAlertView ("Error", "Need sign in Game Center to submit the score", null, "OK", null).Show();
				Console.WriteLine ("Need sign in Game Center to submit the score");
				Authenticate ();
				return;
			}


			GKScore submitScore = new GKScore (leaderboardName);
			submitScore.Init ();
			submitScore.category = leaderboardName;
			try{
				submitScore.Value = Convert.ToInt64(scoreValue);
			}
			catch{
				new UIAlertView ("Error", "Score should be a number", null, "OK", null).Show();
				return;
			}

			submitScore.ShouldSetDefaultLeaderboard = true;
			player.submitScore (submitScore);
		}


		public void showLeaderboard()
		{
			GKLeaderboardViewController leaderboardViewController = new GKLeaderboardViewController ();
			leaderboardViewController.Category = "Leaderboard";
			leaderboardViewController.DidFinish += (object sender, EventArgs e) => {
				leaderboardViewController.DismissViewController(true, null);
			};

			((IOSApplication)Gdx.app).getUIViewController().PresentViewController (leaderboardViewController, true, null);

			Console.WriteLine ("showLeaderboard complete");
		}

		public void showAchievements()
		{
			GKAchievementViewController achievementViewController = new GKAchievementViewController ();
			achievementViewController.DidFinish += (object sender, EventArgs e) => {
				achievementViewController.DismissViewController(true, null);
			};
		
		
			((IOSApplication)Gdx.app).getUIViewController ().PresentViewController (achievementViewController, true, null);

			Console.WriteLine ("achievementsboard complete");
		}


	}
}
