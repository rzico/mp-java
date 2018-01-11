package net.wit.plat.unspay;

import com.uns.common.CommonUtil;
import com.uns.util.HttpClientUtils;
import net.wit.plugin.PaymentPlugin;
import net.wit.util.JsonUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Merchant {
    public static void addMerchant(net.wit.entity.Merchant merchant) throws Exception {
        Map<String,Object> body = new HashMap<String,Object>();
        body.put("scompany",merchant.getScompany());
        body.put("merchantName",merchant.getMerchantName());
        body.put("phone",merchant.getPhone());
        body.put("bankName",merchant.getBankName());
        body.put("cardCity",merchant.getCardCity());
        body.put("cardProvince",merchant.getCardProvince());
        body.put("branchBankName",merchant.getBranchBankName());
        body.put("address",merchant.getAddress());
        body.put("city",merchant.getCity());
        body.put("province",merchant.getProvince());
        body.put("licenseNo",merchant.getLicenseNo());
        body.put("industryType",merchant.getIndustryType());
        body.put("email",merchant.getEmail());
        body.put("idCard",merchant.getIdCard());
        body.put("cardNo",merchant.getCardNo());
        body.put("userType","P");
        Map<String,Object> wechat = new HashMap<>();
        wechat.put("feeType",2);
        wechat.put("d01fee",merchant.getBrokerage().multiply(new BigDecimal("0.01")));
        wechat.put("t1fee",merchant.getBrokerage().multiply(new BigDecimal("0.01")));
        body.put("wechatMap",wechat);
        Map<String,Object> alipty = new HashMap<>();
        alipty.put("feeType",3);
        alipty.put("d01fee",merchant.getBrokerage().multiply(new BigDecimal("0.01")));
        alipty.put("t1fee",merchant.getBrokerage().multiply(new BigDecimal("0.01")));
        body.put("aliptyMap",alipty);

        String reqUrl = "https://180.166.114.152:18082/small_qrcode_pro/add/addMerchant.do";

        Map<String, String> encr = new HashMap<>();
        try {
            Reader reader = new CharArrayReader(Const.privateKey.toCharArray());
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            PEMParser parser = new PEMParser(reader);
            Object priObj = parser.readObject();
            parser.close();
            PrivateKey privKey = converter.getPrivateKey((PrivateKeyInfo) priObj);

            Reader pbReader = new CharArrayReader(Const.publicKey.toCharArray());
            JcaPEMKeyConverter pbConverter = new JcaPEMKeyConverter();
            PEMParser pbParser = new PEMParser(pbReader);
            Object pubObj = pbParser.readObject();
            parser.close();
            PublicKey pubKey = converter.getPublicKey((SubjectPublicKeyInfo) pubObj);


            Map<String,Object> header = new HashMap<>();
            header.put("version","1.0.0");
            header.put("msgType","01");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            header.put("reqDate",formatter.format(new Date()));

            Map<String,Map<String, Object>> data = new HashMap<>();
            data.put("head",header);
            data.put("body",body);
            encr = CommonUtil.encryptData(data,privKey,pubKey);
            encr.put("insNo","0000000052");
            String req = JsonUtils.toJson(encr);
            System.out.println(req);

            String resp= SLLClient.post(reqUrl,"","application/json", "UTF-8", 10000, 10000);

            System.out.println(resp);

        } catch (IOException e) {
            throw  new Exception("验签不通过");
        } catch (Exception e) {
            throw  new Exception("验签不通过");
        }
    }

    public static void main(String[] args) throws Exception {
        net.wit.entity.Merchant merchant = new net.wit.entity.Merchant();
        merchant.setScompany("芸店");
        merchant.setMerchantName("张森荣");
        merchant.setPhone("13860431130");
        merchant.setBankName("32429");
        merchant.setCardCity("2511");
        merchant.setCardProvince("25");
        merchant.setBranchBankName("厦门市海沧支行");
        merchant.setAddress("谊爱路海西文创大厦210");
        merchant.setCity("厦门市");
        merchant.setPhone("福建省");
        merchant.setLicenseNo("913502030899205666");
        merchant.setIndustryType("160");
        merchant.setEmail("zhangsr@rzico.com");
        merchant.setIdCard("352623197805181613");
        merchant.setCardNo("4367421930031121575");
        merchant.setBrokerage(new BigDecimal("0.38"));
        Merchant.addMerchant(merchant);
    }

}
