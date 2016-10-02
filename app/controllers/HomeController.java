package controllers;

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

		List<MatchScore> latestScores = MatchScore.find.orderBy("creationTs desc")
				.setMaxRows(5).fetch("playerOne").fetch("playerTwo").findList();

		latestScores = MatchScore.populateTimeSincePP(latestScores);

		

		List<Player> allPlayers = Player.find.order("name").findList();
		List<Player> playersWithMatches = Player.find.where().gt("matchesPlayed", 0).orderBy("ranking desc").orderBy("position").findList();
        return ok(index.render(playersWithMatches, allPlayers, latestScores));
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
}
