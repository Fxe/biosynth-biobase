package pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.kegg.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.uminho.sysbio.biosynthframework.biodb.kegg.KeggReactionLeftEntity;
import pt.uminho.sysbio.biosynthframework.biodb.kegg.KeggReactionRightEntity;
import pt.uminho.sysbio.biosynthframework.util.BioSynthUtils;


public class KeggModuleFlatFileParser extends AbstractKeggFlatFileParser {

//	KEGG REACTION
//
//	Web page      	Flat file
//	Entry	ENTRY X X
//	Name	NAME ?
//	Definition	DEFINITION X X
//	Equation	EQUATION X X
//	(blank)	(gif image)
//	Remark	REMARK X X
//	Comment	COMMENT X X
//	RPair	RPAIR X
//	Enzyme	ENZYME X
//	Pathway	PATHWAY X
//	Orthology	ORTHOLOGY
//	Reference	REFERENCE

	private List<KeggReactionLeftEntity> leftEntities = new ArrayList<> ();
	private List<KeggReactionRightEntity> rightEntities = new ArrayList<> ();
	
	public KeggModuleFlatFileParser(String flatfile) {
		super(flatfile);
		
		this.parseContent();
	}

	public String getEntry() {
		int tabIndex = this.getTabIndex("ENTRY");
		String content = this.tabContent_.get(tabIndex);
		
		return content;
	}
	
	public String getName() {
		int index = this.getTabIndex("NAME");
		String value = this.getContent(index);
		return value;
	}
	
	public String getEquation() {
		int index = this.getTabIndex("EQUATION");
		if (index < 0) return null;
		String value = this.getContent(index);
		return value;
	}
	
	public String getDefinition() {
		int index = this.getTabIndex("DEFINITION");
		if (index < 0) return null; 
		String value = this.getContent(index);
		return value;
	}
	
	public String getComment() {
		int tabIndex = this.getTabIndex("COMMENT");
		String content = this.tabContent_.get(tabIndex);
		return content;
	}

	public String getRemark() {
		int tabIndex = this.getTabIndex("REMARK");
		String content = this.tabContent_.get(tabIndex);
		return content;
	}
	
	public List<String> getEnzymes() {
		List<String> enzymeIdSet = new ArrayList<String> ();
		int tabIndex = this.getTabIndex("ENZYME");
		
		String content = this.tabContent_.get(tabIndex);
		
		if ( content == null || content.isEmpty()) return enzymeIdSet;
		
		String[] enzymeIdArray = content.split("[\\s+]");
		for (int i = 0; i < enzymeIdArray.length; i++) {
			if ( !enzymeIdArray[i].isEmpty()) enzymeIdSet.add(enzymeIdArray[i]);
		}
		
		return enzymeIdSet;
	}
	
	public List<String> getRPairs() {
		int tabIndex = this.getTabIndex("RPAIR");
		String content = this.tabContent_.get(tabIndex);
		List<String> rpairList = new ArrayList<> ();
		if ( content == null || content.isEmpty()) return rpairList;
		Pattern rpairIdPattern = Pattern.compile( "([rpRP]+[0-9]+)");
		Matcher matcher = rpairIdPattern.matcher( content);
		while ( matcher.find()) {
			rpairList.add( matcher.group(1));
		}
		
		return rpairList;
	}
	
	public List<String> getPathways() {
		int tabIndex = this.getTabIndex("PATHWAY");
		String content = this.tabContent_.get(tabIndex);
		List<String> pathwayList = new ArrayList<> ();
		if ( content == null || content.isEmpty()) return pathwayList;
		Pattern pathwayIdPattern = Pattern.compile( "([a-zA-Z]+[0-9]+)");
		Matcher matcher = pathwayIdPattern.matcher( content);
		while ( matcher.find()) {
			pathwayList.add( matcher.group(1));
		}
		
		return pathwayList;
	}
	
