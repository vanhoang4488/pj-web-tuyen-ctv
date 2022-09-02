package com.os.binlog;

/**
 * interface đóng vài trò xác định các hành động
 * trước và sau của BinLogListener.
 */
@FunctionalInterface
public interface HandleBinLog {

    void handle(BinLogItem item);
}
