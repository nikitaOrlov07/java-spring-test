package sk.uteg.springdatatest.api.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sk.uteg.springdatatest.api.Service.CampaignSummaryService;
import sk.uteg.springdatatest.api.model.CampaignSummary;

import java.util.UUID;

@RestController("campaign")
@RequiredArgsConstructor
@Slf4j
public class CampaignController {

    private final CampaignSummaryService campaignSummaryService;
    @GetMapping("/summary/{uuid}")
    public ResponseEntity<CampaignSummary> getSummary(@PathVariable UUID uuid) {
        try {
            log.info("Finding campaign with uuid {}", uuid);
            CampaignSummary campaignSummary = campaignSummaryService.getCampaignSummary(uuid);
            if (campaignSummary == null) {
                log.error("CampaignSummary object is null");
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(campaignSummary);
        }
        catch (Exception e) {
            return  ResponseEntity.badRequest().build();
        }

    }

}