	public List<String> getOrthologies() {
		int tabIndex = this.getTabIndex("ORTHOLOGY");
		String content = this.tabContent_.get(tabIndex);
		List<String> orthologyList = new ArrayList<> ();
		if ( content == null || content.isEmpty()) return orthologyList;
		Pattern orthologyIdPattern = Pattern.compile( "([a-zA-Z]+[0-9]+)");
		Matcher matcher = orthologyIdPattern.matcher( content);
		while ( matcher.find()) {
			orthologyList.add( matcher.group(1));
		}
		
		return orthologyList;
	}
	
	public List<KeggReactionLeftEntity> getLeft() {
		List<KeggReactionLeftEntity> leftEntities = new ArrayList<> ();
		
		if (this.leftEntities.isEmpty()) this.parserEquation();
		
		leftEntities.addAll(this.leftEntities);
		
		return leftEntities;
	}
	
	public List<KeggReactionRightEntity> getRight() {
		List<KeggReactionRightEntity> rightEntities = new ArrayList<> ();
		
		if (this.rightEntities.isEmpty()) this.parserEquation();
		
		rightEntities.addAll(this.rightEntities);
		
		return rightEntities;
	}
	
	private void parserEquation() {
		leftEntities.clear();
		rightEntities.clear();
		
		String equation = this.getEquation();
		if (equation == null || equation.trim().isEmpty()) return;
		
		equation = equation.trim();
		
		EquationParser equationParser = new EquationParser(equation);
		for (String[] left : equationParser.getLeftTriplet()) {
			KeggReactionLeftEntity leftEntity = new KeggReactionLeftEntity();
			leftEntity.setCpdEntry(left[0]);;
			leftEntity.setStoichiometry(Double.valueOf(left[1]));
			leftEntity.setCoefficient(left[2]);
			
			leftEntities.add(leftEntity);
		}
		
		for (String[] right : equationParser.getRightTriplet()) {
			KeggReactionRightEntity rightEntity = new KeggReactionRightEntity();
			rightEntity.setCpdEntry(right[0]);
			rightEntity.setStoichiometry(Double.valueOf(right[1]));
			rightEntity.setCoefficient(right[2]);
			
			rightEntities.add(rightEntity);
		}
	}
	
	public class EquationParser {
		
		public static final boolean VERBOSE = false;
		
		public static final int LEFT_TO_RIGHT = 1;
		public static final int RIGHT_TO_LEFT = -1;
		public static final int BIDIRECTIONAL = 0;
		
		public static final String LEFT = "LEFT";
		public static final String RIGHT = "RIGHT";
		
		private boolean variable = false;
		private Map<String, Double> leftBasic = new HashMap<> ();
		private Map<String, String> leftVariable = new HashMap<> ();
		private Map<String, Double> rightBasic = new HashMap<> ();
		private Map<String, String> rightVariable = new HashMap<> ();
		
		private String equation;
		
		public EquationParser(String eq) {
			this.equation = eq.replaceAll("\\s+", " ");
			this.parseEquation(equation);
			variable = !(leftVariable.isEmpty() && rightVariable.isEmpty());
		}
		
		
		
		public String getEquation() { return equation;}

		public Set<String> getLeft() {
			Set<String> ret = new HashSet<String> ();
			ret.addAll( leftBasic.keySet());
			ret.addAll( leftVariable.keySet());
			return ret;
		}
		
		public Set<String> getRight() {
			Set<String> ret = new HashSet<String> ();
			ret.addAll( rightBasic.keySet());
			ret.addAll( rightVariable.keySet());
			return ret;
		}
		
		private boolean isSingleton(String[] eqPart) {
			return (eqPart.length == 1);
		}
		
