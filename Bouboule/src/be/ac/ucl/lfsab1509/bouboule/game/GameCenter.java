package be.ac.ucl.lfsab1509.bouboule.game;

public interface GameCenter {
	


	public void Authenticate ();
	
	public void resetAchievements();

	public void showLeaderboard();
	
	public void showAchievements();
	
	public void submitAchievement(String achievements, int percentageValue);
	
	public void submitScore(int scoreValue);
	
	public String getPlayerName ();


}
