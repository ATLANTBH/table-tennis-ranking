package services;

import java.util.Date;
import java.util.List;

import models.MatchScore;
import models.Player;

 public class TennisGameService {
	 
	 private static final double ELO_K_FACTOR = 32;
	 
	 public void storeNewMatch(Long playerOneId, Long playerTwoId, Integer playerOnePoints, Integer playerTwoPoints) {

	    	boolean isPlayerOneWin = playerOnePoints > playerTwoPoints;

	    	Player playerOne = Player.find.byId(playerOneId);
	    	Player playerTwo = Player.find.byId(playerTwoId);

	    	MatchScore score = new MatchScore();
	    	score.playerOnePoints = playerOnePoints;
	    	score.playerTwoPoints = playerTwoPoints;
	    	score.playerOne = playerOne;
	    	score.playerTwo = playerTwo;
	    	score.creationTs = new Date();
	    	
	    	long ratingDelta = calculateRatingDelta(playerOne.ranking, playerTwo.ranking, isPlayerOneWin);

			score.playerOneScoreDelta = isPlayerOneWin ? ratingDelta : -ratingDelta; 
	    	score.playerTwoScoreDelta = !isPlayerOneWin ? ratingDelta : -ratingDelta; 

	    	updatePlayerMatchStats(playerOne, score.playerOneScoreDelta, isPlayerOneWin);
	    	updatePlayerMatchStats(playerTwo, score.playerTwoScoreDelta, !isPlayerOneWin);

	    	score.save();

	    	updatePlayersRanking(playerOne, playerTwo, score);
	    	
	 }
	 
	 private void updatePlayersRanking(Player playerOne, Player playerTwo, MatchScore score) {

	    	List<Player> allPlayers = Player.find.order("ranking desc").findList();
	    	
	    	for (int i = 0; i < allPlayers.size(); i++) {
	    		
	    		Player player = allPlayers.get(i);
	    		
	    		player.latestPositionDelta = player.position - (i + 1);
	    		
	    		player.position = i + 1;
	    		
	    		player.update();
	    		
	    		if (playerOne.id.equals(player.id)) {
	    			score.playerOneRankDelta = player.latestPositionDelta;
	    		} else if (playerTwo.id.equals(player.id)) {
	    			score.playerTwoRankDelta = player.latestPositionDelta;
	    		}
	    		
	    	}
	    	score.update();
	 }

	private void updatePlayerMatchStats(Player player, long playerRatingDelta, boolean isPlayerWin) {

    	if (isPlayerWin) {
    		player.winCount += 1;
    	} else {
    		player.lostCount += 1;
    	}

    	player.matchesPlayed += 1;

    	player.ranking = player.ranking + playerRatingDelta;

    	player.update();
	}

	private long calculateRatingDelta(double p1Rating, double p2Rating, boolean p1Win) {
		
		double p1Score = p1Win ? 1 : 0;

		double p1TransformedRating = Math.pow(10, p1Rating / 400);
		double p2TransformedRating = Math.pow(10, p2Rating / 400);

		double p1ExpectedScore = p1TransformedRating / (p1TransformedRating + p2TransformedRating);
		
		return Math.abs(Math.round(ELO_K_FACTOR * (p1Score - p1ExpectedScore)));
	}

}