		private void parseExpression(String expression, Map<String, Double> basic, Map<String, String> variable) throws IllegalArgumentException {
			String[] split = expression.split(" \\+ ");
			if (VERBOSE) System.out.println( Arrays.toString(split));
			for (int i = 0; i < split.length; i++) {
				String specie;
				String value;
				String block = split[i];
				block = block.replaceAll("\\)|\\(", " ").trim();;
				if (VERBOSE) System.out.println("i: #" + block + "#");
				block = block.replaceAll("\\s+", " ").trim();
				String blockParts[] = block.split(" ");
				if (blockParts.length > 2) {
					System.err.println( "ERROR LENGTH " + Arrays.toString(blockParts) + " " + this.equation);
					throw new IllegalArgumentException("ERROR LENGTH " + Arrays.toString(blockParts) + " " + this.equation);
				}
				if ( isSingleton(blockParts)) {
					value = "1";
					specie = blockParts[0];
				} else if ( Character.isUpperCase( blockParts[0].charAt(0))) {
					specie = blockParts[0];
					value = blockParts[1];
				} else {
					specie = blockParts[1];
					value = blockParts[0];
				}
				
				if (VERBOSE) System.out.println( "SPECIE: " + specie + " VAL: " + value);
				if ( BioSynthUtils.isNumeric(value)) {
					basic.put(specie, Double.parseDouble(value));
				} else {
					if ( variable.containsKey(specie)) {
						variable.put(specie, variable.get(specie) + "+" + value);
					} else {
						variable.put(specie, value);
					}
				}
			}
		}
		
		private void parseEquation(String eq) {

			String[] eqLeftRightSplit = eq.split("=");
			String eqLeft = eqLeftRightSplit[0];
			
			eqLeft = eqLeft.replaceAll("<", "").trim();
			parseExpression(eqLeft, leftBasic, leftVariable);
			
			String eqRight = eqLeftRightSplit[1];
			eqRight = eqRight.replaceAll(">", "").trim();
			parseExpression(eqRight, rightBasic, rightVariable);
			
			/*
			split = eqLeft.split(" \\+ ");
			for (String s : split) {
				String trimS = s.trim();
				String[] eqSplit = trimS.split(" ");
				if ( eqSplit.length > 2) System.err.println("BAD SPLIT LENGTH > 2 " + Arrays.toString(eqSplit));
				if ( isSingleton(eqSplit)) {
					leftBasic.put(trimS, 1d);
					System.out.println("[" + 1.0d + "]" + " " + trimS);
				} else if ( BioSynthUtilsIO.isNumeric(eqSplit[0])) {
					leftBasic.put(eqSplit[1], Double.parseDouble(eqSplit[0]));
					System.out.println("[" + Double.parseDouble(eqSplit[0]) + "]" + " " + eqSplit[1]);
				} else {
					eqSplit[0] = eqSplit[0].replaceAll("\\)|\\(", "");
					leftGeneric.put(eqSplit[1], eqSplit[0]);
					System.out.println("#" + eqSplit[0] + "#");
				}
				
			}*/

			
			/*
			System.out.println("RIGHT:");
			split = eqRight.split(" \\+ ");

			for (String s : split) {
				String trimS = s.trim();
				String[] eqSplit = trimS.split(" ");
				if ( eqSplit.length > 2) System.err.println("BAD SPLIT LENGTH > 2 " + Arrays.toString(eqSplit));
				if ( isSingleton(eqSplit)) {
					rightBasic.put(trimS, 1d);
					System.out.println("[" + 1.0d + "]" + " " + trimS);
				} else if ( BioSynthUtilsIO.isNumeric(eqSplit[0])) {
					rightBasic.put(eqSplit[1], Double.parseDouble(eqSplit[0]));
					System.out.println("[" + Double.parseDouble(eqSplit[0]) + "]" + " " + eqSplit[1]);
				} else {
					eqSplit[0] = eqSplit[0].replaceAll("\\)|\\(", "");
					rightGeneric.put(eqSplit[1], eqSplit[0]);
					System.out.println("#" + eqSplit[0] + "#");
				}
			}
			*/
		}
		
