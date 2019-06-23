package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.mysql.cj.xdevapi.Result;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	
	SerieADAO dao;
	List<Team> squadre;
	Map<Integer, Integer> punti;
	List<Season> vertex;
	SimpleDirectedWeightedGraph<Season, DefaultWeightedEdge> grafo;
	List<Season> best;
	
	public Model() {
		
		this.dao = new SerieADAO();
		squadre = new ArrayList<Team>(dao.listTeams());
		
	}

	public List<Team> getSquadre() {
		// TODO Auto-generated method stub
		return squadre;
	}

	public List<String> getPunti(Team team) {
		// TODO Auto-generated method stub
		punti = new HashMap<Integer, Integer>();
		for(Season season : dao.listAllSeasons())
			punti.put(season.getSeason(), 0);
		List<String> result = new ArrayList<String>();
		dao.getPunti(punti,team);
		List<Integer> stagioni = new ArrayList<Integer>(punti.keySet());
		Collections.sort(stagioni);
		
		for(Integer integer : stagioni)
			result.add("Stagione: "+integer+" punti: "+punti.get(integer));
		
		return result;
	}

	public void creaGrafo(Team team) {
		
		grafo = new SimpleDirectedWeightedGraph<Season, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		vertex = new ArrayList<Season>();
		vertex = dao.listAllSeasons(team);
		Graphs.addAllVertices(grafo, vertex);
		
		for(Season season : vertex) {
			
			for(Season s: vertex) {
				
				if(!season.equals(s)) {
					
					if(punti.get(s.getSeason()) < punti.get(season.getSeason())) {
						
						Season source = s;
						Season target = season;
						double peso = punti.get(season.getSeason()) - punti.get(s.getSeason());
						
						Graphs.addEdge(grafo, source, target, peso);
						System.out.println("Aggiunta arco tra : "+s.getSeason()+" e "+season.getSeason()+" con peso: "+peso);
						
					}
						
					
				}
			}
		}
		
		System.out.println("#vertici: "+grafo.vertexSet().size());
		System.out.println("#archi: "+grafo.edgeSet().size());
		
	}

	public String annataDoro() {
		// TODO Auto-generated method stub
		String best = "";
		int max = 0;
		
		for(Season season : grafo.vertexSet()) {
			int tot = 0;
			
			for(DefaultWeightedEdge edge : grafo.outgoingEdgesOf(season))
				tot -= grafo.getEdgeWeight(edge);
			
			for(DefaultWeightedEdge edge : grafo.incomingEdgesOf(season))
				tot += grafo.getEdgeWeight(edge);
			
			if(tot > max) {
				
				max = tot;
				best = season.toString()+" con peso: "+tot;
			}
		}
		
		if(!best.equals(""))
			return best;
		else {
			for(Season season : grafo.vertexSet())
				best = season.toString()+" con peso 0"; 
			return best;
		}
	}

	public List<Season> trovaCammino() {
		// TODO Auto-generated method stub
		best = new ArrayList<Season>();
		List<Season> parziale = new ArrayList<Season>();
		Collections.sort(vertex);
		
		cerca(parziale);
		
		return best;
	}

	private void cerca(List<Season> parziale) {
		// TODO Auto-generated method stub
		if(parziale.size() > best.size()) {
			best = new ArrayList<Season>(parziale);
			System.out.println("NEW BEST");
		}
		
		for(Season season : grafo.vertexSet()) {
			
			if(controlla(parziale,season) && !parziale.contains(season)) {
				
				parziale.add(season);
				System.out.println("AGGIUNGI");
				System.out.println("CERCA");
				cerca(parziale);
				parziale.remove(parziale.size()-1);
				
			} 
			
			/*else {
				System.out.println("ESCI");
				return;
			}*/
			
		}
	}

	private boolean controlla(List<Season> parziale, Season aggiunta) {
		// TODO Auto-generated method stub
		int i = 0;
		boolean flag = false;
		System.out.println("CONTROLLA");
		
		for(Season season : parziale) {
			
			if(i == parziale.size()-1) {
				
				int prima = vertex.indexOf(season);
				int dopo = vertex.indexOf(aggiunta);
				System.out.println(prima+" e "+dopo);
				DefaultWeightedEdge edge = grafo.getEdge(season, aggiunta);
				if(edge != null && dopo == prima+1)
					flag = true;
				
			}
			i++;
		}
		
		if(parziale.size() == 0)
			flag = true;
		
		return flag;
	}

	public int getPoint(Season season) {
		// TODO Auto-generated method stub
		return punti.get(season.getSeason());
	}
	
}
