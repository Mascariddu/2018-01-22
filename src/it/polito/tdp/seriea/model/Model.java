package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {

	SerieADAO dao;
	List<Team> teams = new ArrayList<Team>();
	public List<Season> seasons = new ArrayList<Season>();
	
	private Team squadraSelezionata;
	private Map<Season, Integer> punteggi;
	
	public Model() {
		dao = new SerieADAO();
		this.teams = dao.listTeams();
		this.seasons = dao.listAllSeasons();
	}
	
	public List<Team> squadre() {
		return this.teams;
	}
	
	public Map<Season, Integer> puntiClassifica(Team squadra){
		this.squadraSelezionata = squadra;
		punteggi = new HashMap<>();
		
		for(Season season : seasons)
			dao.getPuntiClassifica(season,squadra,punteggi);
		
		return this.punteggi;
	}

	public Map<Season, Integer> getMap() {
		// TODO Auto-generated method stub
		return this.punteggi;
	}
	
}
