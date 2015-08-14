package pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.kegg;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.sysbio.biosynthframework.Orientation;
import pt.uminho.sysbio.biosynthframework.biodb.kegg.KeggGeneEntity;
import pt.uminho.sysbio.biosynthframework.biodb.kegg.KeggReactionEntity;
import pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.kegg.parser.KeggGeneFlatFileParser;
import pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.kegg.parser.KeggReactionFlatFileParser;
import pt.uminho.sysbio.biosynthframework.io.ReactionDao;

public class RestKeggGenesDaoImpl
extends AbstractRestfulKeggDao  {

	public static boolean DELAY_ON_IO_ERROR = false;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RestKeggGenesDaoImpl.class);
	private static final String restRxnQuery = "http://rest.kegg.jp/get/%s";
	

	public KeggGeneEntity getGeneByEntry(String entry) {
		String restRxnQuery = String.format(RestKeggGenesDaoImpl.restRxnQuery, entry);
		
		String localPath = getPathFolder() + entry ;
//		KeggReactionEntity rxn = new KeggReactionEntity();
		
		String rnFlatFile = null;
		
		try {
			LOGGER.info(restRxnQuery);
			LOGGER.info(localPath);
			rnFlatFile = this.getLocalOrWeb(restRxnQuery, localPath + ".txt");
			
			KeggGeneFlatFileParser parser = new KeggGeneFlatFileParser(rnFlatFile);
//			rxn.setEntry(parser.getEntry());
//			rxn.setName(parser.getName());
//			rxn.setOrientation(Orientation.Reversible);
//			rxn.setComment(parser.getComment());
//			rxn.setRemark(parser.getRemark());
//			rxn.setDefinition(parser.getDefinition());
//			rxn.setEquation(parser.getEquation());
//			rxn.setEnzymes(parser.getEnzymes());
//			rxn.setPathways(parser.getPathways());
//			rxn.setRpairs(parser.getRPairs());
//			rxn.setOrthologies(parser.getOrthologies());
//			rxn.setLeft(parser.getLeft());
//			rxn.setRight(parser.getRight());
			
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			
//			LOGGER.debug(e.getStackTrace());
			return null;
		}
		return rnFlatFile;
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
