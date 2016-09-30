package controllers;

import models.Player;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

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
}
