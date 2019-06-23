package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.text.AttributeSet.CharacterAttribute;

import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {

	public List<Season> listAllSeasons(Team team) {
		String sql = "SELECT Distinct s.season,s.description FROM seasons s,matches m WHERE s.season = m.Season AND (m.HomeTeam = ? OR m.AwayTeam = ?)";
		List<Season> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, team.getTeam());
			st.setString(2, team.getTeam());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Season(res.getInt("season"), res.getString("description")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Team> listTeams() {
		String sql = "SELECT team FROM teams";
		List<Team> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Team(res.getString("team")));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public void getPunti(Map<Integer, Integer> punti, Team team) {
		// TODO Auto-generated method stub
		String sql = "SELECT m.FTR as ok,m.Season as sis FROM matches m WHERE (m.HomeTeam = ? AND (m.FTR = 'H' OR m.FTR = 'D')) OR (m.AwayTeam = ? AND (m.FTR = 'A' OR m.FTR = 'D'))";
	
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, team.getTeam());
			st.setString(2, team.getTeam());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				if(res.getString("ok").charAt(0) == 'D')
					punti.replace(res.getInt("sis"),punti.get(res.getInt("sis"))+1);
				else punti.replace(res.getInt("sis"),punti.get(res.getInt("sis"))+3);
			}

			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Season> listAllSeasons() {
		String sql = "SELECT * FROM seasons";
		List<Season> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Season(res.getInt("season"), res.getString("description")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
