package pt.uminho.sysbio.biosynthframework.biodb.kegg;

import java.util.List;

public class KeggGeneEntity extends KeggEntity{

	protected String nucleotidesSeq;
	protected String aminoacidsSeq;
	
	
	@Override
	public void addProperty(String key, String value) {
		System.out.println(">>>>>>>>>" + key + "<<<");
		if(key.equals("AASEQ"))
		{
			if(!value.matches("[0-9]+")) // ignoring first row with the symbols counting
			{
				if(aminoacidsSeq==null)
					aminoacidsSeq = value;
				else
					aminoacidsSeq += value;
			}
		}
		else if(key.equals("NTSEQ"))
		{
			if(!value.matches("[0-9]+")) // ignoring first row with the symbols counting
			{
				if(nucleotidesSeq==null)
					nucleotidesSeq = value;
				else
					nucleotidesSeq += value;
			}
		}
		else
			super.addProperty(key, value);
	}
	
	@Override
	public void addProperty(String key, List<String> values) {
		
		if(key.equals("AASEQ"))
		{
			aminoacidsSeq = values.get(1); // ignoring first row with the symbols counting
			for(int i=2; i<values.size(); i++)
				aminoacidsSeq += values.get(i);
		}
		else if(key.equals("NTSEQ"))
		{
			nucleotidesSeq = values.get(1); // ignoring first row with the symbols counting
			for(int i=2; i<values.size(); i++)
				nucleotidesSeq += values.get(i);
		}
		else
			super.addProperty(key, values);
	}
	

	public String getNucleotidesSeq() {
		return nucleotidesSeq;
	}
	public void setNucleotidesSeq(String nucleotidesSeq) {
		this.nucleotidesSeq = nucleotidesSeq;
	}
	public String getAminoacidsSeq() {
		return aminoacidsSeq;
	}
	public void setAminoacidsSeq(String aminoacidsSeq) {
		this.aminoacidsSeq = aminoacidsSeq;
	}
}
