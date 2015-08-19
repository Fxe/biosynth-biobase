package pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.kegg;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.sysbio.biosynthframework.biodb.kegg.KeggECNumberEntity;
import pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.kegg.parser.KeggGenericEntityFlatFileParser;

public class RestKeggECNumberDaoImpl
extends AbstractRestfulKeggDao {

	public static boolean DELAY_ON_IO_ERROR = false;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RestKeggECNumberDaoImpl.class);
	private static final String restRxnQuery = "http://rest.kegg.jp/get/ec:%s";

	
	public KeggECNumberEntity getECNumberByEntry(String entry) {
		String restRxnQuery = String.format(RestKeggECNumberDaoImpl.restRxnQuery, entry);
		String localPath =getPathFolder() + entry ;
		KeggECNumberEntity ec = null;
		
		try {
			LOGGER.info(restRxnQuery);
			LOGGER.info(localPath);
			String rnFlatFile = this.getLocalOrWeb(restRxnQuery, localPath  +".txt");
			ec = KeggGenericEntityFlatFileParser.parse(KeggECNumberEntity.class, rnFlatFile);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return ec;
	}

	public Set<String> getAllEntries() {
		Set<String> rnIds = new HashSet<>();
		String restListRnQuery = String.format("http://rest.kegg.jp/%s/%s", "list", "ec");
		String localPath = this.getLocalStorage() + "query" + "/ecnumbers.txt";
		try {
			String httpResponseString = getLocalOrWeb(restListRnQuery, localPath);
			String[] httpResponseLine = httpResponseString.split("\n");
			for ( int i = 0; i < httpResponseLine.length; i++) {
				String[] values = httpResponseLine[i].split("\\t");
				rnIds.add(values[0].substring(3));
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return rnIds;
	}
	
	public String getPathFolder(){
		return this.getLocalStorage()  + "ec" + "/";
	}
	
	public void createFolder(){
		File f = new File(getPathFolder());
		f.mkdirs();
	}
	

}
