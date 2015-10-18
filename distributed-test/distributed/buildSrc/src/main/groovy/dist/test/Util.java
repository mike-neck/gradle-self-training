/*
 * Copyright 2015 Shinya Mochida
 * 
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dist.test;

import java.net.URL;

public final class Util {

    private Util() {}

    public static String toTypeName(String name) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name is null or empty.");
        }
        return new StringBuilder(name.substring(0, 1).toUpperCase())
                .append(name.substring(1))
                .toString();
    }

    public static URL template(String name) throws NoSuchTemplateException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String templateName = "templates/" + name;
        URL url = loader.getResource(templateName);
        if (url == null) {
            throw new NoSuchTemplateException("There is no template named [" + name + "].");
        }
        return url;
    }
}
