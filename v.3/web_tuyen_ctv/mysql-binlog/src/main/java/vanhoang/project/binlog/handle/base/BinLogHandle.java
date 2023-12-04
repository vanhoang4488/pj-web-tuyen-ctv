package vanhoang.project.binlog.handle.base;

import vanhoang.project.binlog.bean.BinlogItem;

public interface BinLogHandle {
    void handle(BinlogItem binLogItem);
}
