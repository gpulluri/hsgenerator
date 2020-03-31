package com.hs.generator.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.Optional;

/**
 * Created by gpulluri on 5/12/17.
 */
public class SpecParser {

    public Optional<Spec> parseFile(String file) {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream in = SpecParser.class.getResourceAsStream(file);
            InputStreamReader is = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(is);
            String line = br.readLine();
            while(line != null) {
                sb.append(line).append("\n");
                line = br.readLine();
            }

        } catch (Exception e) {
            return Optional.empty();
        }

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Spec spec = gson.fromJson(sb.toString(), Spec.class);

        return Optional.of(spec);
    }

}
