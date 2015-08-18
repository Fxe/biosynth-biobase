package edu.uminho.biosynth.core.data.io.dao.biodb.kegg;

import org.junit.BeforeClass;
import org.junit.Test;

import pt.uminho.sysbio.biosynthframework.biodb.kegg.KeggGeneEntity;
import pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.kegg.RestKeggGenesDaoImpl;

public class TestRestKeggGeneDaoImpl {
	
	protected static String folder = "/home/rafael/Documents/work/publicDatabases/Kegg/crawler";
	protected static RestKeggGenesDaoImpl rest;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		rest = new RestKeggGenesDaoImpl();
		rest.setLocalStorage(folder);
		rest.setSaveLocalStorage(true);
		rest.setUseLocalStorage(true);
		rest.createFolder();
	}
	
	@Test
	public void test1(){
		KeggGeneEntity geneEntity = rest.getGeneByEntry("eco:b0002");
		System.out.println(geneEntity.getEntry());
		System.out.println(geneEntity.getAminoacidsSeq());
		System.out.println(geneEntity.getPropertyValues("DBLINKS"));
	}

}
