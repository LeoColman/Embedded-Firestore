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
import com.google.cloud.firestore.DocumentChange
import com.google.cloud.firestore.Query
import com.google.cloud.firestore.QueryDocumentSnapshot
import com.google.cloud.firestore.QuerySnapshot

internal class FakeQuerySnapshot(
    private val fakeQuery: FakeQuery
) : QuerySnapshot(fakeQuery.asQuery(), Timestamp.now()) {
    

    override fun hashCode(): Int {
        TODO("Not yet implemented")
    }

    override fun equals(other: Any?): Boolean {
        TODO("Not yet implemented")
    }

    override fun getDocumentChanges(): MutableList<DocumentChange> {
        TODO("Not yet implemented")
    }

    override fun getDocuments(): MutableList<QueryDocumentSnapshot> =
        fakeQuery.documentSequence.map { 
            FakeQueryDocumentSnapshot(FakeDocumentSnapshot(it)).asQueryDocumentSnapshot() 
        }.toMutableList()

    override fun size(): Int = fakeQuery.documentSequence.toList().size
}
