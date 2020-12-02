package vn.icommerce.iam;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import vn.icommerce.common.brave.BraveMarker;
import vn.icommerce.common.dedup.DeDupMarker;
import vn.icommerce.common.elasticsearch.ElasticsearchMarker;
import vn.icommerce.common.jackson.JacksonMarker;
import vn.icommerce.common.outbox.OutboxMarker;
import vn.icommerce.common.springresttemplate.SpringRestTemplateMarker;
import vn.icommerce.common.springretry.SpringRetryMarker;
import vn.icommerce.common.springtx.SpringTxMarker;
import vn.icommerce.sharedkernel.SharedKernelMarker;

/**
 * This is the configuration of the IAM app.
 *
 */
@Configuration
@ComponentScan(basePackageClasses = {
    JacksonMarker.class,
    BraveMarker.class,
    SpringTxMarker.class,
    SpringRestTemplateMarker.class,
    SharedKernelMarker.class,
    OutboxMarker.class,
    DeDupMarker.class,
    ElasticsearchMarker.class,
    SpringRetryMarker.class
})
@EntityScan(basePackageClasses = {SharedKernelMarker.class, OutboxMarker.class, DeDupMarker.class})
@EnableJpaRepositories(basePackageClasses = {SharedKernelMarker.class, OutboxMarker.class,
    DeDupMarker.class})
public class IamAppConfig {

}
