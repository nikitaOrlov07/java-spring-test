package sk.uteg.springdatatest.api.Service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.uteg.springdatatest.api.Repository.CampaignRepository;
import sk.uteg.springdatatest.api.Service.CampaignSummaryService;
import sk.uteg.springdatatest.api.Service.FeedbackService;
import sk.uteg.springdatatest.api.model.CampaignSummary;
import sk.uteg.springdatatest.api.model.OptionSummary;
import sk.uteg.springdatatest.api.model.QuestionSummary;
import sk.uteg.springdatatest.db.model.*;

import java.math.RoundingMode;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.util.*;

@Service
public class CampaignSummaryServiceImpl implements CampaignSummaryService {

    private CampaignRepository campaignRepository;
    private FeedbackService feedbackService;

    @Autowired
    public CampaignSummaryServiceImpl(CampaignRepository campaignRepository, FeedbackService feedbackService) {
        this.campaignRepository = campaignRepository;
        this.feedbackService = feedbackService;
    }

    @Override
    public CampaignSummary getCampaignSummary(UUID campaignId) throws Exception {
        // Get the campaign
        Campaign campaign = campaignRepository.findById(campaignId).orElseThrow(() -> new Exception("Campaign not found"));

        // Get campaign feedbacks
        List<Feedback> feedbacks =  feedbackService.findFeedbacksByCampaign(campaign);

        // Create the campaign summary object to store information
        CampaignSummary campaignSummary = CampaignSummary.builder()
                .totalFeedbacks(feedbacks.size())
                .build();

        // Questions
        List<QuestionSummary> questionSummaries = new ArrayList<>();
        campaign.getQuestions().forEach(question -> {
            QuestionSummary questionSummary = new QuestionSummary();
            questionSummary.setName(question.getText());
            questionSummary.setType(question.getType());

            if (question.getType() == QuestionType.RATING) {
                BigDecimal averageRating = getRating(feedbacks, question);
                questionSummary.setRatingAverage(averageRating);
                questionSummary.setOptionSummaries(Collections.emptyList());
            } else if (question.getType() == QuestionType.CHOICE) {
                List<OptionSummary> optionSummaries = calculateOptionCounts(feedbacks, question);
                questionSummary.setOptionSummaries(optionSummaries);
                questionSummary.setRatingAverage(BigDecimal.ZERO);
            }

            questionSummaries.add(questionSummary);
        });

        campaignSummary.setQuestionSummaries(questionSummaries);
        return campaignSummary;
    }
    private BigDecimal getRating(List<Feedback> feedbacks, Question question) {
        List<Integer> ratings = feedbacks.stream()
                .flatMap(feedback -> feedback.getAnswers().stream())
                .filter(answer -> answer.getQuestion().equals(question))
                .map(Answer::getRatingValue)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (ratings.isEmpty()) {
            return BigDecimal.ZERO;
        }


        BigDecimal sum = ratings.stream()
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return sum.divide(BigDecimal.valueOf(ratings.size()), 2, RoundingMode.HALF_UP);
    }


    private List<OptionSummary> calculateOptionCounts(List<Feedback> feedbacks, Question question) {
        Map<String, Integer> optionCounts = new HashMap<>();

        for (Feedback feedback : feedbacks) {
            for (Answer answer : feedback.getAnswers()) {
                if (answer.getQuestion().equals(question)) {
                    answer.getSelectedOptions().forEach(option -> {
                        optionCounts.put(option.getText(), optionCounts.getOrDefault(option.getText(), 0) + 1);
                    });
                }
            }
        }

        return optionCounts.entrySet().stream()
                .map(entry -> new OptionSummary(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
