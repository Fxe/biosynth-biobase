package pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.kegg;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.sysbio.biosynthframework.biodb.kegg.KeggKOEntity;
import pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.kegg.parser.KeggGenericEntityFlatFileParser;

public class RestKeggKOsDaoImpl
extends AbstractRestfulKeggDao {

	public static boolean DELAY_ON_IO_ERROR = false;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RestKeggKOsDaoImpl.class);
	private static final String restRxnQuery = "http://rest.kegg.jp/get/ko:%s";
	
	

	public KeggKOEntity getKOByEntry(String entry) {
		String restRxnQuery = String.format(RestKeggKOsDaoImpl.restRxnQuery, entry);
		String localPath = getPathFolder() + entry ;
		KeggKOEntity ko = null;
		
		try {
			LOGGER.info(restRxnQuery);
			LOGGER.info(localPath);
			String koFlatFile = this.getLocalOrWeb(restRxnQuery, localPath + entry +".txt");
			ko = KeggGenericEntityFlatFileParser.parse(KeggKOEntity.class, koFlatFile);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return ko;
	}

	public Set<String> getAllKOEntries() {
		Set<String> rnIds = new HashSet<>();
		String restListRnQuery = String.format("http://rest.kegg.jp/%s/%s", "list", "ko");
		String localPath = this.getLocalStorage() + "query" + "/kos.txt";
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
		return this.getLocalStorage()  + "kos" + "/";
	}
	
	public void createFolder(){
		File f = new File(getPathFolder());
		f.mkdirs();
	}
	
	public static void main(String[] args) {
		RestKeggKOsDaoImpl k = new RestKeggKOsDaoImpl();
//		System.out.println(k.getAllKOEntries());
		System.out.println(k.getKOByEntry("K09053"));
	}
}
