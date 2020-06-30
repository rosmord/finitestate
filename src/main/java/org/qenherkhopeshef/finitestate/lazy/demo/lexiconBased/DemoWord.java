package org.qenherkhopeshef.finitestate.lazy.demo.lexiconBased;

public class DemoWord {
	private String word;
	private String PartOfSpeech;

	public DemoWord(String word, String partOfSpeech) {
		this.word = word;
		PartOfSpeech = partOfSpeech;
	}

	public String getPartOfSpeech() {
		return PartOfSpeech;
	}

	public String getWord() {
		return word;
	}

	
}
