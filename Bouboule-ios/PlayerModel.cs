using System;
using System.IO;
using System.Collections.Generic;
using MonoTouch.Foundation;
using MonoTouch.GameKit;
using MonoTouch.UIKit;

//Based on Xamarin Documentation

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
	public class PlayerModel
	{

		string storedScoresFilename;
		string storedAchievementsFilename;
		public string playerName;

		NSMutableArray storedScores;
		NSMutableDictionary storedAchievements;


		public PlayerModel ()
		{
			string id = GKLocalPlayer.LocalPlayer.PlayerID;
			playerName = GKLocalPlayer.LocalPlayer.Alias;
			storedScoresFilename = id.Substring(id.IndexOf(":")+1)+"storedScores.plist";
			storedAchievementsFilename = id.Substring(id.IndexOf(":")+1)+"storedAchievements.plist";

		}

		/* ************************************************************************
		 * 
		 * Scores
		 * 
		 * *************************************************************************
		*/

		// Store score for submission at a later time
		public void storeScore (GKScore score)
		{
			storedScores.Add (score);
			this.writeStoredScore ();
		}

		// Attempt to resubmit the scoress
		public void resubmitSotredScores()
		{

			if (GKLocalPlayer.LocalPlayer.Authenticated && storedScores.Count != 0) 
			{
				int index =(int) storedScores.Count - 1;
				while (index >=0) 
				{
					GKScore score = new GKScore (storedScores.ValueAt ((uint)index));
					score.ShouldSetDefaultLeaderboard = true;
					if (score == null)
						return;
					score.ReportScore (new GKNotificationHandler( (error) => 
					{
						/*
						if(error == null)
						{
							new UIAlertView ("Score Submitted", "Score submitted successfully ", null, "OK", null).Show();
						}
						else
						{
							this.storeScore(score);
							new UIAlertView ("Score Stored", "Score Stored ", null, "OK", null).Show();
						}*/	
					}));
					storedScores.RemoveObject(index);
					index --;
				}
			}
		}

		//Save stored scores to file
		public void writeStoredScore()
		{
			NSError err;
			NSData archivedScore = NSKeyedArchiver.ArchivedDataWithRootObject (storedScores);
			archivedScore.Save (this.storedScoresFilename, NSDataWritingOptions.FileProtectionNone, out err);
			if (err != null)
				Console.WriteLine ("Error when saving stored scores.");

		}
		// Load stored scores from disk
		public void loadStoredScores()
		{
			NSArray unarchivedObj = (NSArray) NSKeyedUnarchiver.UnarchiveFile (this.storedScoresFilename);
			if (unarchivedObj != null) 
			{
				storedScores = (NSMutableArray) unarchivedObj;
				this.resubmitSotredScores ();
			} else 
			{
				storedScores = new NSMutableArray ();
			}
		}

		// Attemp to submit a score. On an error store it for a later time
		public void submitScore(GKScore score)
		{
			if (GKLocalPlayer.LocalPlayer.Authenticated) 
			{
				if (score == null)
					return;
				score.ReportScore (new GKNotificationHandler( (error) => 
				{
					//if(error == null|| (error.Code ==null && error.Domain == null))

					if(error == null)
					{
						this.resubmitSotredScores();
						//new UIAlertView ("Score Submitted", "Score submitted successfully ", null, "OK", null).Show();
					}
					else
					{
						this.storeScore(score);
						//new UIAlertView ("Score Stored", "Score Stored ", null, "OK", null).Show();
					}
				}));
			}
		}

		/* ************************************************************************
		 * 
		 * Achivements
		 * 
		 * *************************************************************************
		*/

		// Create an entry for an achievement that hasn't been submitted to the server 
		void storeAchievement(GKAchievement achievement)
		{
			GKAchievement currentStorage = (GKAchievement) storedAchievements.ValueForKey (new NSString(achievement.Identifier));

			if (currentStorage == null) {
				storedAchievements.Add(new NSString(achievement.Identifier),achievement);
			}
			else if (currentStorage.PercentComplete < achievement.PercentComplete) {
				storedAchievements.SetValueForKey (achievement, new NSString (achievement.Identifier));
				this.writeStoredAchievements ();
			}
		}

		// store achievements to disk to submit at a later time.
		void writeStoredAchievements()
		{
			NSError err;
			NSData archivedScore = NSKeyedArchiver.ArchivedDataWithRootObject (storedAchievements);
			archivedScore.Save (this.storedAchievementsFilename, NSDataWritingOptions.FileProtectionNone, out err);
			if (err != null)
				Console.WriteLine ("Error when saving stored achievements.");

		}

		// Submit an achievement to the server and store if submission fails
		public void submitAchievement(GKAchievement achievement)
		{
			if (achievement != null) {
				//submit the achievement
				achievement.ReportAchievement (new GKNotificationHandler ((error) => {
					if (error == null) {
						if (storedAchievements.ContainsKey (new NSString(achievement.Identifier)))
							storedAchievements.Remove (new NSString(achievement.Identifier));
						Console.WriteLine("Achievement submitted successfully");
						//new UIAlertView ("Achievement Submitted", "Achievement submitted successfully ", null, "OK", null).Show ();
					} else {
						this.storeAchievement (achievement);
						//new UIAlertView ("Achievement Stored", "Achievement Stored ", null, "OK", null).Show ();
					}
				}));
			}
		}

		// Load stored achievements and attempt to submit them
		public void loadStoredAchievements()
		{
			if (storedAchievements == null) 
			{

				NSMutableDictionary unarchivedObj = (NSMutableDictionary) NSKeyedUnarchiver.UnarchiveFile (this.storedAchievementsFilename);
				if (unarchivedObj != null) 
				{
					this.resubmitStoredAchievements ();
				} else 
				{
					storedAchievements = new NSMutableDictionary ();
				}
			}
		}

		// Try to submit all stored achievements to update any achievements that were not successful. 
		public void resubmitStoredAchievements()
		{
			if (storedAchievements != null) {
				foreach (NSString key in storedAchievements.Keys) {
					GKAchievement achievement = (GKAchievement) storedAchievements.ObjectForKey (key);
					this.submitAchievement (achievement);
				}
			}
		}

		// Reset all the achievements for local player 
		public void resetAchievements()
		{
			GKAchievement.ResetAchivements (new GKNotificationHandler (delegate(NSError error) {
				if(error == null)
					new UIAlertView ("Achievement reset", "Achievement reset successfully", null, "OK", null).Show ();
				else
					new UIAlertView ("Reset failed", "Reset failed because: " + error, null, "OK", null).Show ();
			}));
		}
	}
}

