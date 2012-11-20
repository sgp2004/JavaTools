package com.daodao.hadoop.io;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guanpu
 *         Date: 12-11-3
 *         Time: 上午8:43
 *         To change this template use File | Settings | File Templates.
 */
public class StreamCompressor {
    // echo "Text" | hadoop StreamCompressor org.apache.hadoop.io.compress.GzipCodec \ | gunzip -
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String codecClassname = args[0];
        Class<?> codecClass = Class.forName(codecClassname); Configuration conf = new Configuration(); CompressionCodec codec = (CompressionCodec)
                ReflectionUtils.newInstance(codecClass, conf);
        CompressionOutputStream out = codec.createOutputStream(System.out); IOUtils.copyBytes(System.in, out, 4096, false);
        out.finish();
    }
}
