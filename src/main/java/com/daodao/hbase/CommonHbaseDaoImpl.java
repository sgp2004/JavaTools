package com.daodao.hbase;

import com.weibo.api.commons.hbase.CustomHBase;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class CommonHbaseDaoImpl implements ICommonDao {
    private static final Logger log = LoggerFactory.getLogger(CommonHbaseDaoImpl.class);

    public CustomHBase getHbhelper() {
        return hbhelper;
    }

    public void setHbhelper(CustomHBase hbhelper) {
        this.hbhelper = hbhelper;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    private CustomHBase hbhelper;
    private String tableName;
    private static AtomicInteger count = new AtomicInteger(0);

    @Override
    public long getCount(String row) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getCount(String row, String columnFamily) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<String> getRows(String rowLike) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> getColumns(String row, String columnFamily) {
        List<String> columns = new ArrayList<String>();
        Result result;
        try {
            result = hbhelper.get(tableName, Bytes.toBytes(row), Bytes.toBytes(columnFamily));
            for (KeyValue kv : result.raw()) {
                String id = Bytes.toString(kv.getQualifier());
                columns.add(id);
            }
        } catch (Exception e) {
            log.error("[hbase_error]", e);
        }
        return columns;
    }

    @Override
    public boolean insert(String rowkey, String columnFamily, String column, String value) {
        try {
            Put put = new Put(Bytes.toBytes(rowkey));
            put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(value)); // status_count
            hbhelper.put(tableName, put);
        } catch (Exception e) {
            log.error("[hbase_error]", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean insert(String rowkey, String columnFamily, String column, long num) {
        Put put = new Put(Bytes.toBytes(rowkey));
        put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(num)); // status_count
        try {
            hbhelper.put(tableName, put);
        } catch (Exception e) {
            log.error("[hbase_error]", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean update(String rowkey, String columnFamily, String column, String value) {
        return this.insert(rowkey, columnFamily, column, value);
    }


    @Override
    public boolean incr(String rowkey, String columnFamily, String column, long value) {
        try {
            hbhelper.incr(tableName, Bytes.toBytes(rowkey), Bytes.toBytes(columnFamily), Bytes.toBytes(column), value);
        } catch (Exception e) {
            log.error("[hbase_error]", e);
            return false;
        }
        return true;
    }

    @Override
    public String getStrValue(String rowkey, String columnFamily, String column) {
        Result result;
        try {
            result = hbhelper.get(tableName, Bytes.toBytes(rowkey), Bytes.toBytes(columnFamily));
            byte[] bytes = result.getValue(Bytes.toBytes(columnFamily), Bytes.toBytes(column));
            if (bytes != null)
                return Bytes.toString(bytes);

        } catch (Exception e) {
            log.error("[hbase_error]", e);
        }
        return null;
    }

    @Override
    public long getLongValue(String rowkey, String columnFamily, String column) {
        Result result;
        try {
            result = hbhelper.get(tableName, Bytes.toBytes(rowkey), Bytes.toBytes(columnFamily));
            byte[] bytes = result.getValue(Bytes.toBytes(columnFamily), Bytes.toBytes(column));
            if (bytes != null)
                return Bytes.toLong(result.getValue(Bytes.toBytes(columnFamily), Bytes.toBytes(column)));
        } catch (Exception e) {
            log.error("[hbase_error]", e);
        }
        return 0;
    }

    @Override
    public boolean update(String rowkey, String columnFamily, String column, long value) {
        return this.insert(rowkey, columnFamily, column, value);
    }

    @Override
    public Map<String, String> getMapString(String rowkey, String columnFamily) {
        Result result;
        Map<String, String> map = new HashMap<String, String>();
        try {
            result = hbhelper.get(tableName, Bytes.toBytes(rowkey), Bytes.toBytes(columnFamily));
            for (KeyValue kv : result.raw()) {
                map.put(Bytes.toString(kv.getQualifier()), Bytes.toString(kv.getValue()));
            }
            result = null;
        } catch (Exception e) {
            log.error("[hbase_error]", e);
        }
        return map;
    }

    @Override
    public Map<String, Long> getMapLong(String rowkey, String columnFamily) {
        Result result;
        Map<String, Long> map = new HashMap<String, Long>();
        try {
            result = hbhelper.get(tableName, Bytes.toBytes(rowkey), Bytes.toBytes(columnFamily));
            for (KeyValue kv : result.raw()) {
                map.put(Bytes.toString(kv.getQualifier()), Bytes.toLong(kv.getValue()));
            }
            result = null;
        } catch (Exception e) {
            log.error("[hbase_error]", e);
        }
        return map;
    }

    @Override
    public boolean insertStrColumnMap(String rowkey, String columnFamily, Map<String, String> columnMap) {
        try {
            Put put = new Put(Bytes.toBytes(rowkey));
            for (String column : columnMap.keySet())
                put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(columnMap.get(column)));
            hbhelper.put(tableName, put);
        } catch (Exception e) {
            log.error("[hbase_error]", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean insertLongColumnMap(String rowkey, String columnFamily, Map<String, Long> columnMap) {
        try {
            Put put = new Put(Bytes.toBytes(rowkey));
            for (String column : columnMap.keySet())
                put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(columnMap.get(column)));
            hbhelper.put(tableName, put);
        } catch (Exception e) {
            log.error("[hbase_error]", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean updateStrColumnMap(String rowkey, String columnFamily, Map<String, String> columnMap) {
        boolean result = this.insertStrColumnMap(rowkey, columnFamily, columnMap);
        columnMap.clear();
        return result;
    }

    @Override
    public boolean updateLongColumnMap(String rowkey, String columnFamily, Map<String, Long> columnMap) {
        return this.insertLongColumnMap(rowkey, columnFamily, columnMap);
    }

    @Override
    public boolean delete(String rowkey) {
        try {
            hbhelper.delete(tableName, Bytes.toBytes(rowkey));
        } catch (Exception e) {
            log.error("[hbase_error]", e);
            return false;
        }
        return true;
    }

    @Override
    public ResultScanner scan(String startRow, String stopRow, FilterList filterList) {
        try {
            ResultScanner resultScanner = hbhelper.scan(tableName, Bytes.toBytes(startRow), Bytes.toBytes(stopRow), filterList);
            return resultScanner;
        } catch (Exception e) {
             log.error("[hbase_error]", e);
            return null;
        }
    }

    @Override
    public ResultScanner scan (Scan scan){
        try {
            ResultScanner resultScanner = hbhelper.scan(tableName, scan);
            return resultScanner;
        } catch (Exception e) {
            log.error("[hbase_error]", e);
            return null;
        }
    }

}
