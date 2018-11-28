package com.quorum.tessera.key.vault.hashicorp;

import com.quorum.tessera.config.HashicorpKeyVaultConfig;
import com.quorum.tessera.key.vault.VaultSecretNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.verification.VerificationMode;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class HashicorpKeyVaultServiceTest {

    private HashicorpKeyVaultService keyVaultService;
    private VaultTemplate vaultTemplate;

    private final String url = "someurl";
    private final String secretPath = "secret/path";
    private final String secretName = "secretname";


    @Before
    public void setUp() {
        HashicorpKeyVaultConfig keyVaultConfig = mock(HashicorpKeyVaultConfig.class);
        when(keyVaultConfig.getUrl()).thenReturn(url);
        vaultTemplate = mock(VaultTemplate.class);

        keyVaultService = new HashicorpKeyVaultService(keyVaultConfig, vaultTemplate);
    }

    @Test
    public void getSecretThrowsExceptionIfSecretNotFoundAtPath() {
        when(vaultTemplate.read(anyString())).thenReturn(null);

        final Throwable ex = catchThrowable(() -> keyVaultService.getHashicorpSecret(secretPath, secretName));

        assertThat(ex).isInstanceOf(VaultSecretNotFoundException.class);
        assertThat(ex).hasMessage("Hashicorp Vault secret not found at path " + secretPath + " in vault " + url);

    }

    @Test
    public void getSecretThrowsExceptionIfNoDataForSpecifiedSecret() {
        VaultResponse vaultResponse = mock(VaultResponse.class);

        when(vaultTemplate.read(anyString())).thenReturn(vaultResponse);
        when(vaultResponse.getData()).thenReturn(null);

        final Throwable ex = catchThrowable(() -> keyVaultService.getHashicorpSecret(secretPath, secretName));

        assertThat(ex).isInstanceOf(VaultSecretNotFoundException.class);
        assertThat(ex).hasMessage("No data for Hashicorp Vault secret at path " + secretPath + " in vault " + url);
    }

    @Test
    public void getSecretThrowsExceptionIfNoValueForSpecifiedSecretName() {
        VaultResponse vaultResponse = mock(VaultResponse.class);
        Map<String, Object> secretData = Collections.singletonMap("othername", "value");

        when(vaultTemplate.read(anyString())).thenReturn(vaultResponse);
        when(vaultResponse.getData()).thenReturn(secretData);

        final Throwable ex = catchThrowable(() -> keyVaultService.getHashicorpSecret(secretPath, secretName));

        assertThat(ex).isInstanceOf(VaultSecretNotFoundException.class);
        assertThat(ex).hasMessage("Value for secret id " + secretName + " not found at path " + secretPath + " in vault " + url);
    }

    @Test
    public void getSecretRetrievesValueForSpecifiedKeyAtSpecifiedPath() {
        VaultResponse vaultResponse = mock(VaultResponse.class);
        String value = "value";
        Map<String, Object> secretData = Collections.singletonMap(secretName, value);

        when(vaultTemplate.read(anyString())).thenReturn(vaultResponse);
        when(vaultResponse.getData()).thenReturn(secretData);

        String result = keyVaultService.getHashicorpSecret(secretPath, secretName);

        assertThat(result).isEqualTo(value);
    }

    @Test
    public void setSecretCallsVaultTemplate() {
        Map<String, String> secretData = Collections.singletonMap(secretName, "value");
        keyVaultService.setHashicorpSecret(secretPath, secretData);

        verify(vaultTemplate, times(1)).write(secretPath, secretData);
    }

    @Test
    //TODO Not ideal - see class todos
    public void getSecretAndSetSecretReturnNull() {
        assertThat(keyVaultService.getSecret(secretName)).isNull();
        assertThat(keyVaultService.setSecret(secretPath, secretName)).isNull();
    }

}
