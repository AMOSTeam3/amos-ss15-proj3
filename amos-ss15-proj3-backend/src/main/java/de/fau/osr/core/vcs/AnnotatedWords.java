package de.fau.osr.core.vcs;

import java.util.List;

import de.fau.osr.util.FlatSource;

public class AnnotatedWords {
	/**
	 * @param source
	 * @param annotations
	 */
	public AnnotatedWords(FlatSource source, List<Object>[] annotations) {
		this.source = source;
		this.annotations = annotations;
	}
	public FlatSource source;
	public List<Object>[] annotations;
}