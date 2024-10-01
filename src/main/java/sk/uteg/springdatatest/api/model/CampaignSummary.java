package sk.uteg.springdatatest.api.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CampaignSummary {
    private long totalFeedbacks;
    private List<QuestionSummary> questionSummaries;
}
