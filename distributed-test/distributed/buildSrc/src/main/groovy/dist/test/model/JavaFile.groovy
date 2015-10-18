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
package dist.test.model

import dist.test.task.ClassGeneration

class JavaFile {

    static final BASE_PACKAGE = ClassGeneration.BASE_PACKAGE

    final String dir

    final Names midPackage

    final Names type

    JavaFile(String dir, Names midPackage, Names type) {
        this.dir = dir
        this.midPackage = midPackage
        this.type = type
    }

    String getTypeName() {
        "${type.asTypeName()}Test"
    }

    String getTime() {
        type.toTime()
    }

    String packageName() {
        "${BASE_PACKAGE}.${midPackage.breeds}"
    }

    String toFileName() {
        "${dir}/${BASE_PACKAGE.replace('.', '/')}/${midPackage.breeds}/${type.asTypeName()}Test.java"
    }
}
