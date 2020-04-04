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

import com.google.cloud.Timestamp
import com.google.cloud.firestore.Blob
import com.google.cloud.firestore.GeoPoint
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import java.util.Date

class FakeDocumentSnapshotTest : FunSpec({
    
    test("Should automatically convert the basic types from Firestore") {
        val map = mutableMapOf<String, Any>(
            "boolean" to true,
            "double" to 1.0,
            "string" to "string",
            "long" to 1L,
            "date" to Date(10_000),
            "timestamp" to Timestamp.MAX_VALUE,
            "blob" to Blob.fromBytes("foo".toByteArray()),
            "geopoint" to GeoPoint(10.0, 10.0)
        )
        
        FakeDocumentSnapshot(FakeDocumentReference(map, mockk())).apply {
            assertSoftly {
                getBoolean("boolean") shouldBe true
                getDouble("double") shouldBe 1.0
                getString("string") shouldBe "string"
                getLong("long") shouldBe 1L
                getDate("date") shouldBe Date(10_000)
                getTimestamp("timestamp") shouldBe Timestamp.MAX_VALUE
                getBlob("blob") shouldBe Blob.fromBytes("foo".toByteArray())
                getGeoPoint("geopoint") shouldBe GeoPoint(10.0, 10.0)
            }
        }
    }
    
    test("Should return the document reference passed to the constructor") {
        val documentReference = FakeDocumentReference(mutableMapOf(), mockk())
        val target = FakeDocumentSnapshot(documentReference)
        target.getReference() shouldBe documentReference
    }
    
    test("Should verify that it contains a field correctly") {
        val map = mutableMapOf<String, Any>("foo" to "bar")
        
        FakeDocumentSnapshot(FakeDocumentReference(map, mockk())).apply { 
            assertSoftly { 
                contains("foo") shouldBe true
                contains("bar") shouldBe false
            }
        }
    }
    
    test("getData should return fields from document reference") {
        val map = mutableMapOf<String, Any>("foo" to "bar")
        
        FakeDocumentSnapshot(FakeDocumentReference(map, mockk())).getData() shouldBe map
    }
    
    test("Should convert to object") {
        val map = mutableMapOf<String, Any>("a" to "a", "b" to 0, "c" to "c")
        val pojo = 
            FakeDocumentSnapshot(FakeDocumentReference(map, mockk())).asDocumentSnapshot().toObject(Pojo::class.java)!!
        pojo.a shouldBe "a"
        pojo.b shouldBe 0
        pojo.c shouldBe "c"
    }
})
data class Pojo constructor(val a: String = "", val b: Int = 0, val c: String = "")

