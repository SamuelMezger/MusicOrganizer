package repository;

import model.metadata.Metadata;
import model.metadata.MetadataKey;
import model.metadata.MetadataField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MetadataSorter {
    private final List<Metadata> foundMetadataList = new ArrayList<>();
    private int numberNonOfFallbacks = 0;
    private Optional<Metadata> userOverridden;

    public void add(Metadata metadata) {
        this.foundMetadataList.add(this.foundMetadataList.size()-this.numberNonOfFallbacks,  metadata);
    }

    public void addFallback(Metadata metadata) {
        this.foundMetadataList.add(metadata);
        this.numberNonOfFallbacks++;
    }

    public void setUserOverridden(Metadata metadata) {
        System.out.println(metadata);
        this.userOverridden = Optional.of(metadata);
    }

    public Metadata getProgrammsBest() {

        List<MetadataField> info = new ArrayList<>();
        this.foundMetadataList.forEach(System.out::println);

        Arrays.stream(MetadataKey.values()).forEach(key ->
                this.foundMetadataList.stream().filter(metadata -> metadata.get(key).isPresent()).findFirst()
                        .ifPresent(metadata ->
                                info.add(metadata.get(key).get()))
        );
        return new Metadata(info);
    }

    public Metadata getFinalChoice() {
        return this.userOverridden.orElse(this.getProgrammsBest());
    }

    public void setUserOverriddenButTestIfItsReallyFromUser(Metadata metadata) {
        if (!this.getProgrammsBest().equals(metadata)) this.setUserOverridden(metadata);
        else System.out.println("Nope");
    }
}
