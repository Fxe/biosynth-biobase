package pt.uminho.sysbio.biosynthframework.biodb.kegg;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeggECNumberEntity extends KeggEntity{
	
	@Override
	public void addProperty(String key, String value) {
		if(key.equals("ENTRY"))
		{
			Pattern p = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+[a-z]?|\\d+\\.\\d+\\.\\d+\\.\\-|\\d+\\.\\d+\\.[\\d+\\-].\\-|\\d+\\.[\\d+\\-]\\.\\-.\\-)");
			Matcher m = p.matcher(value);
			m.find();
			entry = m.group();
		}
		else
			super.addProperty(key, value);
	}

}
