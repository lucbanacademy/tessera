package com.github.nexus.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class TrustAllManager extends ExtendedTrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        //Do nothing, let all pass
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        //Do nothing, let all pass
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

}
