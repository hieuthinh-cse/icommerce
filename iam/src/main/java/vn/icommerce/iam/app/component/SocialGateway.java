package vn.icommerce.iam.app.component;

/**
 * Interface to password encoder.
 */
public interface SocialGateway {

  FacebookUser getUser(String accessToken);
}
