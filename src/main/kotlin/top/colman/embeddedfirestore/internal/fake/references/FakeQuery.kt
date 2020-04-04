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

import com.google.api.core.ApiFuture
import com.google.api.gax.rpc.ApiStreamObserver
import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.EventListener
import com.google.cloud.firestore.FieldPath
import com.google.cloud.firestore.ListenerRegistration
import com.google.cloud.firestore.Query
import com.google.cloud.firestore.Query.Direction
import com.google.cloud.firestore.QuerySnapshot
import net.bytebuddy.ByteBuddy
import net.bytebuddy.description.method.MethodDescription
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.matcher.ElementMatchers.isPublic
import top.colman.embeddedfirestore.internal.fake.createInstance
import top.colman.embeddedfirestore.internal.fake.future.InstantApiFuture
import top.colman.embeddedfirestore.internal.fake.notNamed
import java.util.concurrent.Executor

@Suppress("TooManyFunctions")
internal class FakeQuery(
    startingDocuments: List<FakeDocumentReference>
) {
    var documentSequence = startingDocuments.asSequence()
    
    fun whereEqualTo(field: String, value: Any?) = apply {
        documentSequence = documentSequence.filter { it.get().get().get(field) == value }
    }.asQuery()
    
    fun whereEqualTo(fieldPath: FieldPath, value: Any?): Query = TODO()

    fun whereLessThan(field: String, value: Any) = filterComparing(field, value) { it < 0 }
    
    fun whereLessThan(fieldPath: FieldPath, value: Any): Query = TODO()
    
    fun whereLessThanOrEqualTo(field: String, value: Any): Query = filterComparing(field, value) { it <= 0 }

    fun whereLessThanOrEqualTo(fieldPath: FieldPath, value: Any): Query = TODO()
    
    fun whereGreaterThan(field: String, value: Any): Query = filterComparing(field, value) { it > 0 }
    
    fun whereGreaterThan(fieldPath: FieldPath, value: Any): Query = TODO()
    
    fun whereGreaterThanOrEqualTo(field: String, value: Any): Query = filterComparing(field, value) { it >= 0 }
    
    fun whereGreaterThanOrEqualTo(fieldPath: FieldPath, value: Any): Query = TODO()
    
    fun whereArrayContains(field: String, value: Any): Query = apply { 
        documentSequence = documentSequence.filter { 
            val docValue = it.get().get()[field] as Array<*> 
            docValue.contains(value)
        }
    }.asQuery()
    
    fun whereArrayContains(fieldPath: FieldPath, value: Any): Query = TODO()
    
    fun orderBy(field: String): Query = orderBy(field, Direction.ASCENDING)
    
    fun orderBy(fieldPath: FieldPath): Query = TODO()
    
    fun orderBy(field: String, direction: Direction): Query = apply { 
        documentSequence = when(direction) {
            Direction.ASCENDING -> documentSequence.sortedBy { it.get().get()[field] as Comparable<Any> }
            Direction.DESCENDING -> documentSequence.sortedByDescending { it.get().get()[field] as Comparable<Any> }
        }
    }.asQuery()
    
    fun orderBy(fieldPath: FieldPath, direction: Direction): Query = TODO()
    
    fun limit(limit: Int): Query = apply { 
        documentSequence = documentSequence.take(limit)
    }.asQuery()
    
    fun offset(offset: Int): Query = apply {
        documentSequence = documentSequence.drop(offset)
    }.asQuery()
    
    fun startAt(snapshot: DocumentSnapshot): Query = TODO()
    
    fun startAt(vararg fieldValues: String): Query = TODO()
    
    fun select(vararg fields: String): Query = TODO()
    
    fun select(vararg fieldPaths: FieldPath): Query = TODO()
    
    fun startAfter(snapshot: DocumentSnapshot): Query = TODO()
    
    fun startAfter(vararg fieldValues: String): Query = TODO()
    
    fun endBefore(snapshot: DocumentSnapshot): Query = TODO()
    
    fun endBefore(vararg fieldValues: String): Query = TODO()

    fun endAt(snapshot: DocumentSnapshot): Query = TODO()

    fun endAt(vararg fieldValues: String): Query = TODO()
    
    fun stream(responseObserver: ApiStreamObserver<DocumentSnapshot>): Unit = TODO()
    
    fun get(): ApiFuture<QuerySnapshot> = InstantApiFuture(FakeQuerySnapshot(this))
    
    fun addSnapshotListener(listener: EventListener<QuerySnapshot>): ListenerRegistration = TODO()
    
    fun addSnapshotListener(executor: Executor, listener: EventListener<QuerySnapshot>): ListenerRegistration = TODO()
    
    private fun filterComparing(field: String, value: Any, predicate: (Int) -> Boolean) = apply { 
        documentSequence = documentSequence.filter {
            val docValue = it.get().get()[field]

            val compareTo = docValue?.myCompareTo(value)
            if (compareTo == null) false
            else predicate(compareTo)
        }
    }.asQuery()
    
    private fun Any.myCompareTo(other: Any?): Int? {
        if (other == null) return null
        val thisClass = this::class.java
        val otherClass = this::class.java
        
        return thisClass.methods
            .filter { it.name == "compareTo" }
            .firstOrNull { it.parameters.size == 1 && it.parameters.first().type.isAssignableFrom(otherClass) }
            ?.invoke(this, other) as? Int
        
    }

    fun asQuery(): Query {
        return ByteBuddy()
            .subclass(Query::class.java)
            .method(isPublic<MethodDescription>().and(notNamed("getFirestore")))
            .intercept(MethodDelegation.to(this))
            .make()
            .load(Query::class.java.classLoader)
            .loaded.createInstance()
    }
    
}
