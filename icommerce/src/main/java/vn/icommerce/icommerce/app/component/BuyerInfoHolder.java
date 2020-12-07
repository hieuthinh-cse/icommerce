package vn.icommerce.icommerce.app.component;

/**
 * Interface to the account holder.
 */
public interface BuyerInfoHolder {

  Boolean isLogin();

  Long getBuyerId();

  String getSessionId();
}
