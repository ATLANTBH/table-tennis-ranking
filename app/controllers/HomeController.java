package controllers;

import java.util.Arrays;
import java.util.List;

import models.MatchScore;
import models.Player;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import services.TennisGameService;
import views.html.index;


public class HomeController extends Controller {

	public Result index() {
		
		List<MatchScore> latestScores = MatchScore.find.orderBy("creationTs desc").setMaxRows(5).fetch("playerOne").fetch("playerTwo").findList();

		latestScores = MatchScore.populateTimeSincePP(latestScores);
		
		List<Player> allPlayers = Player.find.order("ranking desc").order("position").findList();
        return ok(index.render(allPlayers, latestScores));
    }
    
    public Result addScore() {
    	
    	DynamicForm requestData = Form.form().bindFromRequest();
    	
    	Long playerOneId = Long.parseLong(requestData.get("playerOne"));
    	Long playerTwoId = Long.parseLong(requestData.get("playerTwo"));
    	Integer playerOnePoints = Integer.parseInt(requestData.get("playerOnePoints"));
    	Integer playerTwoPoints = Integer.parseInt(requestData.get("playerTwoPoints"));
    	
    	TennisGameService tgs = new TennisGameService();
    	tgs.storeNewMatch(playerOneId, playerTwoId, playerOnePoints, playerTwoPoints);
    	
    	return redirect(routes.HomeController.index());
    }
    
    public Result insertPlayers() {
    	
    	if (Player.find.order("ranking desc").findList().isEmpty()) {
    	
	    	List<String> playerNames = Arrays.asList("Amar", "Emin", "Anis", "Jasmin", "Faruk", "Emir", "Kenan");
	    	
	    	for (String name : playerNames) {
	    		
		    	Player player = new Player();
		    	player.name = name;
		    	player.position = 1;
		    	player.latestPositionDelta = 0;
		    	player.ranking = 1000L;
		    	player.matchesPlayed = 0;
		    	player.winCount = 0;
		    	player.lostCount = 0;
		    	
		    	player.save();
	    	}
	    	
	    	return ok("Players Inserted: " + playerNames);
	    	
    	} else {
    		return ok("Players already exist.");
    	}
    }
    

}
