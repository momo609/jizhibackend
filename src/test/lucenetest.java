package test;

import static org.junit.Assert.*;

import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.junit.Test;

import util.LuceneUtil;

public class lucenetest {

	@Test
	public void test() throws InvalidTokenOffsetsException {
		new LuceneUtil(LuceneUtil.pIndexpath).searchByTerm("title", "”¢”Ô", 10);
	}

}
