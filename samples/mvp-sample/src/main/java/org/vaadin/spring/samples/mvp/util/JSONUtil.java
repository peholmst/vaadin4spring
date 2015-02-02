/*
 * Copyright 2015 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.spring.samples.mvp.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.URL;

import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

/**
 * Handy methods for working with JSON
 * <p>
 * I defer to <a href="http://code.google.com/p/google-gson/">GSON</a> to do my
 * "dirty work".
 *
 * @author cphillipson
 *
 */
public class JSONUtil {

    static final GsonBuilder b = new GsonBuilder();
    static {
        b.registerTypeAdapter(DateTime.class, new JSONUtil.DateTimeSerializer());
    }
    static final Gson gson = b.setDateFormat("yyyy-MM-dd HH:mm:ss zzz").setPrettyPrinting().create();

    static final ClassLoader l = Thread.currentThread().getContextClassLoader();


    @SuppressWarnings("unchecked")
    public static <T> T restoreFromJson(final String resourcePath, final Class<T> clazz) {
        return postSerialize((T) restoreFromJson(resourcePath, TypeToken.get(clazz).getType()));
    }

    @SuppressWarnings("unchecked")
    public static <T> T restoreFromJson(final String resourcePath, final Type type) {
        final URL resource = l.getResource(resourcePath);
        if (resource == null) {
            return null;
        }
        return postSerialize((T) restoreFromJson(resource, type));
    }

    @SuppressWarnings("unchecked")
    public static <T> T restoreFromJson(final URL resource, final Type type) {
        try {
            return postSerialize((T) gson.fromJson(getReader(resource.openStream()), type));
        } catch (final Exception e) {
            throw new RuntimeException("Unable to unmarshall file with url: '" + resource.getFile() + "'.", e);
        }
    }

    protected static <T> T postSerialize(final T object) {
        if (object instanceof PostSerializer) {
            ((PostSerializer) object).doAfterSerialize();
        }

        return object;
    }

    private static Reader getReader(final InputStream is) {
        final Reader result = new InputStreamReader(is);
        return result;
    }

    public static URL storeAsJsonInTempFile(final Object o) {
        try {
            final File file = File.createTempFile("storeAsJson", ".gson");
            return storeAsJson(o, file);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static URL storeAsJson(final Object o, final String fileName) {
        final File file = new File(fileName);
        return storeAsJson(o, file);
    }

    public static URL storeAsJson(final Object o, final File file) {
        final String jsonString = toJson(o);
        try {
            final FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(jsonString);
            fileWriter.close();
            return file.toURI().toURL();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(final String jsonString, final Class<T> clazz) {
        return postSerialize(gson.fromJson(jsonString, clazz));
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromJson(final String jsonString, final Type clazz) {
        return postSerialize((T) gson.fromJson(jsonString, clazz));
    }

    public static String toJson(final Object o) {
        final String jsonString = gson.toJson(o);
        return jsonString;
    }

    /**
     * Marker instance for classes that needs to do something after serialization.
     *
     * This will only apply to objects that are top-level objects that are being deserialized.
     *
     * In other words, if Object A contains Object B and B implements this interface, B will no get
     * its doAfterSerialize() method invoked.
     *
     * @author shuynh
     *
     */
    public interface PostSerializer extends Serializable {

        void doAfterSerialize();
    }

    public static class DateTimeSerializer implements JsonSerializer<DateTime> {
        @Override
        public JsonElement serialize(DateTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(SSTimeUtil.dateTimeToIsoNoMillis(src));
        }
    }
}
