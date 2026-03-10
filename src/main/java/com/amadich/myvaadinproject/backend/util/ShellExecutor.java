package com.amadich.myvaadinproject.backend.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ShellExecutor {

    public static void run(String command) {

        try {

            Process process = Runtime.getRuntime()
                    .exec(new String[]{"bash","-c",command});

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(process.getInputStream())
                    );

            String line;

            while((line = reader.readLine()) != null) {

                System.out.println(line);
            }

            process.waitFor();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
