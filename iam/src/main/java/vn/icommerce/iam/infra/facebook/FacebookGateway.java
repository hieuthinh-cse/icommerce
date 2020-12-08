package vn.icommerce.iam.infra.facebook;

import static vn.icommerce.sharedkernel.domain.model.DomainCode.EXTERNAL_SVC_CONNECTION_ERROR;
import static vn.icommerce.sharedkernel.domain.model.DomainCode.EXTERNAL_SVC_EMPTY_RESPONSE_ERROR;
import static vn.icommerce.sharedkernel.domain.model.DomainCode.EXTERNAL_SVC_INTERRUPTION_ERROR;
import static vn.icommerce.sharedkernel.domain.model.DomainCode.EXTERNAL_SVC_RESPONSE_ERROR;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NoHttpResponseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import vn.icommerce.iam.app.component.FacebookDebug;
import vn.icommerce.iam.app.component.FacebookUser;
import vn.icommerce.iam.app.component.SocialGateway;
import vn.icommerce.sharedkernel.app.component.ResultStatus;
import vn.icommerce.sharedkernel.domain.exception.DomainException;
import vn.icommerce.sharedkernel.domain.model.DomainCode;

@Component
@Slf4j
public class FacebookGateway implements SocialGateway {

  private final RestTemplate restTemplate;

  private final FacebookConfig facebookConfig;

  private final String FACEBOOK_GRAPH_API_BASE = "https://graph.facebook.com/v9.0";

  public FacebookGateway(RestTemplate restTemplate,
      FacebookConfig facebookConfig) {
    this.restTemplate = restTemplate;
    this.facebookConfig = facebookConfig;
  }

  private FacebookDebug toFacebookDebug(FacebookTokenDebugResponse resp) {
    if (Objects.nonNull(resp.getData()) &&
        Objects.nonNull(resp.getData().getError())) {
      return new FacebookDebug()
          .setResultStatus(ResultStatus.FAILED);
    }

    return new FacebookDebug()
        .setResultStatus(ResultStatus.OK)
        .setAppId(Long.valueOf(resp.getData().getAppId()))
        .setIsValid(resp.getData().getIsValid());
  }

  private void requireValidToken(String accessToken) {
    var path = "/debug_token?input_token={input_token}&access_token={access_token}";
    final Map<String, String> variables = new HashMap<>();
    variables.put("input_token", accessToken);
    variables.put("access_token",
        String.format("%s|%s", facebookConfig.getAppId(), facebookConfig.getAppSecret()));
    var tokenDebug = getForResult(FACEBOOK_GRAPH_API_BASE + path, variables, this::toFacebookDebug,
        FacebookTokenDebugResponse.class);

    if (ResultStatus.FAILED.equals(tokenDebug.getResultStatus())) {
      throw new DomainException(DomainCode.TOKEN_EXPIRED);
    }
  }

  private FacebookUser toFacebookUserResult(FacebookUserResponse resp) {
    return new FacebookUser()
        .setResultStatus(ResultStatus.OK)
        .setId(resp.getId())
        .setFullName(String.format("%s %s",resp.getFirstName(), resp.getLastName()))
        .setEmail(resp.getEmail());
  }

  public FacebookUser getUser(String accessToken) {
    requireValidToken(accessToken);

    var path = "/me?fields={fields}&redirect={redirect}&access_token={access_token}";
    var fields = "email,first_name,last_name,id,picture.width(720).height(720)";
    final Map<String, String> variables = new HashMap<>();
    variables.put("fields", fields);
    variables.put("redirect", "false");
    variables.put("access_token", accessToken);
    return getForResult(FACEBOOK_GRAPH_API_BASE + path, variables, this::toFacebookUserResult,
        FacebookUserResponse.class);
  }

  private <I, O> O getForResult(String uri, Map<String, String> variables, Function<I, O> transform,
      Class<I> clazz) {
    return handleExternalSvcCall(
        uri,
        () -> {
          ResponseEntity<I> responseEntity = restTemplate.getForEntity(uri, clazz, variables);

          log.info("method: getForResult, uri: {} , resp: {}", uri, responseEntity.getBody());

          return Optional
              .ofNullable(responseEntity.getBody())
              .map(transform)
              .orElseThrow(() -> new DomainException(EXTERNAL_SVC_EMPTY_RESPONSE_ERROR, uri));
        });
  }

  private <T> T handleExternalSvcCall(String uri, Supplier<T> supplier) {
    try {
      return supplier.get();
    } catch (RestClientResponseException e) {
      throw new DomainException(
          EXTERNAL_SVC_RESPONSE_ERROR,
          uri,
          e.getMessage(),
          e.getResponseBodyAsString());
    } catch (ResourceAccessException e) {
      if (isConnectionFailureException(e)) {
        throw new DomainException(
            EXTERNAL_SVC_CONNECTION_ERROR,
            uri,
            e.getMessage());
      }

      throw new DomainException(
          EXTERNAL_SVC_INTERRUPTION_ERROR,
          uri,
          e.getMessage());
    }
  }

  private boolean isConnectionFailureException(ResourceAccessException e) {
    Throwable t = e.getRootCause();

    return t instanceof ConnectException
        || t instanceof UnknownHostException
        || t instanceof NoHttpResponseException;
  }
}
