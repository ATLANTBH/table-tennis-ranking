package models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.avaje.ebean.Model;



@Entity
public class MatchScore extends Model {

	@Id
	public Long id;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
	public Player playerOne;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	public Player playerTwo;
	
	public Integer playerOnePoints;
	public Integer playerTwoPoints;
	
	public Integer playerOneRankDelta;
	public Integer playerTwoRankDelta;
	public Long playerOneScoreDelta;
	public Long playerTwoScoreDelta;
	
	public Date creationTs;
	
	@Transient
	public String timeSincePP;
	
	
	public static List<MatchScore> populateTimeSincePP(List<MatchScore> scores) {
		for (MatchScore score : scores) {
			score.timeSincePP = timeSince(score.creationTs);
		}
		return scores;
	}
	
	public static List<MatchScore> populateTimeSincePPWithDate(List<MatchScore> scores) {
		SimpleDateFormat dt1 = new SimpleDateFormat("d MMM");

		
		for (MatchScore score : scores) {
			score.timeSincePP = dt1.format(score.creationTs);
		}
		return scores;
	}
	

	private static String timeSince(Date date) {

		long seconds = Math.round((new Date().getTime() - date.getTime()) / 1000);

	    long interval = Math.round(seconds / 31536000);

	    if (interval > 1) {
	        return interval + " years";
	    }
	    interval = Math.round(seconds / 2592000);
	    if (interval > 1) {
	        return interval + " months";
	    }
	    interval = Math.round(seconds / 86400);
	    if (interval > 1) {
	        return interval + " days";
	    }
	    interval = Math.round(seconds / 3600);
	    if (interval > 1) {
	        return interval + " hours";
	    }
	    interval = Math.round(seconds / 60);
	    if (interval > 1) {
	        return interval + " minutes";
	    }
	    return Math.round(seconds) + " seconds";
	}
	
	public static Find<Long, MatchScore> find = new Find<Long, MatchScore>() {};
	
}
