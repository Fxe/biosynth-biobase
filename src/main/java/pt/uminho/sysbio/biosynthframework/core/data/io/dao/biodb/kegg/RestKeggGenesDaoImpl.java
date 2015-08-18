package pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.kegg;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.sysbio.biosynthframework.Orientation;
import pt.uminho.sysbio.biosynthframework.biodb.kegg.KeggECNumberEntity;
import pt.uminho.sysbio.biosynthframework.biodb.kegg.KeggGeneEntity;
import pt.uminho.sysbio.biosynthframework.biodb.kegg.KeggReactionEntity;
import pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.kegg.parser.KeggGeneFlatFileParser;
import pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.kegg.parser.KeggGenericEntityFlatFileParser;
import pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.kegg.parser.KeggReactionFlatFileParser;
import pt.uminho.sysbio.biosynthframework.io.ReactionDao;

public class RestKeggGenesDaoImpl
extends AbstractRestfulKeggDao  {

	public static boolean DELAY_ON_IO_ERROR = false;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RestKeggGenesDaoImpl.class);
	private static final String restGeneQuery = "http://rest.kegg.jp/get/%s";
	

	public KeggGeneEntity getGeneByEntry(String entry) {
		String restGeneQuery = String.format(RestKeggGenesDaoImpl.restGeneQuery, entry);
		String localPath = getPathFolder() + entry ;
		KeggGeneEntity geneEntity = null;
		
		try {
			LOGGER.info(restGeneQuery);
			LOGGER.info(localPath);
			String rnFlatFile = this.getLocalOrWeb(restGeneQuery, localPath + ".txt");
			geneEntity = KeggGenericEntityFlatFileParser.parse(KeggGeneEntity.class, rnFlatFile);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return geneEntity;
	}


	public Set<String> getAllGenesEntries(String genome) {
		Set<String> rnIds = new HashSet<>();
		String restListRnQuery = String.format("http://rest.kegg.jp/%s/%s", "list", genome);
		String localPath = this.getLocalStorage() + "query" + String.format("/genes_%s.txt", genome);
		try {
			String httpResponseString = getLocalOrWeb(restListRnQuery, localPath);
			String[] httpResponseLine = httpResponseString.split("\n");
			for ( int i = 0; i < httpResponseLine.length; i++) {
				String[] values = httpResponseLine[i].split("\\t");
				rnIds.add(values[0]);
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return rnIds;
	}

	public String getPathFolder(){
		return this.getLocalStorage()  + "gene" + "/";
	}
	
	public void createFolder(){
		File f = new File(getPathFolder());
		f.mkdirs();
	}
	
	public static void main(String[] args) {
		RestKeggGenesDaoImpl gene = new RestKeggGenesDaoImpl();
		System.out.println(gene.getAllGenesEntries("T02080"));
		System.out.println(gene.getGeneByEntry("bls:W91_1194"));
		
	}
}
