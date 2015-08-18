package pt.uminho.sysbio.biosynthframework.biodb.kegg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class KeggEntity {
	
	protected String entry;
	protected List<String> names;
	protected String definition;
	
	protected Map<String, List<String>> properties; 
	
	
	public KeggEntity(){
		properties = new HashMap<>();
	}
	

	public void addProperty(String key, String value){
		if(key.equals("ENTRY"))
			entry = value;
		else if(key.equals("NAME"))
		{
			if(names==null)
				names = new ArrayList<>();
			names.add(value);
		}
		else if(key.equals("DEFINITION"))
			definition = value;
		else
		{
			if(!properties.containsKey(key))
				properties.put(key, new ArrayList<String>());
			properties.get(key).add(value);
		}
	}
	
	public void addProperty(String key, List<String> values){
		if(key.equals("ENTRY"))
			entry = values.get(0);
		else if(key.equals("NAME"))
		{
			if(names==null)
				names = new ArrayList<>();
			names.addAll(values);
		}
		else if(key.equals("DEFINITION"))
		{
			definition = "";
			for(String s : values)
				definition += s + "\n";
		}
		else
		{
			if(!properties.containsKey(key))
				properties.put(key, new ArrayList<String>());
			properties.get(key).addAll(values);
		}
	}
	
	public List<String> removeProperty(String key){
		return properties.remove(key);
	}
	
	public boolean removePropertyValue(String key, String value){
		return properties.containsValue(key)
			? properties.get(key).remove(value)
			: false;
	}
	
	public List<String> getPropertyValues(String key){
		return properties.containsKey(key) ? properties.get(key) : null;
	}
	
	public String getPropertyFirstValue(String key){
		return properties.containsKey(key) ? properties.get(key).get(0) : null;
	}

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}


	public String getDefinition() {
		return definition;
	}


	public void setDefinition(String definition) {
		this.definition = definition;
	}


	public Map<String, List<String>> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, List<String>> properties) {
		this.properties = properties;
	}
}
