package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.avaje.ebean.Model;

@Entity
public class Player extends Model {

	@Id
	public Long id;
	
	public String name;

	public Integer position;
	public Integer latestPositionDelta;
	public Long ranking;
	public Integer matchesPlayed;
	public Integer winCount;
	public Integer lostCount;
	
	@OneToMany(mappedBy = "playerOne", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	public List<MatchScore> matchesAsPlayerOne;

	@OneToMany(mappedBy = "playerTwo", cascade=CascadeType.ALL)
	public List<MatchScore> matchesAsPlayerTwo;
	
	
	public static Find<Long, Player> find = new Find<Long, Player>() {};
	
}