package extraction;

import org.junit.Test;


import static org.junit.Assert.*;


public class SearchTermBuilderTest {

    @Test
    public void removeSpecialCharactersTest() {
        String searchTerm = new SearchTermBuilder()
                .add("(ab)cd")
                .add("[ef]  gh")
                .add("【ij】kl/mn.op\"____qr'st-uv_wx+yz")
                .toString();
        assertEquals(searchTerm, "ab cd ef gh ij kl mn op qr st uv wx yz");
    }

    @Test
    public void removeSpecialCharactersButNotOtherLanguagesTest() {
        String before = "時間を持て余しているのでは";
        String after = new SearchTermBuilder().add(before).toString();
        assertEquals(after, before);
    }

    @Test
    public void removeSpecialCharactersButNotNumbersTest() {
        String before = "12 ab 34 cd";
        String after = new SearchTermBuilder().add(before).toString();
        assertEquals(after, before);
    }

}