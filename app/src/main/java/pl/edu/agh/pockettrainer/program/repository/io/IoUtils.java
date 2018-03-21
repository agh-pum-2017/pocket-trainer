package pl.edu.agh.pockettrainer.program.repository.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import pl.edu.agh.pockettrainer.program.Logger;

public class IoUtils {

    private final static Logger logger = new Logger(IoUtils.class);

    public static TempDir makeTempDir(File parentDir) {
        final File file = new File(parentDir, UUID.randomUUID().toString());
        file.mkdirs();
        logger.debug("Making temporary directory at '%s'", file.getAbsolutePath());
        return new TempDir(file);
    }

    public static void delete(File path) {
        if (path.isDirectory())
            for (File child : path.listFiles()) {
                delete(child);
            }
        if (path.delete()) {
            logger.debug("Deleting '%s'", path);
        } else {
            logger.error("Could not delete '%s'", path);
        }
    }

    public static void unzip(InputStream zipArchive, File outputFolder) {

        byte[] buffer = new byte[1024];

        try{
            ZipInputStream zis = new ZipInputStream(zipArchive);
            ZipEntry entry = zis.getNextEntry();

            while (entry != null) {

                File file = new File(outputFolder, entry.getName());

                logger.debug("Unzipping file '%s'", file.getAbsolutePath());

                File parentDir = new File(file.getParent());
                parentDir.mkdirs();

                try (FileOutputStream fos = new FileOutputStream(file)) {
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                }

                zis.closeEntry();
                entry = zis.getNextEntry();
            }

        } catch(IOException ex) {
            logger.error(ex, "Unable to unzip archive into'%s'", outputFolder.getAbsolutePath());
        }
    }

    public static String readFully(File file) throws IOException {
        return readFully(file, "UTF-8");
    }

    public static String readFully(File file, String encoding) throws IOException {
        logger.debug("Reading file '%s'", file.getAbsolutePath());
        final StringBuilder sb = new StringBuilder();
        try (BufferedReader in = new BufferedReader(
                                    new InputStreamReader(
                                        new FileInputStream(
                                            file.getAbsolutePath()), encoding))) {
            while (true) {
                final String line = in.readLine();
                if (line == null) break;
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }

    public static void save(File file, String contents) throws IOException {
        save(file, contents, "UTF-8");
    }

    public static void save(File file, String contents, String encoding) throws IOException {
        logger.debug("Saving file at '%s'", file.getAbsolutePath());
        file.getParentFile().mkdirs();
        try (BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(file.getAbsolutePath()), encoding))) {
            out.write(contents);
        }
    }

    public static void copy(File from, File to) throws IOException {
        logger.debug("Copying file from '%s' to '%s'", from.getAbsolutePath(), to.getAbsolutePath());
        final FileChannel src = new FileInputStream(from).getChannel();
        final FileChannel dst = new FileOutputStream(to).getChannel();
        dst.transferFrom(src, 0, src.size());
    }

    public static List<File> listFiles(File dir) {
        final File[] files = dir.listFiles();
        return files == null ? new ArrayList<File>() : Arrays.asList(files);
    }

    public static String md5(File file) throws NoSuchAlgorithmException, IOException {
        logger.debug("Calculating MD5 hash sum of file '%s'", file.getAbsolutePath());
        try (InputStream input =  new FileInputStream(file)) {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            final byte[] buffer = new byte[1024];
            int read;
            do {
                read = input.read(buffer);
                if (read > 0) {
                    md.update(buffer, 0, read);
                }
            } while (read != -1);
            return String.format("%032X", new BigInteger(1, md.digest()));
        }
    }

    public static String md5(String contents) throws NoSuchAlgorithmException, IOException {
        logger.debug("Calculating MD5 hash sum of a string");
        final MessageDigest md = MessageDigest.getInstance("MD5");
        return String.format("%032X", new BigInteger(1, md.digest(contents.getBytes("UTF-8"))));
    }

    public static File downloadSync(String address, File dir) throws IOException {

        final URL url = new URL(address);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            connection.setConnectTimeout(3000);
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (InputStream input = connection.getInputStream()) {

                    final File file = new File(dir, UUID.randomUUID().toString());
                    file.getParentFile().mkdirs();

                    logger.debug("Downloading '%s' into '%s'", address, file.getAbsolutePath());

                    try (OutputStream output = new FileOutputStream(file)) {
                        byte data[] = new byte[4096];
                        int count;
                        while ((count = input.read(data)) != -1) {
                            output.write(data, 0, count);
                        }
                    }

                    return file;
                }
            } else {
                logger.error("Server returned HTTP %d %s", connection.getResponseCode(), connection.getResponseMessage());
            }
        } finally {
            connection.disconnect();
        }

        return null;
    }
}
