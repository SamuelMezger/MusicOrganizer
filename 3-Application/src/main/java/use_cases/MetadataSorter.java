package use_cases;

import model.metadata.Metadata;
import model.metadata.MetadataKey;
import model.metadata.MetadataField;

import java.util.*;

public class MetadataSorter {
    private final List<Metadata> foundMetadataList = new ArrayList<>();
    private int numberOfFallbacks = 0;
    private Optional<Metadata> userOverridden = Optional.empty();
    private Metadata programmsBestGuess;

    public MetadataSorter() {
        this.programmsBestGuess = new Metadata(Collections.emptyList());
    }

    public void add(Metadata metadata) {
        this.foundMetadataList.add(this.foundMetadataList.size()-this.numberOfFallbacks,  metadata);
        this.updateProgrammsBest();
    }

    public void addFallback(Metadata metadata) {
        this.foundMetadataList.add(metadata);
        this.numberOfFallbacks++;
        this.updateProgrammsBest();
    }

    public void setUserOverriddenButTestIfItsReallyFromUser(Metadata metadata) {
        if (!this.getProgrammsBestGuess().equals(metadata))
            this.setUserOverridden(metadata);
        else System.out.println("Nope");
    }

    public void setUserOverridden(Metadata metadata) {
        System.out.println("setting override");
        System.out.println(metadata);
        this.userOverridden = Optional.of(metadata);
    }

    public Metadata getCurrentChoice() {
        return this.userOverridden.orElse(this.getProgrammsBestGuess());
    }

    public List<Metadata> getAllFoundMetadata() {
        return Collections.unmodifiableList(this.foundMetadataList);
    }

    private Metadata getProgrammsBestGuess() {
        return this.programmsBestGuess;
    }

    private void updateProgrammsBest() {

        List<MetadataField> info = new ArrayList<>();
        System.out.println("updating best guess");
        this.foundMetadataList.forEach(System.out::println);

        Arrays.stream(MetadataKey.values()).forEach(key ->
                this.foundMetadataList.stream().filter(metadata -> metadata.get(key).isPresent()).findFirst()
                        .ifPresent(metadata ->
                                info.add(metadata.get(key).get()))
        );
        this.programmsBestGuess = new Metadata(info);
    }
}
