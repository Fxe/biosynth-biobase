package pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.kegg.parser;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.uminho.sysbio.biosynthframework.biodb.kegg.KeggEntity;

public class KeggGenericEntityFlatFileParser {
	
	static final public String PROP_KEY_VALUE_SEPARATOR = "\\s{2,}|\\t+";
	static final public String END_OF_FILE = "///";
	
	static public <T extends KeggEntity> T parse(Class<T> entityClass, String rawText) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		Pattern p = Pattern.compile("\\n");
		Matcher m = p.matcher(rawText);

		T entity = entityClass.getDeclaredConstructor().newInstance();
		
		m.find();
		int i=0;
		String line = rawText.substring(i, m.start()).trim();
		parseFirstRow(line, entity);
		String currProp=null;
		i = m.end();
		m.find();
		do
		{	
			line = rawText.substring(i, m.start()).trim();
			if(!line.equals("") && !line.equals(END_OF_FILE))
			{
				String[] ts = line.split(PROP_KEY_VALUE_SEPARATOR);
				if(ts.length>1)
				{
					currProp = ts[0];
					entity.addProperty(currProp, ts[1]);
				}
				else
					entity.addProperty(currProp, ts[0]);
				i=m.end();
			}
			
		}while(m.find());
		return entity;
	}
	
	static protected <T extends KeggEntity> void parseFirstRow(String line, T entity){
		String[] ts = line.split(PROP_KEY_VALUE_SEPARATOR);
		if(ts.length%2==0)
			for(int i=0; i<ts.length; i+=2)
				entity.addProperty(ts[i], ts[i+1]);
		else
			entity.addProperty(ts[0], ts[1]);
	}
	

}
