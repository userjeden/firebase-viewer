package pl.userjeden.firebaseviewer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StationReading {

	private Map<String, Map<String, String>> readingData;
	private Set<Domain> readingDomains;


	public Set<Domain> prepareDomains(){
		Set<String> names = new HashSet<String>();
		readingDomains = new HashSet<Domain>();
		for(Map<String, String> map : readingData.values()){
			names.addAll(map.keySet());
		}

		for(String domainValue : names){
			Domain domain = new Domain();
			domain.setName(domainValue);
			readingDomains.add(domain);
		}

		return readingDomains;
	}

	public void printDomains(){
		for(Domain d : readingDomains){
			System.out.println(d.getName());
		}
	}


	public List<String> sortByTime(){
		List<String> keys = new ArrayList<String>();
		keys.addAll(readingData.keySet());
		Collections.sort(keys);
		return keys;
	}

	public Set<String> filterDomains(){
		Set<String> domains = new HashSet<String>();
		for(Map<String, String> map : readingData.values()){
			domains.addAll(map.keySet());
		}
		return domains;
	}



	public void printMap(){
		List<String> keys = sortByTime();
		for(String l : keys){
			System.out.print(l.toString() + ":");
			Map<String, String> map = readingData.get(l);
			for(String key : map.keySet()){
				System.out.print(" (" + key + " : ");
				System.out.print(map.get(key) + " ),");
			}
			System.out.print("\n");
		}
	}

}
