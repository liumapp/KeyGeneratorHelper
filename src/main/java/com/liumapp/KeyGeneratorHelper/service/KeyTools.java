package com.liumapp.KeyGeneratorHelper.service;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * Created by liumapp on 10/12/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
public class KeyTools {

    public static KeyStoreAdapter newKeyStore(String password) throws KeyStoreException {
        try {
            return createKeyStoreAdapter(null, password);
        } catch (IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new KeyStoreException(e);
        }
    }

    private static KeyStoreAdapter createKeyStoreAdapter(InputStream ksStream, String password) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(ksStream, password.toCharArray());
        return new KeyStoreAdapter(keyStore, password);
    }

}
