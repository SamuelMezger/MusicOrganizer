package extraction;

import java.util.*;
import java.util.stream.Collectors;

public class SearchQueryBuilder {
    private static final Set<String> wordsToRemove = new HashSet<>(Arrays.asList(
            "",
            "official",
            "music",
            "video",
            "mv",
            "4k",
            "hd",
            "lyric",
            "lyrics",
            "ft",
            "feat",
            "amv",
            "prod",
            "audio",
            "inst"
    ));

//    matches special characters but not numbers, letters in all languages and !?
    private static final String removeAndSplitByRegex = "[^\\p{L}\\p{Z}0-9!?]| ";


    private final Set<String> allSearchTerms = new LinkedHashSet<>();

    public SearchQueryBuilder addSearchTerm(String searchTerm) {
        this.allSearchTerms.addAll(SearchQueryBuilder.splitAtDisallowedChars(searchTerm));
        return this;
    }

    public SearchQueryBuilder addSearchTerms(List<String> searchTerms) {
        searchTerms.forEach(this::addSearchTerm);
        return this;
    }

    @Override
    public String toString() {
        List<String> cleanWords = SearchQueryBuilder.splitAtDisallowedChars(String.join(" ", this.allSearchTerms));
        return SearchQueryBuilder.removeDisallowedWordsAndJoin(cleanWords);
    }


    private static String removeDisallowedWordsAndJoin(List<String> words) {
        return words.stream().filter(s -> !wordsToRemove.contains(s)).collect(Collectors.joining(" "));
    }

    private static List<String> splitAtDisallowedChars(String unclean) {
        return Arrays.asList(unclean.split(removeAndSplitByRegex));
    }


}
