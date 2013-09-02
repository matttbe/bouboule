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
			authenticatedHandler = new GKNotificationHandler (delegate(NSError error) {
				if (GKLocalPlayer.LocalPlayer.Authenticated) {
					//Switching Users

					if(currentPlayerID == null || currentPlayerID != GKLocalPlayer.LocalPlayer.PlayerID)
					{
						//load the player settings
						currentPlayerID = GKLocalPlayer.LocalPlayer.PlayerID;
						player = new PlayerModel();
						player.loadStoredScores();
						player.loadStoredAchievements();

						if (GlobalSettings.PROFILE_MGR != null)
							GlobalSettings.PROFILE_MGR.switchUser(currentPlayerID);

					}
				} else {
					/* var alert = new UIAlertView ("Game Center Login", "Need login the game center!", null, "Retry", null);
					alert.Clicked += delegate {
						GKLocalPlayer.LocalPlayer.Authenticate (authenticatedHandler);
					};
					alert.Show (); */

					UIAlertView alert = new UIAlertView () { 
						Title = "Game Center Login", Message = "The Game Center improve the Bouboule experience but you can play without."
					};
					alert.AddButton("Login");
					alert.AddButton("Dismiss");
					alert.Clicked += (sender, e) => {

						//If the user don't want to use the game center -> choice 2
						if ( e.ButtonIndex == 0) //Retry login
							GKLocalPlayer.LocalPlayer.Authenticate (authenticatedHandler);

						if ( e.ButtonIndex == 1) { //Disable gamecenter
							Console.WriteLine("Game Center Disabled");
							GlobalSettings.GAMECENTER = null;
						}
					};

					alert.Show ();

				}
			});

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
				return "Bouboule";
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

			GKLocalPlayer.LocalPlayer.Authenticate (authenticatedHandler);

		}


		public void resetAchievements() 
		{
			if (!GKLocalPlayer.LocalPlayer.Authenticated) {
				new UIAlertView ("Error", "Need sign in Game Center to reset the achievement", null, "OK", null).Show();
				GKLocalPlayer.LocalPlayer.Authenticate (authenticatedHandler);
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
				new UIAlertView ("Error", "Need sign in Game Center to submit the achievement", null, "OK", null).Show();
				GKLocalPlayer.LocalPlayer.Authenticate (authenticatedHandler);
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


		//submit score to default leaderboard
		public void submitScore(int scoreValue)
		{
			if (!GKLocalPlayer.LocalPlayer.Authenticated) {
				new UIAlertView ("Error", "Need sign in Game Center to submit the score", null, "OK", null).Show();
				GKLocalPlayer.LocalPlayer.Authenticate (authenticatedHandler);
				return;
			}


			GKScore submitScore = new GKScore ("be.ac.ucl.lfsab1509.bouboule.leaderboardscore");
			submitScore.Init ();
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
