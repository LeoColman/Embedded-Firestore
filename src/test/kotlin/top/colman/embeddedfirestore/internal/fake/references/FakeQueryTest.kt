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

import com.google.cloud.firestore.Query
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.mockk

class FakeQueryTest : FunSpec({

    val queried = FakeQuery(List(5) {
        FakeDocumentReference(
            mutableMapOf(
                "value" to it,
                "inverse-value" to (4 - it),
                "array" to arrayOf(it, it + 1, it + 2)
            ),
            mockk()
        )
    })
    
    test("EqualTo") {
        val returnedValues = queried.whereEqualTo("value", 3).get().get()
        returnedValues.documents shouldHaveSize 1
        returnedValues.documents.single()["value"] shouldBe 3
    }
    
    test("LessThan") {
        val returnedValues = queried.whereLessThan("value", 3).get().get()
        returnedValues.documents shouldHaveSize 3
        returnedValues.documents[0]["value"] shouldBe 0 
        returnedValues.documents[1]["value"] shouldBe 1 
        returnedValues.documents[2]["value"] shouldBe 2 
    }

    test("LessThanOrEqualTo") {
        val returnedValues = queried.whereLessThanOrEqualTo("value", 3).get().get()
        returnedValues.documents shouldHaveSize 4
        returnedValues.documents[0]["value"] shouldBe 0
        returnedValues.documents[1]["value"] shouldBe 1
        returnedValues.documents[2]["value"] shouldBe 2
        returnedValues.documents[3]["value"] shouldBe 3
    }
    
    test("GreaterThan") {
        val returnedValues = queried.whereGreaterThan("value", 3).get().get()
        returnedValues.documents shouldHaveSize 1
        returnedValues.documents[0]["value"] shouldBe 4
    }

    test("GreaterThanOrEqualTo") {
        val returnedValues = queried.whereGreaterThanOrEqualTo("value", 3).get().get()
        returnedValues.documents shouldHaveSize 2
        returnedValues.documents[0]["value"] shouldBe 3
        returnedValues.documents[1]["value"] shouldBe 4
    }
    
    test("WhereArrayContains") {
        val returnedValues = queried.whereArrayContains("array", 4).get().get()
        returnedValues.documents shouldHaveSize 3
        returnedValues.documents[0]["value"] shouldBe 2
        returnedValues.documents[1]["value"] shouldBe 3
        returnedValues.documents[2]["value"] shouldBe 4
    }
    
    test("OrderBy") {
        val returnedValues = queried.whereLessThanOrEqualTo("value", 3).orderBy("inverse-value").get().get()
        returnedValues.documents shouldHaveSize 4
        returnedValues.documents[0]["value"] shouldBe 3
        returnedValues.documents[1]["value"] shouldBe 2
        returnedValues.documents[2]["value"] shouldBe 1
        returnedValues.documents[3]["value"] shouldBe 0
    }
    
    test("OrderByDesc") {
        val returnedValues = queried
            .whereLessThanOrEqualTo("value", 3)
            .orderBy("inverse-value", Query.Direction.DESCENDING).get().get()
        returnedValues.documents shouldHaveSize 4
        returnedValues.documents[0]["value"] shouldBe 0
        returnedValues.documents[1]["value"] shouldBe 1
        returnedValues.documents[2]["value"] shouldBe 2
        returnedValues.documents[3]["value"] shouldBe 3
    }
    
    test("Limit") {
        val returnedValues = queried.whereLessThanOrEqualTo("value", 3).limit(2).get().get()
        returnedValues.documents shouldHaveSize 2
        returnedValues.documents[0]["value"] shouldBe 0
        returnedValues.documents[1]["value"] shouldBe 1
    }

    test("Offset") {
        val returnedValues = queried.whereLessThanOrEqualTo("value", 3).offset(2).get().get()
        returnedValues.documents shouldHaveSize 2
        returnedValues.documents[0]["value"] shouldBe 2
        returnedValues.documents[1]["value"] shouldBe 3
    }
    
    isolation = IsolationMode.InstancePerTest
})
