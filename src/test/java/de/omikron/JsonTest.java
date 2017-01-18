package de.omikron;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.Gson;

import de.omikron.helper.dom.SearchResult;
import de.omikron.helper.reponse.SearchResponse;

public class JsonTest {

	//TODO: add null objcts to result
	
		
	@Test
	public void testSearchResponse() {
		Gson gson = new Gson(); 
		String json = "{searchResult:{breadCrumbTrailItems:[{associatedFieldName:null,searchParams:'test',type:'search'}],timedOut:true}}";
		
		SearchResponse result = gson.fromJson(json, SearchResponse.class);
		System.out.println(result);
		assertTrue(result.getSearchResult().isTimedOut());
	}
	
	@Test
	public void testSearchResult() {
		Gson gson = new Gson(); 
		String json = "{breadCrumbTrailItems:[],timedOut:true}";
		
		SearchResult result = gson.fromJson(json, SearchResult.class);
		System.out.println(result);
		assertTrue(result.isTimedOut());
	}

}
