package com.os.binlog;

/**
 * interface đóng vai trò xử lý event.
 */
@FunctionalInterface
public interface BinLogListener {

    void listen(BinLogItem item);
}
