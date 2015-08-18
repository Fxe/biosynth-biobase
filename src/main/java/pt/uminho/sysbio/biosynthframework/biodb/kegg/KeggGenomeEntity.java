package pt.uminho.sysbio.biosynthframework.biodb.kegg;

public class KeggGenomeEntity extends KeggEntity{

//	protected String entry;
//	protected String name;
//	protected String definition;
//	protected String dataSource;
//	protected String keywords;
//	protected String disease;
//	protected String comment;
//	protected String chromosome;
//	protected String sequence;
//	protected Integer length;
//	protected String reference;
//	protected String authors;
//	protected String title;
//	protected String jornal;
//	protected String annotation;
	
	protected String lineage;
	protected String taxonomy;
	
	
	
	public String getLineage() {
		return lineage;
	}
	public void setLineage(String lineage) {
		this.lineage = lineage;
	}
	public String getTaxonomy() {
		return taxonomy;
	}
	public void setTaxonomy(String taxonomy) {
		this.taxonomy = taxonomy;
	}
	
	@Override
	public void addProperty(String key, String value) {
		if(key.equals("LINEAGE"))
			lineage = value;
		else if(key.equals("TAXONOMY"))
			taxonomy = value;
		else
			super.addProperty(key, value);
	}
	
//	public String getEntry() {
//		return entry;
//	}
//	public void setEntry(String entry) {
//		this.entry = entry;
//	}
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
//	public String getDefinition() {
//		return definition;
//	}
//	public void setDefinition(String definition) {
//		this.definition = definition;
//	}
//	public String getLinhage() {
//		return linhage;
//	}
//	public void setLinhage(String linhage) {
//		this.linhage = linhage;
//	}
//	public String getDataSource() {
//		return dataSource;
//	}
//	public void setDataSource(String dataSource) {
//		this.dataSource = dataSource;
//	}
//	public String getKeywords() {
//		return keywords;
//	}
//	public void setKeywords(String keywords) {
//		this.keywords = keywords;
//	}
//	public String getDisease() {
//		return disease;
//	}
//	public void setDisease(String disease) {
//		this.disease = disease;
//	}
//	public String getComment() {
//		return comment;
//	}
//	public void setComment(String comment) {
//		this.comment = comment;
//	}
//	public String getChromosome() {
//		return chromosome;
//	}
//	public void setChromosome(String chromosome) {
//		this.chromosome = chromosome;
//	}
//	public String getSequence() {
//		return sequence;
//	}
//	public void setSequence(String sequence) {
//		this.sequence = sequence;
//	}
//	public Integer getLength() {
//		return length;
//	}
//	public void setLength(Integer length) {
//		this.length = length;
//	}
//	public String getReference() {
//		return reference;
//	}
//	public void setReference(String reference) {
//		this.reference = reference;
//	}
//	public String getAuthors() {
//		return authors;
//	}
//	public void setAuthors(String authors) {
//		this.authors = authors;
//	}
//	public String getTitle() {
//		return title;
//	}
//	public void setTitle(String title) {
//		this.title = title;
//	}
//	public String getJornal() {
//		return jornal;
//	}
//	public void setJornal(String jornal) {
//		this.jornal = jornal;
//	}
//	
//	public String getAnnotation() {
//		return annotation;
//	}
//	
//	public void setAnnotation(String annotation) {
//		this.annotation = annotation;
//	}
}
