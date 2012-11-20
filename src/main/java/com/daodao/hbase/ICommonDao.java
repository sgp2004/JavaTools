package com.daodao.hbase;

import java.util.List;
import java.util.Map;


public interface ICommonDao {
    public boolean insert(String rowkey, String columnFamily, String column, String value);

    public boolean insert(String rowkey, String columnFamily, String column, long num);

    public boolean insertStrColumnMap(String rowkey, String columnFamily, Map<String, String> columnMap);

    public boolean insertLongColumnMap(String rowkey, String columnFamily, Map<String, Long> columnMap);

    public boolean update(String rowkey, String columnFamily, String column, String value);

    public boolean update(String rowkey, String columnFamily, String column, long value);

    public boolean updateStrColumnMap(String rowkey, String columnFamily, Map<String, String> columnMap);

    public boolean updateLongColumnMap(String rowkey, String columnFamily, Map<String, Long> columnMap);

    public boolean delete(String rowkey);

    public boolean incr(String rowkey, String columnFamily, String column, long value);

    public long getCount(String rowkey);

    public long getCount(String rowkey, String columnFamily);

    public String getStrValue(String rowkey, String columnFamily, String column);

    public long getLongValue(String rowkey, String columnFamily, String column);

    public Map<String, String> getMapString(String rowkey, String columnFamily);

    public Map<String, Long> getMapLong(String rowkey, String columnFamily);

    public List<String> getRows(String rowLike);

    public List<String> getColumns(String rowkey, String columnFamily);
}
