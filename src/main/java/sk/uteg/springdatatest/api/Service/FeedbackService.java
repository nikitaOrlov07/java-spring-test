package sk.uteg.springdatatest.api.Service;

import sk.uteg.springdatatest.db.model.Campaign;
import sk.uteg.springdatatest.db.model.Feedback;

import java.util.List;

public interface FeedbackService {

    List<Feedback> findFeedbacksByCampaign(Campaign campaign);
}
