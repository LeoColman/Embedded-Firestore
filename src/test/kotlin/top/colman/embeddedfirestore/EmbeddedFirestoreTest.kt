/*
 *    Copyright 2020 Leonardo Colman Lopes
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

import com.google.cloud.firestore.Firestore
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import io.kotest.matchers.types.shouldBeInstanceOf
import top.colman.embeddedfirestore.internal.FakeFirestore

class EmbeddedFirestoreTest : FunSpec({
    test("Should return a Fake Firestore client") {
        EmbeddedFirestore().createClient().shouldBeInstanceOf<Firestore>()
        EmbeddedFirestore().createClient().shouldBeInstanceOf<FakeFirestore>()
    }

    test("Should allow me to start a collection and have it listed") {
        val client = EmbeddedFirestore().createClient {
            createCollection("MyCollection")
            createCollection("MyOtherCollection")
        }

        client.listCollections().toList().map { it.id } shouldBe listOf("MyCollection", "MyOtherCollection")
    }
    
    test("Should allow me to start a collection with created documents") {
        val client = EmbeddedFirestore().createClient {
            createCollection("MyCollection") {
                createDocument("myId", mapOf("a" to "b"))
                createDocument(mapOf("a" to "b"))
                createDocument("myPojoId", MyPojo("ab", 0))
                createDocument(MyPojo("ab", 0))
            }
        }
        
        val docs = client.collection("MyCollection").listDocuments().toList()
        docs.shouldHaveSize(4)
        val (first, second, third, fourth) = docs

        first.get().get().id shouldBe "myId"
        first.get().get()["a"] shouldBe "b"
        
        second.get().get().id.shouldNotBeBlank()
        second.get().get()["a"] shouldBe "b"
        
        third.get().get().id shouldBe "myPojoId"
        third.get().get()["string"] shouldBe "ab"
        third.get().get()["int"] shouldBe 0

        fourth.get().get().id.shouldNotBeBlank()
        fourth.get().get()["string"] shouldBe "ab"
        fourth.get().get()["int"] shouldBe 0
    }

    test("Should allow me to add a document") {
        val client = EmbeddedFirestore().createClient {
            createCollection("MyCollection")
        }

        client.collection("MyCollection").add(mapOf("A" to "B"))
    }

    test("Should allow me to inspect document reference added to collection") {
        val client = EmbeddedFirestore().createClient {
            createCollection("MyCollection")
        }

        val addedDocument = client.collection("MyCollection").add(mapOf("A" to "B")).get()

        addedDocument.get().get()["A"] shouldBe "B"
    }
})

private data class MyPojo(val string: String, val int: Int)
