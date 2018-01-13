package net.wit.plat.unspay;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import sun.misc.BASE64Decoder;

public class Const {
    public static String orgNo = "0000000052";
    public static String privateKey =
                    "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDrAYJ5zOLHd4Bi"+"\r"+
                    "4yeDWVXAOpz6yVr9Gz1F5v4YKCwNH4Ry0rI1KzgKu2qKSNDwsBkJu9Ett1Y9I7Tz"+"\r"+
                    "dFdrnep7p5UItVbJkjp6vSWEY+7fj8JDoGvVseG/SC//jC9ZR07jgePp5JzYQCKu"+"\r"+
                    "e5XCj3Z4wxONL0TopnASdTmHELZe/YkiRK4vCVR6OOgNg3GnHyJbQ7ru9XkbOE7y"+"\r"+
                    "lrQM55aIvcSD0X2qypCSGEogoyvms5wf+CnwoiOAnSosX5JoK0G7B5SvNqPTJjxC"+"\r"+
                    "t8G0BOd5q0SqAt45CBppL1zqBDgn90Z3UtAEq9PEWeh5qbDoEcP5ugMArZ0msgTa"+"\r"+
                    "KwshXoV1AgMBAAECggEBAMAQucxGEUoodrtbH14QUy8KOGYWztBxxMAuq5ierHIF"+"\r"+
                    "QkB9xrwBmInc5pQiMvGjWrND1w/f+RF671bRzOjdZViue/VkD4wqgLTzhYgQDJiW"+"\r"+
                    "a5TNtZQvD2s/2kqnDcOPrf/ulpEAENgEgtPLmXgrvA2ykXYgHdduv8W2HNAwbnxu"+"\r"+
                    "dPqKWeAD1nCgoSgfwti2VzpSO96SHzT5CJOCgThqMFO1mup9gwhDEcXW0P9Wdab6"+"\r"+
                    "vDxAcoIH9BY2WCmoXniGOkbIRo61JYSSnoElizIZirRjvwSaSc+mZENk9e8K5+aC"+"\r"+
                    "q5crME5TckABHiBOdjErvjA2K6dZIvWFcMVku4fKAMECgYEA+ZTZtvEgaTuN4cYw"+"\r"+
                    "841ZkHZbNWg27szqeqOsF7nUqsp8LPhQ1zevJwRe22+aWqB4SS5d5f+Qq64sK8gL"+"\r"+
                    "fANgqbUsQmCXKYOzd6zB+sil/5y8oQTIebwCdyOe/bQv7E9YnvZ/RPh6Gq3wZerZ"+"\r"+
                    "QxzJlJgCwRpONryrjqdXRmBn/q0CgYEA8QyzB3sZNXpXAukEphzJZ0H/HkGm2qTo"+"\r"+
                    "VXeTSRltIqQfSBvrCOYmO6MnIK3/TTDHihXC1vxYfbDDTfryhJralQ4Eeeo8T7xf"+"\r"+
                    "ZW1JRf02wQZqiVZcJyrmSN0T+9DLyMQkQClWqDlcnI53g2lPtG1pqfAkuliFzBI2"+"\r"+
                    "1qeS5/7P4ukCgYB6tpxBXdeAxj5hlw/0gDhcVkVMQhxYV7qmaBkyZTVScFKTzdf5"+"\r"+
                    "qbBd78EwBXSQQLxDxx91+a1JLE8di7NR21tItgK39EP+rnmsSu3pf4RW5Nq+FNr5"+"\r"+
                    "N97Cc2o19cVmXDEHn809vSpUOdesVMdUPzBB9mfMSEHSmfuEHXVE7hvT1QKBgAxK"+"\r"+
                    "x35kKp7thC5jz5bg9OxNE0NpuaaArlBdbqdVopkXoXi947hqdByqbz5dYR2AlUxX"+"\r"+
                    "W7421BRkxTDe0Sst8mOTeWr2JOk0A/FaJ1hoVzh0qU4jl0NwDpo8m95FgX7VcbvL"+"\r"+
                    "391oP27EXRfYcPYUdkTyOA1AomILs7wyg21NMzCxAoGAHbAP2vcHdkEd47POtAw4"+"\r"+
                    "HfOgxA1pL5xuGYBgUF33YMpWPsEKEFaRiHlJuwsqWbSRIjRpnOZDOfkGYO7/lflo"+"\r"+
                    "v5vMjWw8M0MoV99pbRmGSFmctHn+aPsK7fJH1CdqnJoCcUgBtX4oQkn4qjvzAdNc"+"\r"+
                    "q+9QE68knjBttexCwuO7Qpg="+"\r";

