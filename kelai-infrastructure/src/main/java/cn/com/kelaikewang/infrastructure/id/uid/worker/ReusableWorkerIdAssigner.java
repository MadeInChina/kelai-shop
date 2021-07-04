package cn.com.kelaikewang.infrastructure.id.uid.worker;


import cn.com.kelaikewang.infrastructure.id.uid.worker.dao.WorkerNodeDao;
import cn.com.kelaikewang.infrastructure.id.uid.worker.domain.WorkerNode;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class ReusableWorkerIdAssigner extends DisposableWorkerIdAssigner {

    @Autowired
    private WorkerNodeDao workerNodeRepository;

    private static final String workerIdDirPath;
    private static final String WORKER_ID_FILE = "uid-generator-worker-id-%s.conf";
    private static String CLS_PATH_MD5;
    private static final Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
    static {
        String clsPath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        try {
            CLS_PATH_MD5 = md5(clsPath);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String dir = System.getProperty("user.dir") ;
        workerIdDirPath = dir + (dir.endsWith(File.separator) ? "" : File.separator) + "uid-generator";
        File workerIdDir = new File(workerIdDirPath);
        if (!workerIdDir.exists()){
            workerIdDir.mkdirs();
        }
    }
    @Transactional("blTransactionManager")
    @Override
    public synchronized long assignWorkerId() throws Exception {
        if (StringUtils.isEmpty(CLS_PATH_MD5)){
            throw new RuntimeException("CLS_PATH_MD5值为空");
        }
        File workerIdFile = new File(workerIdDirPath + (workerIdDirPath.endsWith(File.separator) ? "" : File.separator) + String.format(WORKER_ID_FILE,CLS_PATH_MD5));
        if (workerIdFile.exists()){
            String workerId = readToString(workerIdFile);
            if (!StringUtils.isEmpty(workerId) && isInteger(workerId)){
                long id = Long.valueOf(workerId);
                WorkerNode workerNode = workerNodeRepository.getById(id);
                if (workerNode != null){
                    return id;
                }else {
                    return assignWorkerIdAndSaveToDisk(workerIdFile);
                }
            }else {
                return assignWorkerIdAndSaveToDisk(workerIdFile);
            }
        }else {
            return assignWorkerIdAndSaveToDisk(workerIdFile);
        }
    }
    private long assignWorkerIdAndSaveToDisk(File workerIdFile) throws Exception {
        if (!workerIdFile.exists()){
            workerIdFile.createNewFile();
        }

        long workerId = super.assignWorkerId();

        writeWorkerIdToFile(String.valueOf(workerId),workerIdFile);

        return workerId;
    }
    private void writeWorkerIdToFile(String workerId,File fileName)throws Exception{

        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        fileOutputStream.write(workerId.getBytes(StandardCharsets.UTF_8));
        fileOutputStream.close();

    }
    private String readToString(File file) {
        String encoding = "UTF-8";
        Long filelength = file.length();
        byte[] content = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(content);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(content, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }
    private static boolean isInteger(String str) {
        return pattern.matcher(str).matches();
    }
    private static String md5(String source) throws NoSuchAlgorithmException {
        byte[] bytes = digest(source.getBytes(StandardCharsets.UTF_8),"MD5",null,1);
        return encodeHex(bytes);
    }
    private static byte[] digest(byte[] input, String algorithm, byte[] salt, int iterations) throws NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance(algorithm);

        if (salt != null) {
            digest.update(salt);
        }

        byte[] result = digest.digest(input);

        for (int i = 1; i < iterations; i++) {
            digest.reset();
            result = digest.digest(result);
        }
        return result;

    }
    private static String encodeHex(byte[] input) {
        return new String(Hex.encodeHex(input));
    }
}
