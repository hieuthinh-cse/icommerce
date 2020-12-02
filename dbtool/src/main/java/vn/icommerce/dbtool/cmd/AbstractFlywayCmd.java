package vn.icommerce.dbtool.cmd;

import java.util.List;

public abstract class AbstractFlywayCmd implements Cmd {

  @Override
  public boolean addOptionValue(String option, List<String> optionValueList) {
    return false;
  }
}
