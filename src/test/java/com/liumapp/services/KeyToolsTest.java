package com.liumapp.services;

import com.liumapp.KeyGeneratorHelper.certificates.CSR;
import com.liumapp.KeyGeneratorHelper.certificates.P7B;
import com.liumapp.KeyGeneratorHelper.certificates.Resource;
import com.liumapp.KeyGeneratorHelper.service.KeyStoreAdapter;
import com.liumapp.KeyGeneratorHelper.service.KeyTools;
import junit.framework.TestCase;

import java.io.File;
import java.io.FileOutputStream;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.time.temporal.ChronoUnit;

/**
 * Created by liumapp on 10/12/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
public class KeyToolsTest extends TestCase {

    public void shouldGenerateKeyStoreWithKeyPair() throws Exception {
        try (FileOutputStream out = new FileOutputStream("test.ks")) {
            KeyTools.newKeyStore("1234")
                    .newKeyPair()
                    .keyLength(2048)
                    .generateWithCertificate()
                    .withValidity(1, ChronoUnit.YEARS)
                    .withDistinguishName()
                    .commonName("Andrea Como")
                    .state("Toscana")
                    .locality("Prato")
                    .country("IT")
                    .email("test@example.com")
                    .build()
                    .createInKeyStore("test", "456")
                    .writeTo(out);
        } finally {
            File keyStoreFile = new File("test.ks");
            assertTrue(keyStoreFile.exists());
            assertTrue(keyStoreFile.delete());
        }
    }

    public void shouldLoadKeyStoreFromClassPath() throws Exception {
        Resource resource = Resource.from("classpath:keystore.ks");
        KeyStoreAdapter keyStoreAdapter = KeyTools.keyStoreFrom(resource, "1234");

        assertNotNull(keyStoreAdapter.toKeyStore());
        Certificate certificate = keyStoreAdapter.toKeyStore().getCertificate("test");
        assertNotNull(certificate);
        assertTrue(certificate instanceof X509Certificate);

        X509Certificate x509Certificate = (X509Certificate) certificate;
        assertEquals("CN=Andrea Como, ST=Toscana, L=Prato, C=IT", x509Certificate.getSubjectDN().getName());
    }

    public void shouldGenerateCertificateSignRequest() throws Exception {
        Resource resource = Resource.from("classpath:keystore.ks");
        KeyStoreAdapter keyStoreAdapter = KeyTools.keyStoreFrom(resource, "1234");

        CSR csr = keyStoreAdapter.generateCSR("test", "456");

        assertNotNull(csr);
        assertNotNull(csr.toPkcs10());

        assertEquals("CN=Andrea Como, ST=Toscana, L=Prato, C=IT", csr.toPkcs10().getSubjectName().toString());
    }

    public void shouldSignCertificateSignRequest() throws Exception {
        Resource resource = Resource.from("classpath:keystore.ks");
        KeyStoreAdapter requesterKeyStore = KeyTools.keyStoreFrom(resource, "1234");

        X509Certificate[] certificates = requesterKeyStore.getCertificates("test");
        assertEquals(1, certificates.length);

        CSR csr = requesterKeyStore.generateCSR("test", "456");

        Resource ca = Resource.from("classpath:ca.ks");
        KeyStoreAdapter caKeyStore = KeyTools.keyStoreFrom(ca, "ca");

        P7B signResponse = caKeyStore.signCSR(csr, "ca", "ca")
                .withValidity(1, ChronoUnit.YEARS)
                .sign();

        requesterKeyStore.importCAReply(signResponse, "test", "456");
        certificates = requesterKeyStore.getCertificates("test");

        assertEquals(2, certificates.length);
    }

    public void shouldVerifySignedCertificate() throws Exception {
        Resource ca = Resource.from("classpath:ca.ks");
        KeyStoreAdapter caKeyStore = KeyTools.keyStoreFrom(ca, "ca");

        Resource signedResource = Resource.from("classpath:signed-by-ca.ks");
        KeyStoreAdapter signedKeyStore = KeyTools.keyStoreFrom(signedResource, "1234");
        signedKeyStore.verifyWithTrustStore("test", caKeyStore.toKeyStore());
    }

    public void shouldNotVerifySignedCertificate() throws Exception {
        Resource ca = Resource.from("classpath:signed-by-ca.ks");
        KeyStoreAdapter caKeyStore = KeyTools.keyStoreFrom(ca, "1234");

        Resource signedResource = Resource.from("classpath:ca.ks");
        KeyStoreAdapter signedKeyStore = KeyTools.keyStoreFrom(signedResource, "ca");

        signedKeyStore.verifyWithTrustStore("ca", caKeyStore.toKeyStore());
    }

}
