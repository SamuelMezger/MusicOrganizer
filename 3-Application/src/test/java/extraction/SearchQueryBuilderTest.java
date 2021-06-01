package extraction;

import org.junit.Test;
import use_cases.SearchQueryBuilder;


import static org.junit.Assert.*;


public class SearchQueryBuilderTest {

    @Test
    public void removeSpecialCharactersTest() {
        String searchTerm = new SearchQueryBuilder()
                .addSearchTerm("(ab)cd")
                .addSearchTerm("[ef]  gh")
                .addSearchTerm("【ij】kl/mn.op\"____qr'st-uv_wx+yz")
                .toString();
        assertEquals(searchTerm, "ab cd ef gh ij kl mn op qr st uv wx yz");
    }

    @Test
    public void dontRemoveOtherLanguagesTest() {
        String before = "時間を持て余しているのでは";
        String after = new SearchQueryBuilder().addSearchTerm(before).toString();
        assertEquals(before, after);
    }

    @Test
    public void dontRemoveNumbersTest() {
        String before = "12 ab 34 cd";
        String after = new SearchQueryBuilder().addSearchTerm(before).toString();
        assertEquals(before, after);
    }

}