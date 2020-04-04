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

package top.colman.embeddedfirestore.internal.fake.references

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import top.colman.embeddedfirestore.EmbeddedFirestore

class FakeDocumentReferenceTest : FunSpec({
    
    test("Should update a document") {
        val client = EmbeddedFirestore().createClient { 
            createCollection("MyCollection")
        }
        
        val doc = client.collection("MyCollection").add(mapOf("a" to "b")).get()
        client.collection("MyCollection").listDocuments().single().get().get()["a"] shouldBe "b"

        doc.set(mapOf("a" to "c")).get()
        
        client.collection("MyCollection").listDocuments().single().get().get()["a"] shouldBe "c"
        
    }
    
    test("Should delete a document") {
        val client = EmbeddedFirestore().createClient {
            createCollection("MyCollection")
        }

        client.collection("MyCollection").add(mapOf("a" to "b")).get()

        val doc = client.collection("MyCollection")
            .whereEqualTo("a", "b")
            .get().get().single().reference.delete().get()

        client.collection("MyCollection").listDocuments().toList().shouldBeEmpty()
    }
})
