package sk.uteg.springdatatest.api.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.uteg.springdatatest.db.model.Campaign;

import java.util.UUID;

public interface CampaignRepository extends JpaRepository<Campaign,UUID> {

}
