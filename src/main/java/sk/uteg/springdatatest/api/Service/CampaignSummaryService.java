package sk.uteg.springdatatest.api.Service;

import sk.uteg.springdatatest.api.model.CampaignSummary;

import java.util.UUID;

public interface CampaignSummaryService {
    CampaignSummary getCampaignSummary(UUID campaignId) throws Exception;
}
