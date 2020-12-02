package vn.icommerce.dbtool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import vn.icommerce.common.jackson.JacksonMarker;
import vn.icommerce.common.springtx.SpringTxMarker;
import vn.icommerce.dbtool.cmd.Cmd;
import vn.icommerce.dbtool.cmd.ElasticsearchCleanCmd;
import vn.icommerce.dbtool.cmd.ElasticsearchImportFromDbCmd;
import vn.icommerce.dbtool.cmd.ElasticsearchMigrateCmd;
import vn.icommerce.dbtool.cmd.FlywayCleanCmd;
import vn.icommerce.dbtool.cmd.FlywayMigrateCmd;
import vn.icommerce.sharedkernel.SharedKernelMarker;

/**
 * This is a tool to provision data.
 *
 */
@SpringBootApplication(
    scanBasePackageClasses = {
        JacksonMarker.class,
        DbTool.class,
        SpringTxMarker.class
    },
    exclude = KafkaAutoConfiguration.class)
@EntityScan(basePackageClasses = SharedKernelMarker.class)
public class DbTool implements ApplicationRunner {

  public static void main(String[] args) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    if (Objects.isNull(System.getProperty("spring.profiles.active"))) {
      System.setProperty("spring.profiles.active", "local");
    }

    new SpringApplicationBuilder(DbTool.class)
        .web(WebApplicationType.NONE)
        .build()
        .run(args)
        .close();
  }

  private final Map<String, Cmd> cmdFactory = new HashMap<>();

  public DbTool(
      FlywayCleanCmd flywayCleanCmd,
      FlywayMigrateCmd flywayMigrateCmd,
      ElasticsearchCleanCmd elasticsearchCleanCmd,
      ElasticsearchMigrateCmd elasticsearchMigrateCmd,
      ElasticsearchImportFromDbCmd elasticsearchImportFromDbCmd) {
    cmdFactory.put("fC", flywayCleanCmd);
    cmdFactory.put("fM", flywayMigrateCmd);
    cmdFactory.put("eC", elasticsearchCleanCmd);
    cmdFactory.put("eM", elasticsearchMigrateCmd);
    cmdFactory.put("eI", elasticsearchImportFromDbCmd);
  }

  private List<String> getOptionValueList(String option, ApplicationArguments args) {
    return args
        .getOptionValues(option)
        .stream()
        .map(optionValue -> optionValue.split(","))
        .flatMap(Stream::of)
        .collect(Collectors.toList());
  }

  private Cmd toCmd(String cmd) {
    return Optional
        .ofNullable(cmdFactory.get(cmd))
        .orElseThrow(
            () -> new RuntimeException(String.format("Error: %s is not a valid command", cmd)));
  }

  private void addOptionToCmdList(
      List<Cmd> cmdList,
      String option,
      ApplicationArguments args) {
    var added = cmdList
        .stream()
        .map(cmd -> cmd.addOptionValue(option, getOptionValueList(option, args)))
        .reduce(false, (added1, added2) -> added1 || added2);

    if (!added) {
      throw new RuntimeException(String.format("Error: %s is not a valid option", option));
    }
  }

  private List<Cmd> toCmdList(ApplicationArguments args) {
    var cmdList = args
        .getNonOptionArgs()
        .stream()
        .map(this::toCmd)
        .distinct()
        .collect(Collectors.toList());

    args
        .getOptionNames()
        .stream()
        .filter(option -> !"spring.profiles.active".equals(option))
        .forEach(option -> addOptionToCmdList(cmdList, option, args));

    return cmdList;
  }

  @Override
  public void run(ApplicationArguments args) {
    toCmdList(args).forEach(Cmd::run);
  }
}
