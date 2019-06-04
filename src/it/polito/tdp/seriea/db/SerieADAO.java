package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {

	public List<Season> listAllSeasons() {
		String sql = "SELECT season, description FROM seasons";
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

	public void getPuntiClassifica(Season s, Team t, Map<Season, Integer> map) {
		// TODO Auto-generated method stub
		String sql = "SELECT m.Season as s,m.HomeTeam,m.AwayTeam,m.FTR as r FROM matches m WHERE m.Season =? AND ((m.HomeTeam = ? AND (m.FTR = 'H' OR m.FTR = 'D')) OR (m.AwayTeam = ? AND (m.FTR = 'A' OR m.FTR = 'D' )))";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, s.getSeason());
			st.setString(2, t.toString());
			st.setString(3, t.toString());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				
				System.out.println("entrati");
				if(!map.containsKey(s))
					map.put(s, 0);
				
				if(!res.getString("r").equals("D"))
					map.replace(s, map.get(s)+3);
				else map.replace(s, map.get(s)+1);
			}

			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
	}

}
