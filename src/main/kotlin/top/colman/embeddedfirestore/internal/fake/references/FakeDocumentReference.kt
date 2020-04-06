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
import com.google.cloud.Timestamp
import com.google.cloud.firestore.CollectionReference
import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.EventListener
import com.google.cloud.firestore.FieldPath
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.ListenerRegistration
import com.google.cloud.firestore.Precondition
import com.google.cloud.firestore.SetOptions
import com.google.cloud.firestore.WriteResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.bytebuddy.ByteBuddy
import net.bytebuddy.implementation.MethodDelegation
import top.colman.embeddedfirestore.internal.fake.createInstance
import top.colman.embeddedfirestore.internal.fake.future.InstantApiFuture
import top.colman.embeddedfirestore.internal.fake.notNamed
import java.util.concurrent.Executor

@Suppress("TooManyFunctions")
internal class FakeDocumentReference(
    var fields: MutableMap<String, Any>,
    val fakeParent: FakeCollectionReference,
    val id: String = randomId(),
    val path: String = "" + id
) {

    fun getFirestore(): Firestore = fakeParent.getFirestore()
    
    fun getName(): String = TODO()
    
    fun getParent(): CollectionReference = fakeParent.asCollectionReference()
    
    fun collection(collectionPath: String): CollectionReference = TODO()
    
    fun create(fields: Map<String, Any>): ApiFuture<WriteResult> = TODO()
    
    fun create(pojo: Any): ApiFuture<WriteResult> = TODO()
    
    fun set(fields: Map<String, Any>): ApiFuture<WriteResult> {
        this.fields = fields.toMutableMap()
        fakeParent.setDocument(id, this)
        return InstantApiFuture(
            FakeWriteResult(Timestamp.now()).asWriteResult()
        )
    }
    
    fun set(fields: Map<String, Any>, options: SetOptions): ApiFuture<WriteResult> = TODO()
    
    fun set(pojo: Any): ApiFuture<WriteResult> = set(pojo.asMap())
    
    fun set(pojo: Any, options: SetOptions): ApiFuture<WriteResult> = TODO()
    
    fun update(fields: Map<String, Any>): ApiFuture<WriteResult> = TODO()
    
    fun update(fields: Map<String, Any>, options: Precondition): ApiFuture<WriteResult> = TODO()
    
    fun update(field: String, value: Any, vararg moreFieldsAndValues: Any): ApiFuture<WriteResult> = TODO()
    
    fun update(fieldPath: FieldPath, value: Any, vararg moreFieldsAndValues: Any): ApiFuture<WriteResult> = TODO()
    
    fun update(
        options: Precondition,
        field: String,
        value: Any,
        vararg moreFieldsAndValues: Any
    ): ApiFuture<WriteResult> = TODO()
    
    fun update(
        options: Precondition,
        fieldPath: FieldPath,
        value: Any,
        vararg moreFieldsAndValues: Any
    ): ApiFuture<WriteResult> = TODO()
    
    fun delete(options: Precondition): ApiFuture<WriteResult> = TODO()
    
    fun delete(): ApiFuture<WriteResult> {
        fakeParent.delete(id)
        return InstantApiFuture(FakeWriteResult(Timestamp.now()).asWriteResult())
    }
    
    fun get(): ApiFuture<DocumentSnapshot> = InstantApiFuture(FakeDocumentSnapshot(this).asDocumentSnapshot())
    
    fun listCollections(): Iterable<CollectionReference> = TODO()
    
    fun getCollections() = listCollections()
    
    fun addSnapshotListener(
        executor: Executor,
        listener: EventListener<DocumentSnapshot>
    ): ListenerRegistration = TODO()
    
    fun addSnapshotListener(listener: EventListener<DocumentSnapshot>): ListenerRegistration = TODO()
    
    override fun toString() = "DocumentReference{path=${path}}"
    
    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(other == null || other !is DocumentReference) return false
        return fields == other.get().get().data
        
    }
    
    override fun hashCode() = fields.hashCode()
    
    fun asDocumentReference(): DocumentReference {
        return ByteBuddy()
            .subclass(DocumentReference::class.java)
            .method(notNamed("getResourcePath").and(notNamed("clone")))
            .intercept(MethodDelegation.to(this))
            .make()
            .load(DocumentReference::class.java.classLoader)
            .loaded.createInstance()
    }
}

@Suppress("MagicNumber")
private fun randomId(): String {
    return List(20) { 
        (('a'..'z') + ('A'..'Z') + ('0'..'9')).random()
    }.joinToString(separator = "")
}

private val gson = Gson()
private fun <T> T.asMap(): Map<String, Any> {
    val json = gson.toJson(this)
    return gson.fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
}