    public static String usnPublicKey =
    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3IIQ9ZEldFR5fRdthqhm"+"\r"+
    "L7K5LlyvvbEo8gqKzG/xzcrEfZ83AvBj+XlmeVl06uszoRWA3SNMz5vMvS/Ko1Fd"+"\r"+
    "xCOBKNjcCqbEHPhItuwg6oPMZLuNZb3X/kFCLmxuCpYj/OmY38QTaN9Z1bB5O8Me"+"\r"+
    "dboWpTw0EM2Y/5X1q6r8jqr6q4rwFGsPDVnSFXOJ9srPEvJklZRhIFkN3AaxHJ3x"+"\r"+
    "DIR4E5vbtrWLFpgQ0+1JLmg1giwZds3MbTqAIZvph94GilyPiDqcNNXFShKChdac"+"\r"+
    "jbd7iy68iWAwzV4eLtDkqqgu+z4dFwgZlOsmYcDwOzxBNhA2PnwSB4cuNTYdlBcj"+"\r"+
    "GwIDAQAB"+"\r";

    public static String publicKey =
                    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6wGCeczix3eAYuMng1lV"+"\r"+
                    "wDqc+sla/Rs9Reb+GCgsDR+EctKyNSs4CrtqikjQ8LAZCbvRLbdWPSO083RXa53q"+"\r"+
                    "e6eVCLVWyZI6er0lhGPu34/CQ6Br1bHhv0gv/4wvWUdO44Hj6eSc2EAirnuVwo92"+"\r"+
                    "eMMTjS9E6KZwEnU5hxC2Xv2JIkSuLwlUejjoDYNxpx8iW0O67vV5GzhO8pa0DOeW"+"\r"+
                    "iL3Eg9F9qsqQkhhKIKMr5rOcH/gp8KIjgJ0qLF+SaCtBuweUrzaj0yY8QrfBtATn"+"\r"+
                    "eatEqgLeOQgaaS9c6gQ4J/dGd1LQBKvTxFnoeamw6BHD+boDAK2dJrIE2isLIV6F"+"\r"+
                    "dQIDAQAB"+"\r";

    /**
     * 获取私钥
     * @return 当前的私钥对象
     */
    public static RSAPrivateKey getPrivateKey() throws Exception {
        try {
            BASE64Decoder base64Decoder= new BASE64Decoder();
            byte[] buffer= base64Decoder.decodeBuffer(Const.privateKey);
            PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory= KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (IOException e) {
            throw new Exception("私钥数据内容读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    /**
     * 获取公钥
     * @return 当前的公钥对象
     */
    public static RSAPublicKey getPublicKey()  throws Exception {
        try {
            BASE64Decoder base64Decoder= new BASE64Decoder();
            byte[] buffer= base64Decoder.decodeBuffer(Const.publicKey);
            KeyFactory keyFactory= KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec= new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (IOException e) {
            throw new Exception("公钥数据内容读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }


    /**
     * 获取公钥
     * @return 当前的公钥对象
     */
    public static RSAPublicKey getUsnPublicKey()  throws Exception {
        try {
            BASE64Decoder base64Decoder= new BASE64Decoder();
            byte[] buffer= base64Decoder.decodeBuffer(Const.usnPublicKey);
            KeyFactory keyFactory= KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec= new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (IOException e) {
            throw new Exception("公钥数据内容读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

}