		public String[][] getLeftTriplet() {
			//TODO: parse generic values
			int size = leftBasic.size() + leftVariable.size();
			String[][] ret = new String[size][3];
			int ptr = 0;
			for ( String cpdId : leftBasic.keySet()) {
				ret[ptr][0] = cpdId;
				ret[ptr][1] = leftBasic.get(cpdId).toString();
				ret[ptr][2] = ret[ptr][1];
				ptr++;
			}
			for ( String cpdId : leftVariable.keySet()) {
				ret[ptr][0] = cpdId;
				ret[ptr][1] = "1";
				ret[ptr][2] = leftVariable.get(cpdId).toString();
				ptr++;
			}
			return ret;
		}
		
		public String[][] getRightTriplet() {
			//TODO: parse generic values
			int size = rightBasic.size() + rightVariable.size();
			String[][] ret = new String[size][3];
			int ptr = 0;
			for ( String cpdId : rightBasic.keySet()) {
				ret[ptr][0] = cpdId;
				ret[ptr][1] = rightBasic.get(cpdId).toString();
				ret[ptr][2] = ret[ptr][1];
				ptr++;
			}
			for ( String cpdId : rightVariable.keySet()) {
				ret[ptr][0] = cpdId;
				ret[ptr][1] = "1";
				ret[ptr][2] = rightVariable.get(cpdId).toString();
				ptr++;
			}
			return ret;
		}
		
		public boolean isVariable() {
			return this.variable;
		}
		
		public boolean isEquivalent(String exp1, String exp2) {
			return exp1.equals(exp2);
		}
		
		public boolean isFeasible() {
			Map<String, String> left = new HashMap<String, String> ();
			Map<String, String> right = new HashMap<String, String> ();
			
			for ( String k : leftBasic.keySet()) {
				left.put(k, leftBasic.get(k).toString());
			}
			for ( String k : leftVariable.keySet()) {
				if ( left.containsKey(k)) {
					left.put(k, left.get(k) + "+" + leftVariable.get(k).toString());
				} else {
					left.put(k, leftVariable.get(k).toString());
				}
			}
			
			for ( String k : rightBasic.keySet()) {
				right.put(k, rightBasic.get(k).toString());
			}
			for ( String k : rightVariable.keySet()) {
				if ( right.containsKey(k)) {
					right.put(k, right.get(k) + "+" + rightVariable.get(k).toString());
				} else {
					right.put(k, rightVariable.get(k).toString());
				}
			}
			
			//System.out.println( left);
			//System.out.println( right);
			
			for (String entry : left.keySet()) {
				if ( right.containsKey(entry)) {
					if ( isEquivalent(left.get(entry), right.get(entry))) {
						return false;
					}
				}
			}
			return true;
		}
		
		public void reduceEquationStoich() {
			//TODO: !!!!!!!
			double[][] stoich = new double[2][];
			int leftSize = this.leftBasic.size() + this.leftVariable.size();
			int rightSize = this.rightBasic.size() + this.rightVariable.size();
			stoich[0] = new double[ leftSize];
			stoich[1] = new double[ rightSize];
			
//			int ptr;
//			
//			ptr = 0;
			for ( int i = 0; i < stoich[0].length; i++) {
				
			}
			//n A <=> n-1 A + C
			//n Q + m K <=> n+m K
			Map<String, String> variables = new HashMap<String, String> ();
			for ( String leftVar : this.leftVariable.keySet()) {
				System.out.println( leftVar);
				String a = leftVariable.get(leftVar);
				String[] vars = a.split("[^a-z]");
				String[] values = a.split("[a-z]");
				System.out.println( Arrays.toString(vars));
				System.out.println( Arrays.toString(values));
				for ( int i = 0; i < vars.length; i++) {
					variables.put( vars[i], null);
				}
			}
			
			for ( String rightVar : this.rightVariable.keySet()) {
				System.out.println( rightVar);
				String a = rightVariable.get(rightVar);
				System.out.println( Arrays.toString(a.split("[a-z]")));
				System.out.println( Arrays.toString(a.split("[^a-z]")));
			}
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append( variable ? "Variable Equation" : "Basic Equation");
			sb.append(leftBasic).append(leftVariable);
			sb.append(rightBasic).append(rightVariable);
			return sb.toString();
		}
	}
}
