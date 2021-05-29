package repository;

import model.metadata.Metadata;
import model.metadata.MetadataKey;
import model.metadata.Metadatum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MetadataRepository {
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
        this.userOverridden = Optional.of(metadata);
    }

    public Metadata getProgrammsBest() {

        List<Metadatum> info = new ArrayList<>();
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
}
