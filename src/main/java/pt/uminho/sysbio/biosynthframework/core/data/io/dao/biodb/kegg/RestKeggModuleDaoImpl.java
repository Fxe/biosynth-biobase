package pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.kegg;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.sysbio.biosynthframework.biodb.kegg.KeggModuleEntity;
import pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.kegg.parser.KeggGenericEntityFlatFileParser;

public class RestKeggModuleDaoImpl
extends AbstractRestfulKeggDao {

	public static boolean DELAY_ON_IO_ERROR = false;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RestKeggModuleDaoImpl.class);
	private static final String restRxnQuery = "http://rest.kegg.jp/get/md:%s";
	
	

	public KeggModuleEntity getModuleByEntry(String entry) {
		String restRxnQuery = String.format(RestKeggModuleDaoImpl.restRxnQuery, entry);
		String localPath = getPathFolder() + entry ;
		KeggModuleEntity module = null;
		
		try {
			LOGGER.info(restRxnQuery);
			LOGGER.info(localPath);
			String moduleFlatFile = this.getLocalOrWeb(restRxnQuery, localPath + ".txt");
			module = KeggGenericEntityFlatFileParser.parse(KeggModuleEntity.class, moduleFlatFile);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return module;
	}

	public Set<String> getAllModuleEntries() {
		Set<String> rnIds = new HashSet<>();
		String restListRnQuery = String.format("http://rest.kegg.jp/%s/%s", "list", "md");
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
		RestKeggModuleDaoImpl k = new RestKeggModuleDaoImpl();
//		System.out.println(k.getAllModuleEntries());
		System.out.println(k.getModuleByEntry("K09053"));
	}
}
