package sk.uteg.springdatatest.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OptionSummary {
    private String text;
    private int occurrences;
}
