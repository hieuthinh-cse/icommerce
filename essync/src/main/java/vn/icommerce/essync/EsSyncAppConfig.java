package vn.icommerce.essync;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import vn.icommerce.common.brave.BraveMarker;
import vn.icommerce.common.dedup.DeDupMarker;
import vn.icommerce.common.elasticsearch.ElasticsearchMarker;
import vn.icommerce.common.jackson.JacksonMarker;
import vn.icommerce.common.kafka.KafkaMarker;
import vn.icommerce.common.outbox.OutboxMarker;
import vn.icommerce.common.springretry.SpringRetryMarker;
import vn.icommerce.common.springtx.SpringTxExecutor;
import vn.icommerce.sharedkernel.SharedKernelMarker;

/**
 * This is the configuration of the EsSync app.
 *
 */
@Configuration
@ComponentScan(basePackageClasses = {
    JacksonMarker.class,
    KafkaMarker.class,
    BraveMarker.class,
    OutboxMarker.class,
    DeDupMarker.class,
    SharedKernelMarker.class,
    SpringTxExecutor.class,
    ElasticsearchMarker.class,
    SpringRetryMarker.class
})
@EntityScan(basePackageClasses = {SharedKernelMarker.class, OutboxMarker.class, DeDupMarker.class})
@EnableJpaRepositories(basePackageClasses = {SharedKernelMarker.class, OutboxMarker.class,
    DeDupMarker.class})
public class EsSyncAppConfig {

}
