package com.daodao.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guanpu
 *         Date: 12-11-1
 *         Time: 下午5:47
 *         To change this template use File | Settings | File Templates.
 */
public class FileSystemDoubleCat {
    public static void main(String[] args) throws Exception {
        String uri = args[0];
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        FSDataInputStream in = null;
        try {
            in = fs.open(new Path(uri));
            IOUtils.copyBytes(in, System.out, 4096, false);
            in.seek(0); // go back to the start of the file
            IOUtils.copyBytes(in, System.out, 4096, false);

            //make a dir
            fs.mkdirs(new Path("dir_by_program"));

            Path p = new Path("sync_test");
            FSDataOutputStream out = fs.create(p);
            out.write("content".getBytes("UTF-8"));
            out.flush();
            out.sync();
            System.out.println("time to sleep");
            Thread.sleep(10000);
            System.out.println("sleep over,hello!");
        } finally {
            IOUtils.closeStream(in);
        }
    }
}