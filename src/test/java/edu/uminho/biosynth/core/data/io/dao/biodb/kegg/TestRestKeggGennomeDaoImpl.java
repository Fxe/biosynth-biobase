package edu.uminho.biosynth.core.data.io.dao.biodb.kegg;

import org.junit.BeforeClass;
import org.junit.Test;

import pt.uminho.sysbio.biosynthframework.biodb.kegg.KeggGenomeEntity;
import pt.uminho.sysbio.biosynthframework.biodb.kegg.KeggGenomeEntity;
import pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.kegg.RestKeggGenomeDaoImpl;

public class TestRestKeggGennomeDaoImpl {
	
	protected static String folder = "/home/rafael/Documents/work/publicDatabases/Kegg/crawler";
	protected static RestKeggGenomeDaoImpl rest;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		rest = new RestKeggGenomeDaoImpl();
		rest.setLocalStorage(folder);
		rest.setSaveLocalStorage(true);
		rest.setUseLocalStorage(true);
		rest.createFolder();
	}
	
	@Test
	public void test1(){
		KeggGenomeEntity genomeEntity = rest.getGenomeByEntry("T00007");
		System.out.println(genomeEntity.getEntry());
		System.out.println(genomeEntity.getLineage());
		System.out.println(genomeEntity.getTaxonomy());
		System.out.println(genomeEntity.getPropertyValues("SEQUENCE"));
	}

}
