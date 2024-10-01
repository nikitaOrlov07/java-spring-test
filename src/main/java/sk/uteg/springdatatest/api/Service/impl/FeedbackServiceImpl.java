package sk.uteg.springdatatest.api.Service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.uteg.springdatatest.api.Repository.FeedbackRepository;
import sk.uteg.springdatatest.api.Service.FeedbackService;
import sk.uteg.springdatatest.db.model.Campaign;
import sk.uteg.springdatatest.db.model.Feedback;

import java.util.List;

@Service
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Override
    public List<Feedback> findFeedbacksByCampaign(Campaign campaign) {
        log.info("Feedback service method \"findFeedbacksByCampaign\" is working");
        return  feedbackRepository.findByCampaign(campaign);
    }
}
