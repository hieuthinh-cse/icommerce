package vn.icommerce.dbtool.cmd;

import java.util.List;

public interface Cmd {

  boolean addOptionValue(String option, List<String> optionValueList);

  void run();
}
