package vn.icommerce.iamoutbox;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import vn.icommerce.common.brave.BraveMarker;
import vn.icommerce.common.dedup.DeDupMarker;
import vn.icommerce.common.jackson.JacksonMarker;
import vn.icommerce.common.kafka.KafkaMarker;
import vn.icommerce.common.outbox.OutboxMarker;
import vn.icommerce.common.springtx.SpringTxMarker;

@Configuration
@ComponentScan(basePackageClasses = {
    SpringTxMarker.class,
    JacksonMarker.class,
    KafkaMarker.class,
    BraveMarker.class,
    OutboxMarker.class,
    DeDupMarker.class,
})
@EntityScan(basePackageClasses = {OutboxMarker.class, DeDupMarker.class})
@EnableJpaRepositories(basePackageClasses = {OutboxMarker.class, DeDupMarker.class})
public class IamOutboxAppConfig {

}
