package io.github.ganchix.ganache;

import org.slf4j.Logger;
import org.testcontainers.containers.output.OutputFrame;

import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * Created by Rafael Ríos on 20/05/18.
 */
public class LogGanacheExtractorConsumer implements Consumer<OutputFrame> {
    private static final Pattern ANSI_CODE_PATTERN = Pattern.compile("\\[\\d[ABCD]");
    private static final Pattern IS_ADDRESS_OR_PRIVATE_KEY = Pattern.compile("\\((\\d{1,})\\).*");
    private final Logger logger;
    private String prefix = "";
    private GanacheContainer ganacheContainer;

    public LogGanacheExtractorConsumer(Logger logger, GanacheContainer ganacheContainer) {
        this.logger = logger;
        this.ganacheContainer = ganacheContainer;
    }

    public LogGanacheExtractorConsumer withPrefix(String prefix) {
        this.prefix = "[" + prefix + "] ";
        return this;
    }

    @Override
    public void accept(OutputFrame outputFrame) {
        String utf8String = outputFrame.getUtf8String();

        if (utf8String != null) {
            OutputFrame.OutputType outputType = outputFrame.getType();
            String message = utf8String.trim();
            if (IS_ADDRESS_OR_PRIVATE_KEY.matcher(message).matches()) {
                String[] split = message.split(" ");
                String addressOrPrivateKey = split[1].trim();
                String position = split[0].replaceFirst("\\(", "").replaceFirst("\\)", "");
                if (addressOrPrivateKey.startsWith("0x")) {
                    ganacheContainer.addAccountAddress(Integer.parseInt(position), addressOrPrivateKey);
                } else {
                    ganacheContainer.addAccountPrivateKey(Integer.parseInt(position), addressOrPrivateKey);
                }
            }

            if (ANSI_CODE_PATTERN.matcher(message).matches()) {
                return;
            }

            switch (outputType) {
                case END:
                    break;
                case STDOUT:
                case STDERR:
                    logger.info("{}{}: {}", prefix, outputType, message);
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected outputType " + outputType);
            }
        }
    }
}

