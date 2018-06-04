package pruts;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

public class KanEenSetNullBevatten {

	
	@Test
	public void nullErIn() {
		Set<String> eenSet=new LinkedHashSet<>();
		eenSet.add(null);
	}
}
