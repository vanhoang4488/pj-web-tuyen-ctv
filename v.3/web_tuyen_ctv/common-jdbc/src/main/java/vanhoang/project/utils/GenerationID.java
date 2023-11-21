package vanhoang.project.utils;

import org.springframework.stereotype.Component;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class GenerationID {

    private final static Long START_PROJECT_TIME = 1_700_497_723_894L;
    private final static Long TIMESTAMP_LEFTSHIFT = 10L;
    private final static Long WORKID_LEFTSHIFT = 12L;
    private final static Long[] RANDOM_NUMBERS = new Long[] {4L, 8L, 32L, 64L, 88L, 124L, 322L, 444L, 888L, 1045L};

    private final Long workId;

    public GenerationID() throws UnknownHostException {
        String ip = Inet4Address.getLocalHost().getHostAddress();
        String[] partIpStrings = ip.split("\\.");
        long totalPartId = 0L;
        for (String partIdString : partIpStrings) {
            totalPartId += Long.parseLong(partIdString);
        }
        int randomNumberIndex = ThreadLocalRandom.current().nextInt(RANDOM_NUMBERS.length);
        this.workId = totalPartId % RANDOM_NUMBERS[randomNumberIndex];
    }

    public synchronized Long generationUUID() {
        int randomNumberIndex = ThreadLocalRandom.current().nextInt(RANDOM_NUMBERS.length);
        Long randomNumber = RANDOM_NUMBERS[randomNumberIndex];
        return ((LocalDateTimeUtils.getMillisNow() - START_PROJECT_TIME) << TIMESTAMP_LEFTSHIFT) | workId << WORKID_LEFTSHIFT | randomNumber;
    }
}
