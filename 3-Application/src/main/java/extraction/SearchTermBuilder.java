package extraction;

import java.util.*;
import java.util.stream.Collectors;

public class SearchTermBuilder {
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

    public SearchTermBuilder add(String searchTerm) {
        this.allSearchTerms.addAll(SearchTermBuilder.splitAtDisallowedChars(searchTerm));
        return this;
    }

    public SearchTermBuilder add(List<String> searchTerms) {
        searchTerms.forEach(this::add);
        return this;
    }

    @Override
    public String toString() {
        List<String> cleanWords = SearchTermBuilder.splitAtDisallowedChars(String.join(" ", this.allSearchTerms));
        return SearchTermBuilder.removeDisallowedWordsAndJoin(cleanWords);
    }


    private static String removeDisallowedWordsAndJoin(List<String> words) {
        return words.stream().filter(s -> !wordsToRemove.contains(s)).collect(Collectors.joining(" "));
    }

    private static List<String> splitAtDisallowedChars(String unclean) {
        return Arrays.asList(unclean.split(removeAndSplitByRegex));
    }


}
