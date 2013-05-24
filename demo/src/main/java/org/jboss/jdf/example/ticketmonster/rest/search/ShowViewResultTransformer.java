package org.jboss.jdf.example.ticketmonster.rest.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.transform.ResultTransformer;
import org.jboss.jdf.example.ticketmonster.model.Show;

public class ShowViewResultTransformer implements ResultTransformer {
	public static ShowViewResultTransformer INSTANCE = new ShowViewResultTransformer();

	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		return tuple;
	}

	@Override
	public List transformList(List collection) {
		List<ShowView> results = new ArrayList<ShowView>(collection.size());
		for(Show show : (List<Show>) collection) {
			results.add(new ShowView(show));
		}
		return results;
	}

}
