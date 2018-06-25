
package com.github.nexus.config;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class ConfigExceptionTest {
    

    @Test
    public void constructWithCause() {
        Throwable cause = new Exception("OUCH");
        ConfigException configException = new ConfigException(cause);
        
        assertThat(configException).hasCause(cause);
        
    }
    
}
