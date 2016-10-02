package controllers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import models.MatchScore;
import models.Player;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.playerDetails;

/**
 * Player controller.
 *
 * @author jbegic
 */
public class PlayerController extends Controller {

	//TODO: proper error handling
	public Result create() {
		String name = request().getQueryString("name");

		if (name != null && !name.trim().isEmpty()) {
			// Check if player with the same name already exists
			if (Player.find.where().eq("name", name.trim()).findRowCount() > 0) {
				return badRequest("Player already exists.");
			}

			Player player = Player.createWithDefaultParameters(name.trim());
			player.save();

			return ok(Json.toJson(player));
		} else {
			return badRequest("Invalid player's name.");
		}
	}
	
	public Result playerDetails(Long playerId) {
		
		Player player = Player.find.byId(playerId);
		
		List<MatchScore> matchesPlayed = player.matchesAsPlayerOne;
		matchesPlayed.addAll(player.matchesAsPlayerTwo);
		
		Collections.sort(matchesPlayed, new Comparator<MatchScore>() {
			  public int compare(MatchScore c1, MatchScore c2) {
			    if (c1.creationTs.after(c2.creationTs)) return -1;
			    if (c1.creationTs.before(c2.creationTs)) return 1;
			    return 0;
			  }});

		matchesPlayed = MatchScore.populateTimeSincePPWithDate(matchesPlayed);
		
		return ok(playerDetails.render(player, matchesPlayed));
	}
	
}
