package models;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Score ranking utils.
 *
 * @author jbegic
 */
public class ScoreUtils {

	public static List<Player> penalize(List<Player> players) {

		Integer totalPlayed = players.stream().collect(Collectors.summingInt(p -> p.matchesPlayed));

		Double avg = totalPlayed.doubleValue() / players.size();

		players.stream().forEach(p -> applyPenalty(p, avg));

		Collections.sort(players, (o1, o2) -> o2.ranking.compareTo(o1.ranking));

		for (int i = 0; i < players.size(); i++) {
			players.get(i).position = i + 1;
		}

		return players;
	}

	private static void applyPenalty(Player player, Double average) {
		Double delta = player.matchesPlayed - average;

		if (delta > 0D) {
			player.penalty = 0L;
		} else {
			delta = delta * -1;

			delta -= average * 0.2d;

			if (delta > 0) {
				player.penalty = Math.round(player.ranking * ((delta / average)));
				player.ranking = player.ranking - player.penalty;
			}
		}
	}
}
