package edu.uminho.biosynth.core.data.io.dao.biodb.kegg;

import org.junit.BeforeClass;
import org.junit.Test;

import pt.uminho.sysbio.biosynthframework.biodb.kegg.KeggECNumberEntity;
import pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.kegg.RestKeggECNumberDaoImpl;

public class TestRestKeggEcNumberDaoImpl {
	
	protected static String folder = "/home/rafael/Documents/work/publicDatabases/Kegg/crawler";
	protected static RestKeggECNumberDaoImpl rest;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		rest = new RestKeggECNumberDaoImpl();
		rest.setLocalStorage(folder);
		rest.setSaveLocalStorage(true);
		rest.setUseLocalStorage(true);
		rest.createFolder();
	}
	
	@Test
	public void test1(){
		KeggECNumberEntity ecEntity = rest.getECNumberByEntry("2.4.1.44");
		System.out.println(ecEntity.getEntry());
		System.out.println(ecEntity.getPropertyValues("GENES"));
	}

}
