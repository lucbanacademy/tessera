package com.quorum.tessera.config.migration;

import com.quorum.tessera.config.cli.CliResult;

public class Main {

    public static void main(String... args) throws Exception {
        LegacyCliAdapter adapter = new LegacyCliAdapter();
        try {
            CliResult result = adapter.execute(args);
            System.exit(result.getStatus());
        } catch (final Exception ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }

    }
}
