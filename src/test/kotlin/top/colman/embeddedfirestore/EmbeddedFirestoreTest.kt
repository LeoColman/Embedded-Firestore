/*
 *    Copyright 2019 Leonardo Colman Lopes
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package top.colman.embeddedfirestore

import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec
import net.bytebuddy.ByteBuddy
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.matcher.ElementMatchers
import net.bytebuddy.matcher.ElementMatchers.isDeclaredBy
import net.bytebuddy.matcher.ElementMatchers.not
import top.colman.embeddedfirestore.internal.FakeFirestore

class EmbeddedFirestoreTest : FunSpec() {
    
    init {
        test("Should return a Fake Firestore client") {
            EmbeddedFirestore().createClient().shouldBeInstanceOf<FakeFirestore>()
        }
        
        test("Should allow me to start a collection and have it listed") {
            val client = EmbeddedFirestore().createClient {
                createCollection("MyCollection")
                createCollection("MyOtherCollection")
            }
            
            client.listCollections().toList().map { it.id } shouldBe listOf("MyCollection", "MyOtherCollection")
        }
        
        test("Should allow me to add a document") {
            val client = EmbeddedFirestore().createClient { 
                createCollection("MyCollection")
            }
            
            client.collection("MyCollection").add(mapOf("A" to "B"))
        }
    }
}
