package vanhoang.project.config;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;
import vanhoang.project.utils.LocalDateTimeUtils;

import java.io.*;

@Slf4j
public class CustomBinlogLifecycleListener implements BinaryLogClient.LifecycleListener {
    @Override
    public void onConnect(BinaryLogClient client) {
        log.info("====> connected binlog slave mysql success: {}, {}, {}",
                BinLogClientConfig.BINLOG_ENTITY_MAP,
                BinLogClientConfig.BINLOG_ENTITY_FIELD_MAP,
                BinLogClientConfig.BINLOG_ENTITY_LISTENER_MAP);
    }

    @Override
    public void onCommunicationFailure(BinaryLogClient client, Exception ex) {

    }

    @Override
    public void onEventDeserializationFailure(BinaryLogClient client, Exception ex) {

    }

    @Override
    public void onDisconnect(BinaryLogClient client) {
        log.info("====> starting write binlog info ...");
        try {
            File file = ResourceUtils.getFile(BinLogClientConfig.BINLOG_INFO_FILE);
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("binlog_file=" + client.getBinlogFilename());
            bufferedWriter.newLine();
            bufferedWriter.write("binlog_pos=" + client.getBinlogPosition());
            bufferedWriter.newLine();
            bufferedWriter.write("now=" + LocalDateTimeUtils.getNow());
            bufferedWriter.flush();
            bufferedWriter.close();
            fileWriter.close();
            log.info("====> write bin log disconnect success");
        } catch (IOException e) {
            log.error("====> write bin log disconnect failed: ", e);
        }
    }
}
